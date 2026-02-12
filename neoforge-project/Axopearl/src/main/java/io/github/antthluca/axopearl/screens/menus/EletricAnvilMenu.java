package io.github.antthluca.axopearl.screens.menus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.antthluca.axopearl.init.InitBlocks;
import io.github.antthluca.axopearl.init.InitMenuTypes;
import io.github.antthluca.axopearl.utils.EnchantmentUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;

public class EletricAnvilMenu extends AbstractContainerMenu {
    public static final int SLOTS_PER_ROW = 9;
    public static final int ROWS_PER_INV = 3;
    public static final int INPUT_SLOT_START = 0;
    public static final int INPUT_SLOT_END = 4;
    public static final int ADDITIONAL_SLOT = 4;
    public static final int OUTPUT_SLOT_START = 5;
    public static final int OUTPUT_SLOT_END = 8;
    public static final int PLAYER_INV_SLOT_START = 9;
    public static final int PLAYER_INV_SLOT_END = 36;
    public static final int PLAYER_HOTBAR_SLOT_START = 37;
    public static final int PLAYER_HOTBAR_SLOT_END = 46;
    private final int resultSlotIndex;
    protected final ContainerLevelAccess access;
    protected final Player player;
    protected final Container inputSlots;
    protected final Container resultSlots;
    private final DataSlot cost = DataSlot.standalone();

    // CONSTRUCTORS
    public EletricAnvilMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public EletricAnvilMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
        super(InitMenuTypes.ELETRIC_ANVIL_MENU.get(), containerId);
        this.access = access;
        this.player = playerInv.player;
        this.inputSlots = createSimpleContainer(5);
        this.resultSlots = createSimpleContainer(4);
        List<Slot> inputSlots = createInputSlots();
        this.resultSlotIndex = inputSlots.size();

        // Inputs 0-4
        for (Slot iSlot : inputSlots) {
            this.addSlot(iSlot);
        }

        // Outputs 0-3
        for (Slot oSlot : createresultSlots()) {
            this.addSlot(oSlot);
        }

        // Cost
        addDataSlot(this.cost);

        // Default
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

    private SimpleContainer createSimpleContainer(int size) {
        return new SimpleContainer(size) {
            public void setChanged() {
                super.setChanged();
                EletricAnvilMenu.this.slotsChanged(this);
            }
        };
    }

    private List<Slot> createInputSlots() {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(new Slot(this.inputSlots, 0, 18, 8));
        slots.add(new Slot(this.inputSlots, 1, 8, 30));
        slots.add(new Slot(this.inputSlots, 2, 8, 54));
        slots.add(new Slot(this.inputSlots, 3, 18, 76));
        slots.add(new Slot(this.inputSlots, 4, 62, 42));
        return slots;
    }

    private List<Slot> createresultSlots() {
        List<Slot> slots = new ArrayList<Slot>();
        slots.add(new Slot(this.resultSlots, 0, 106, 8));
        slots.add(new Slot(this.resultSlots, 1, 116, 30));
        slots.add(new Slot(this.resultSlots, 2, 116, 54));
        slots.add(new Slot(this.resultSlots, 3, 106, 76));
        return slots;
    }

    private void addPlayerInventory(Inventory playerInv) {
        for (int y = 0; y < ROWS_PER_INV; ++y) {
            for (int x = 0; x < SLOTS_PER_ROW; ++x) {
                this.addSlot(new Slot(
                    playerInv,
                    x + y * 9 + 9,
                    8 + x * 18,
                    113 + y * 18
                ));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInv) {
        for (int c = 0; c < SLOTS_PER_ROW; ++c) {
            this.addSlot(new Slot(
                playerInv,
                c,
                8 + c * 18,
                171
            ));
        }
    }

    // GETTERS AND SETTERS
    public int getCost() { return this.cost.get(); }

    public void setCost(int value) { this.cost.set(Math.max(0, value)); }

    //SUPER
    @Override
    public void slotsChanged(Container cont) {
        super.slotsChanged(cont);

        if (cont == this.inputSlots) {
            this.createResult();
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        this.access.execute((level, pos) -> this.clearContainer(player, this.inputSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return (Boolean) this.access.evaluate((level, pos) -> !this.isValidBlock(level.getBlockState(pos))
            ? false
            : player.canInteractWithBlock(pos, (double) 4.0F), true);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int idx) {
        Slot slot = this.slots.get(idx);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = slot.getItem();
        ItemStack stackCopy = slotStack.copy();

        if (idx >= OUTPUT_SLOT_START && idx <= OUTPUT_SLOT_END) {
            if (!this.moveItemStackTo(slotStack, PLAYER_INV_SLOT_START, PLAYER_HOTBAR_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        } else if (idx >= PLAYER_INV_SLOT_START) {
            if (!this.moveItemStackTo(slotStack, INPUT_SLOT_START, OUTPUT_SLOT_START, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(slotStack, PLAYER_INV_SLOT_START, PLAYER_HOTBAR_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return stackCopy;
    }

    // MAIN
    protected boolean canMoveIntoInputSlot(ItemStack stack) {
        return true;
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(InitBlocks.ELETRIC_ANVIL);
    }

    protected boolean mayPickup(Player player, boolean hasStack) {
        return (player.hasInfiniteMaterials() || player.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    protected void onTake(Player player, ItemStack stack) {
        if (!player.hasInfiniteMaterials()) {
            player.giveExperienceLevels(-this.getCost());
        }
        this.setCost(0);
        
        for (int c = INPUT_SLOT_START; c < OUTPUT_SLOT_START; c++) {
            this.inputSlots.setItem(c, ItemStack.EMPTY);
        }

        this.access.execute((level, pos) -> level.levelEvent(1030, pos, 0));
    }

    public void createResult() {
        this.createResultInternal();
    }

    protected void createResultInternal() {
        ItemStack addStack = this.inputSlots.getItem(ADDITIONAL_SLOT);
        if (!addStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(addStack)) {
            ItemEnchantments addEnchs = EnchantmentHelper.getEnchantmentsForCrafting(addStack);

            for (int c = INPUT_SLOT_START; c < ADDITIONAL_SLOT; c++) {
                boolean changed = false;
                ItemStack inpStack = this.inputSlots.getItem(c);
                ItemEnchantments inpEnchs = EnchantmentHelper.getEnchantmentsForCrafting(inpStack);
                ItemEnchantments.Mutable inpEnchsMutable = new ItemEnchantments.Mutable(inpEnchs);
                int xpCost = 0;
                int sumRepairCost = inpStack.getOrDefault(DataComponents.REPAIR_COST, 0) + addStack.getOrDefault(DataComponents.REPAIR_COST, 0);

                if (!inpStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(inpStack)) {
                    Iterator iterator = addEnchs.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Object2IntMap.Entry<Holder<Enchantment>> addEntry = (Object2IntMap.Entry) iterator.next();
                        Holder<Enchantment> addKey = (Holder) addEntry.getKey();
                        if (EnchantmentUtils.canEnchantPerSupport(inpStack.getItemHolder(), addKey)) {
                            if (EnchantmentUtils.canEnchantPerCompatibility(inpEnchsMutable, addKey)) {
                                int addLevel = addEntry.getIntValue();
                                int inpLevel = inpEnchsMutable.getLevel(addKey);
                                int finalLevel = inpLevel == addLevel ? addLevel + 1 : Math.max(inpLevel, addLevel);
                                if (finalLevel != inpLevel) {
                                    changed = true;
                                    inpEnchsMutable.set(addKey, finalLevel);
                                    xpCost += finalLevel - 1;
                                }
                            }
                        }
                    }
                }

                if (!changed) {
                    this.resultSlots.setItem(c, ItemStack.EMPTY);
                    this.cost.set(0);
                } else {
                    ItemStack resultStack = inpStack.copy();
                    EnchantmentHelper.setEnchantments(resultStack, inpEnchsMutable.toImmutable());
                    this.resultSlots.setItem(c, resultStack);
                    this.cost.set(xpCost);
                }
            }
        }
    }

    public static int calculateIncreasedRepairCost(int oldCost) {
        return (int) Math.min((long) oldCost * 2L + 1L, 2147483647L);
    }
}
