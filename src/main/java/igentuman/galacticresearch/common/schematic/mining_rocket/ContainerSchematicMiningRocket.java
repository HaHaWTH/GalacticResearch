package igentuman.galacticresearch.common.schematic.mining_rocket;

import igentuman.galacticresearch.common.CommonProxy;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class ContainerSchematicMiningRocket extends Container
{

    public InventorySchematicMiningRocket craftMatrix = new InventorySchematicMiningRocket(this);
    public IInventory craftResult = new InventoryCraftResult();
    private final World world;

    public ContainerSchematicMiningRocket(InventoryPlayer par1InventoryPlayer, BlockPos pos) {
        int change = 27;
        this.world = par1InventoryPlayer.player.world;
        this.addSlotToContainer(new SlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 69 + change));
        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 1, 48, -9 + change, pos, par1InventoryPlayer.player));
        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 2, 48, 9 + change, pos, par1InventoryPlayer.player));

        int var6;
        for(var6 = 0; var6 < 3; ++var6) {
            this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 3 + var6, 39, -7 + var6 * 18 + 16 + 18 + change, pos, par1InventoryPlayer.player));
        }

        for(var6 = 0; var6 < 3; ++var6) {
            this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 6 + var6, 57, -7 + var6 * 18 + 16 + 18 + change, pos, par1InventoryPlayer.player));
        }

        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 9, 21, 63 + change, pos, par1InventoryPlayer.player));
        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 10, 21, 81 + change, pos, par1InventoryPlayer.player));
        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 11, 48, 81 + change, pos, par1InventoryPlayer.player));
        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 12, 75, 63 + change, pos, par1InventoryPlayer.player));
        this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 13, 75, 81 + change, pos, par1InventoryPlayer.player));

        for(int var8 = 0; var8 < 3; ++var8) {
            this.addSlotToContainer(new SlotSchematicMiningRocket(this.craftMatrix, 14 + var8, 93 + var8 * 26, -15 + change, pos, par1InventoryPlayer.player));
        }

        change = 9;

        for(var6 = 0; var6 < 3; ++var6) {
            for(int var7 = 0; var7 < 9; ++var7) {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 129 + var6 * 18 + change));
            }
        }

        for(var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 187 + change));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if (!this.world.isRemote) {
            for(int var2 = 1; var2 < this.craftMatrix.getSizeInventory(); ++var2) {
                ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);
                if (!var3.isEmpty()) {
                    par1EntityPlayer.entityDropItem(var3, 0.0F);
                }
            }
        }

    }

    @Nonnull
    public static ItemStack findMatchingMiningRocketRecipe(InventorySchematicMiningRocket inventoryRocketBench) {
        Iterator var1 = CommonProxy.getMiningRocketRecipes().iterator();

        INasaWorkbenchRecipe recipe;
        do {
            if (!var1.hasNext()) {
                return ItemStack.EMPTY;
            }

            recipe = (INasaWorkbenchRecipe)var1.next();
        } while(!recipe.matches(inventoryRocketBench));

        return recipe.getRecipeOutput();
    }

    public void onCraftMatrixChanged(IInventory par1IInventory) {
        this.craftResult.setInventorySlotContents(0, findMatchingMiningRocketRecipe(this.craftMatrix));
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1) {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = (Slot)this.inventorySlots.get(par1);
        if (var3 != null && var3.getHasStack()) {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            boolean done = false;
            if (par1 <= 16) {
                if (!this.mergeItemStack(var4, 17, 53, false)) {
                    return ItemStack.EMPTY;
                }

                if (par1 == 0) {
                    var3.onSlotChange(var4, var2);
                }
            } else {
                for(int i = 1; i < 14; ++i) {
                    Slot testSlot = (Slot)this.inventorySlots.get(i);
                    if (!testSlot.getHasStack() && testSlot.isItemValid(var2)) {
                        if (!this.mergeOneItem(var4, i, i + 1, false)) {
                            return ItemStack.EMPTY;
                        }

                        done = true;
                        break;
                    }
                }

                if (!done) {
                    if (var2.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !((Slot)this.inventorySlots.get(14)).getHasStack()) {
                        if (!this.mergeOneItem(var4, 14, 15, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (var2.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !((Slot)this.inventorySlots.get(15)).getHasStack()) {
                        if (!this.mergeOneItem(var4, 15, 16, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (var2.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !((Slot)this.inventorySlots.get(16)).getHasStack()) {
                        if (!this.mergeOneItem(var4, 16, 17, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (par1 >= 17 && par1 < 44) {
                        if (!this.mergeItemStack(var4, 44, 53, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (par1 >= 44 && par1 < 53) {
                        if (!this.mergeItemStack(var4, 17, 44, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.mergeItemStack(var4, 17, 53, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty()) {
                var3.putStack(ItemStack.EMPTY);
            } else {
                var3.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount()) {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
        boolean flag1 = false;
        if (!par1ItemStack.isEmpty()) {
            for(int k = par2; k < par3; ++k) {
                Slot slot = (Slot)this.inventorySlots.get(k);
                ItemStack slotStack = slot.getStack();
                if (slotStack.isEmpty()) {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.putStack(stackOneItem);
                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
