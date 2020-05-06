package ru.stonlex.bukkit.inventory.button.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import ru.stonlex.bukkit.inventory.button.IBukkitInventoryButton;
import ru.stonlex.bukkit.inventory.button.action.impl.ClickableButtonAction;

@RequiredArgsConstructor
@Getter
public class ClickableStonlexInventoryButton implements IBukkitInventoryButton {

    private final ItemStack itemStack;

    private final ClickableButtonAction buttonAction;

}
