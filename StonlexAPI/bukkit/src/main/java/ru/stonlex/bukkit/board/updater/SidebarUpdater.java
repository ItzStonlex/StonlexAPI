package ru.stonlex.bukkit.board.updater;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Getter;
import lombok.NonNull;
import ru.stonlex.bukkit.board.MoonSidebar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class SidebarUpdater {

    private final Multimap<Long, Consumer<MoonSidebar>> tasks;

    @Getter
    private final MoonSidebar sidebar;

    private Thread executionThread;

    @Getter
    private volatile boolean started;

    /**
     * Конструктор апдейтера
     *
     * @param sidebar - скорборд
     */
    public SidebarUpdater(@NonNull MoonSidebar sidebar) {
        this.sidebar = sidebar;
        this.tasks = Multimaps.synchronizedSetMultimap(Multimaps.newSetMultimap(new HashMap<>(), HashSet::new));
    }

    /**
     * Получение списка задач.
     *
     * @return список задач
     */
    public Multimap<Long, Consumer<MoonSidebar>> getTasks() {
        return Multimaps.unmodifiableMultimap(tasks);
    }

    /**
     * Очистка всех задач.
     */
    public void clearTasks() {
        this.tasks.clear();
    }

    /**
     * Остановка апдейтера.
     */
    public void stop() {
        Preconditions.checkState(isStarted(), "Updating is not started.");

        executionThread.interrupt();
        if (!executionThread.isInterrupted()) executionThread.stop();
    }

    /**
     * Добавление новой задачи
     *
     * @param task  - реализация задачи
     * @param delay - период ее вызова
     * @return инстанс этого класса
     */
    public SidebarUpdater newTask(@NonNull Consumer<MoonSidebar> task, long delay) {
        if (delay < 0) throw new IllegalArgumentException("Delay value must be > 0");
        tasks.put(delay, task);
        return this;
    }

    /**
     * Стартовать обновление
     */
    public void start() {
        Preconditions.checkState(!isStarted(), "Updating already started.");

        startTaskExecution(); //Lets rock!

        started = true;
    }

    /**
     * Внутренний метод для выполнения задач
     */
    private void startTaskExecution() {
        AtomicLong time = new AtomicLong();

        Runnable updater = () -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException ignored) {
                }

                tasks.asMap().entrySet()
                        .stream()
                        .filter(entry -> time.get() % entry.getKey() == 0)
                        .forEach(entry -> entry.getValue().forEach(consumer -> consumer.accept(sidebar)));

                time.incrementAndGet();
            }
        };
        executionThread = new Thread(updater, String.format("%s-Updater", sidebar.getObjective().getName()));
        executionThread.start();
    }

}