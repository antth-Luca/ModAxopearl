package io.github.antthluca.axopearl.screens;

import io.github.antthluca.axopearl.init.InitMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class EletricAnvilMenu extends AbstractContainerMenu {
    public EletricAnvilMenu(int containerId, Inventory playerInv) {
        super(InitMenuTypes.ELETRIC_ANVIL_MENU.get(), containerId);
    }
}
