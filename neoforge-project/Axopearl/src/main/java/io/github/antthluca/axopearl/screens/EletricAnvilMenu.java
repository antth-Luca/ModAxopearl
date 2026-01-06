package io.github.antthluca.axopearl.screens;

import io.github.antthluca.axopearl.init.InitBlocks;
import io.github.antthluca.axopearl.init.InitMenuTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;

public class EletricAnvilMenu extends ItemCombinerMenu {
    public static final int INPUT_SLOT_START = 0;
    public static final int INPUT_SLOT_END = 3;
    public static final int ADDITIONAL_SLOT = 4;
    public static final int OUTPUT_SLOT_START = 5;
    public static final int OUTPUT_SLOT_END = 8;
    public int repairItemCountCost;
    private final DataSlot cost;

    // CONSTRUCTORS
    public EletricAnvilMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public EletricAnvilMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
        super(InitMenuTypes.ELETRIC_ANVIL_MENU.get(), containerId, playerInv, access, createInputSlotDefinitions());
        this.cost = DataSlot.standalone();
        this.addDataSlot(this.cost);
    }

    private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
            // Input
            .withSlot(0, 18, 8, (stack) -> true)
            .withSlot(1, 8, 30, (stack) -> true)
            .withSlot(2, 8, 54, (stack) -> true)
            .withSlot(3, 18, 76, (stack) -> true)
            // Additional
            .withSlot(4, 62, 42, (stack) -> true)
            // Output
            .withResultSlot(5, 106, 8)
            .withResultSlot(6, 116, 30)
            .withResultSlot(7, 116, 54)
            .withResultSlot(8, 106, 76)
            .build();
    }

    // GETTERS AND SETTERS
    public int getCost() {
        return this.cost.get();
    }

    public void setCost(int value) {
        this.cost.set(Math.max(0, value));
    }

    // SUPER
    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(InitBlocks.ELETRIC_ANVIL);
    }

    @Override
    protected boolean mayPickup(Player player, boolean hasStack) {
        return (player.hasInfiniteMaterials() || player.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        return;
    }

    @Override
    public void createResult() {
        this.createResultInternal();
    }

    // MAIN
    protected void createResultInternal() {
        int changed = 0;
        int xpCost = 0;
        int sumRepairCost = 0;
        for (int iSlot = INPUT_SLOT_START; iSlot < INPUT_SLOT_END + 1; iSlot++) {
            ItemStack inputStack = this.inputSlots.getItem(iSlot);
            ItemStack inputCopy = inputStack.copy();

            if (!inputStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(inputStack)) {
                ItemStack addStack = this.inputSlots.getItem(ADDITIONAL_SLOT);
                ItemEnchantments.Mutable inputEnchs = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(inputCopy));
                sumRepairCost += (long) (Integer) inputStack.getOrDefault(DataComponents.REPAIR_COST, 0) + (long) (Integer) addStack.getOrDefault(DataComponents.REPAIR_COST, 0);

                if (!addStack.isEmpty()) {
                    DataComponentType addComponentType = EnchantmentHelper.getComponentType(addStack);
                    boolean addHasEnchs = addStack.has(addComponentType);

                    if (inputCopy.isDamageableItem() && inputStack.isValidRepairItem(addStack)) {
                        int d = Math.min(inputCopy.getDamageValue(), inputCopy.getMaxDamage() / 4);
                        if (d <= 0) {
                            this.resultSlots.setItem(iSlot + 5, ItemStack.EMPTY);
                            this.cost.set(0);
                            return;
                        }

                        int countMat;
                        for (countMat = 0; d > 0 && countMat < addStack.getCount(); ++countMat) {
                            int newDamage = inputCopy.getDamageValue() - d;
                            inputCopy.setDamageValue(newDamage);
                            ++xpCost;
                            d = Math.min(inputCopy.getDamageValue(), inputCopy.getMaxDamage() / 4);
                        }

                        this.repairItemCountCost = countMat;
                    } else {
                        if (!addHasEnchs && (!inputCopy.is(addStack.getItem()) || !inputCopy.isDamageableItem())) {
                            this.resultSlots.setItem(iSlot + 5, ItemStack.EMPTY);
                            this.cost.set(0);
                            return;
                        }

                        if (inputCopy.isDamageableItem() && !addHasEnchs) {
                            int inpBaseDurab = inputStack.getMaxDamage() - inputStack.getDamageValue();
                            int addBaseDurab = addStack.getMaxDamage() - addStack.getDamageValue();
                            int repairDurab = inpBaseDurab + addBaseDurab
                                + inputCopy.getMaxDamage() * 12 / 100;
                            int repairDam = Math.max(0, inputCopy.getMaxDamage() - repairDurab);
                            
                            if (repairDam < inputCopy.getDamageValue()) {
                                inputCopy.setDamageValue(repairDam);
                                xpCost += 2;
                            }
                        }

                        ItemEnchantments addEnchs = EnchantmentHelper.getEnchantmentsForCrafting(addStack);
                        boolean hasValidEnch = false;
                        boolean hasIncompEnch = false;

                        for (Object2IntMap.Entry<Holder<Enchantment>> addEntry : addEnchs.entrySet()) {
                            Holder<Enchantment> addHolder = (Holder) addEntry.getKey();
                            int inputLevel = inputEnchs.getLevel(addHolder);
                            int addLevel = addEntry.getIntValue();
                            addLevel = inputLevel == addLevel ? addLevel + 1 : Math.max(addLevel, inputLevel);
                            Enchantment ench = (Enchantment) addHolder.value();
                            boolean inputSupport = inputStack.supportsEnchantment(addHolder);
                            if (this.player.getAbilities().instabuild) {
                                inputSupport = true;
                            }

                            for (Holder<Enchantment> inputHolder : inputEnchs.keySet()) {
                                if (!inputHolder.equals(addHolder) && !Enchantment.areCompatible(addHolder, inputHolder)) {
                                    inputSupport = false;
                                    ++xpCost;
                                }
                            }

                            if (!inputSupport) {
                                hasIncompEnch = true;
                            } else {
                                hasValidEnch = true;
                                if (addLevel > ench.getMaxLevel()) {
                                    addLevel = ench.getMaxLevel();
                                }

                                inputEnchs.set(addHolder, addLevel);
                                int enchAnvilCost = ench.getAnvilCost();
                                if (addHasEnchs) {
                                    enchAnvilCost = Math.max(1, enchAnvilCost / 2);
                                }

                                xpCost += enchAnvilCost * addLevel;
                                if (inputStack.getCount() > 1) {
                                    xpCost = 40;
                                }
                            }
                        }

                        if (hasIncompEnch && !hasValidEnch) {
                            this.resultSlots.setItem(iSlot + 5, ItemStack.EMPTY);
                            this.cost.set(0);
                            return;
                        }
                    }
                }

                int clampXpCost = xpCost <= 0 ? 0 : (int) Math.clamp(sumRepairCost + (long) xpCost, 0L, 2147483647L);
                this.cost.set(clampXpCost);
                if (xpCost <= 0) {
                    inputCopy = ItemStack.EMPTY;
                }

                if (!inputCopy.isEmpty()) {
                    int inputRepairCost = (Integer) inputCopy.getOrDefault(DataComponents.REPAIR_COST, 0);
                    if (inputRepairCost < (Integer) addStack.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                        inputRepairCost = (Integer) addStack.getOrDefault(DataComponents.REPAIR_COST, 0);
                    }

                    inputCopy.set(DataComponents.REPAIR_COST, inputRepairCost);
                    EnchantmentHelper.setEnchantments(inputCopy, inputEnchs.toImmutable());
                }

                this.resultSlots.setItem(iSlot + 5, inputCopy);
                this.broadcastChanges();
            } else {
                this.resultSlots.setItem(iSlot + 5, ItemStack.EMPTY);
                this.cost.set(0);
            }
        }
    }

    public static int calculateIncreasedRepairCost(int oldCost) {
        return (int) Math.min((long) oldCost * 2L + 1L, 2147483647L);
    }
}
