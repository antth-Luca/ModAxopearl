package io.github.antthluca.axopearl.screens;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.screens.menus.EletricAnvilMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EletricAnvilScreen extends AbstractContainerScreen<EletricAnvilMenu> implements ContainerListener {
    private static final ResourceLocation ERROR_SPRITE = ResourceLocation.fromNamespaceAndPath(Axopearl.MODID, "container/eletric_anvil/x_error.png");
    private final ResourceLocation ELETRIC_ANVIL_CONTAINER = ResourceLocation.fromNamespaceAndPath(Axopearl.MODID, "textures/gui/container/eletric_anvil.png");
    private final Player player;

    // CONSTRUCTOR
    public EletricAnvilScreen(EletricAnvilMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.player = playerInventory.player;
        this.imageWidth = 176;
        this.imageHeight = 195;
        this.titleLabelX = 56;
        this.inventoryLabelY = imageHeight - 94;
    }

    protected void init() {
        super.init();
        this.menu.addSlotListener(this);
    }

    // SUPER
    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) { }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) { }

    @Override
    public void removed() {
        super.removed();

        this.menu.removeSlotListener(this);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int var1, int var2, float var3) {
        super.render(guiGraphics, var1, var2, var3);
        this.renderTooltip(guiGraphics, var1, var2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.ELETRIC_ANVIL_CONTAINER, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        this.renderErrorIcon(guiGraphics, this.leftPos, this.topPos);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.minecraft.player.experienceDisplayStartTick = this.minecraft.player.tickCount;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        this.init(minecraft, width, height);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isEscape()) {
            this.minecraft.player.closeContainer();
            return true;
        }

        return super.keyPressed(keyEvent);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        int xpCost = this.menu.getCost();
        if (xpCost > 0) {
            int color = -8323296;
            Component component = null;
            for (int o = EletricAnvilMenu.OUTPUT_SLOT_START; o < EletricAnvilMenu.PLAYER_INV_SLOT_START; o++) {
                Slot outSlot = this.menu.getSlot(o);
                if (outSlot.hasItem() && component == null) {
                    component = Component.translatable("container.repair.cost", xpCost);
                    if (!outSlot.mayPickup(this.player)) {
                        color = -40864;
                    }
                }
            }

            if (component != null) {
                int calcW = this.imageWidth - 8 - this.font.width(component) - 2;
                int h = 98;
                guiGraphics.fill(calcW - 2, 97, this.imageWidth - 8, 108, 1325400064);
                guiGraphics.drawString(this.font, component, calcW, h, color);
            }
        }
    }

    // MAIN
    protected void renderErrorIcon(GuiGraphics guiGraphics, int var1, int var2) {
        if (this.menu.getSlot(EletricAnvilMenu.ADDITIONAL_SLOT).hasItem()) {
            for (int i = EletricAnvilMenu.INPUT_SLOT_START; i < EletricAnvilMenu.ADDITIONAL_SLOT; i++) {
                if (this.menu.getSlot(i).hasItem() && !this.menu.getSlot(i + 5).hasItem()) {
                    int calcX = 0;
                    int calcY = 0;

                    if (i == 0) {
                        calcX = var1 + 99;
                        calcY = var2 + 5;
                    } else if (i == 1) {
                        calcX = var1 + 109;
                        calcY = var2 + 27;
                    } else if (i == 2) {
                        calcX = var1 + 109;
                        calcY = var2 + 51;
                    } else if (i == 3) {
                        calcX = var1 + 99;
                        calcY = var2 + 73;
                    }

                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, calcX, calcY, 28, 21);
                }
            }
        }
    }
}
