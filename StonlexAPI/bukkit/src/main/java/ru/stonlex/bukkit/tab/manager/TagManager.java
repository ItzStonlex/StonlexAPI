package ru.stonlex.bukkit.tab.manager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.stonlex.bukkit.protocol.packet.scoreboard.WrapperPlayServerScoreboardTeam;
import ru.stonlex.bukkit.tab.PlayerTag;

import java.util.HashMap;
import java.util.Map;

public final class TagManager {

    @Getter
    private final Map<String, PlayerTag> playerTagMap = new HashMap<>();


    /**
     * Получить PlayerTag игрока
     *
     * @param playerName - ник игрока
     */
    public PlayerTag getPlayerTag(String playerName) {
        return playerTagMap.get(playerName.toLowerCase());
    }

    /**
     * Получить PlayerTag игрока
     *
     * @param player - игрок
     */
    public PlayerTag getPlayerTag(Player player) {
        return getPlayerTag(player.getName());
    }


    /**
     * Установить тег в табе для игрока
     *
     * @param player - игрок, которому установить тег
     * @param receiver - получатель пакета
     * @param teamName - имя тимы для тега
     * @param prefix - префикс
     * @param suffix - суффикс
     */
    public void setTagToPlayer(Player player, Player receiver, String teamName, String prefix, String suffix) {
        PlayerTag playerTag = getPlayerTag(player);

        if (playerTag == null) {
            playerTag = new PlayerTag(player, prefix, suffix, teamName);

            playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        }

        playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED);

        playerTagMap.put(player.getName().toLowerCase(), playerTag);
    }

    /**
     * Установить тег в табе для игрока
     *
     * @param player - игрок, которому установить тег
     * @param receiver - получатель пакета
     * @param prefix - префикс
     * @param suffix - суффикс
     */
    public void setTagToPlayer(Player player, Player receiver, String prefix, String suffix) {
        setTagToPlayer(player, receiver, player.getName(), prefix, suffix);
    }

    /**
     * Установить префикс в табе для игрока
     *
     * @param player игрок, которому установить префикс
     * @param receiver - получатель пакета
     * @param prefix - префикс
     */
    public void setPrefixToPlayer(Player player, Player receiver, String prefix) {
        PlayerTag playerTag = getPlayerTag(player);

        if (playerTag == null) {
            playerTag = new PlayerTag(player, prefix, "", player.getName());

            playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        }

        playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED);

        playerTagMap.put(player.getName().toLowerCase(), playerTag);
    }

    /**
     * Установить суффикс в табе для игрока
     *
     * @param player игрок, которому установить суффикс
     * @param receiver - получатель пакета
     * @param suffix - суффикс
     */
    public void setSuffixToPlayer(Player player, Player receiver, String suffix) {
        PlayerTag playerTag = getPlayerTag(player);

        if (playerTag == null) {
            playerTag = new PlayerTag(player, "", suffix, player.getName());

            playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        }

        playerTag.sendPacket(receiver, WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED);

        playerTagMap.put(player.getName().toLowerCase(), playerTag);
    }

    /**
     * Установить тег в табе для игрока
     *
     * @param player - игрок, которому установить тег
     * @param teamName - имя тимы для тега
     * @param prefix - префикс
     * @param suffix - суффикс
     */
    public void setTag(Player player, String teamName, String prefix, String suffix) {
        Bukkit.getOnlinePlayers().forEach(
                receiver -> setTagToPlayer(player, receiver, player.getName(), prefix, suffix));
    }

    /**
     * Установить тег в табе для игрока
     *
     * @param player - игрок, которому установить тег
     * @param prefix - префикс
     * @param suffix - суффикс
     */
    public void setTag(Player player, String prefix, String suffix) {
        Bukkit.getOnlinePlayers().forEach(
                receiver -> setTagToPlayer(player, receiver, prefix, suffix));
    }

    /**
     * Установить префикс в табе для игрока
     *
     * @param player игрок, которому установить префикс
     * @param prefix - префикс
     */
    public void setPrefix(Player player, String prefix) {
        Bukkit.getOnlinePlayers().forEach(
                receiver -> setPrefixToPlayer(player, receiver, prefix));
    }

    /**
     * Установить суффикс в табе для игрока
     *
     * @param player игрок, которому установить суффикс
     * @param suffix - суффикс
     */
    public void setSuffix(Player player, String suffix) {
        Bukkit.getOnlinePlayers().forEach(
                receiver -> setSuffixToPlayer(player, receiver, suffix));
    }

}