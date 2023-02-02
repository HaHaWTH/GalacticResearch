package igentuman.galacticresearch.common;

import igentuman.galacticresearch.GalacticResearch;
import igentuman.galacticresearch.ISidedProxy;
import igentuman.galacticresearch.RegistryHandler;
import igentuman.galacticresearch.common.capability.SpaceCapabilityHandler;
import igentuman.galacticresearch.common.schematic.SchematicSatelliteRocket;
import igentuman.galacticresearch.network.TileProcessUpdatePacket;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommonProxy implements ISidedProxy {

    private static List<INasaWorkbenchRecipe> satelliteRocketRecipes = new ArrayList();
    public static List<INasaWorkbenchRecipe> getSatelliteRocketRecipes()
    {
        return satelliteRocketRecipes;
    }

    public static void addSatelliteRocketRecipe()
    {
        HashMap<Integer, ItemStack> input = new HashMap<>();
        input.put(1, new ItemStack(GCItems.partNoseCone));
        input.put(2, new ItemStack(GCBlocks.radioTelescope, 1, 0));
        input.put(3, new ItemStack(GCItems.heavyPlatingTier1, 1, 0));
        input.put(4, new ItemStack(GCItems.heavyPlatingTier1, 1, 0));
        input.put(5, new ItemStack(GCItems.heavyPlatingTier1, 1, 0));
        input.put(6, new ItemStack(GCItems.heavyPlatingTier1, 1, 0));
        input.put(7, new ItemStack(GCItems.partFins));
        input.put(8, new ItemStack(GCItems.rocketEngine));
        input.put(9, new ItemStack(GCItems.partFins));
        HashMap<Integer, ItemStack> input2;

        input2 = new HashMap<>(input);
        satelliteRocketRecipes.add(new NasaWorkbenchRecipe(new ItemStack(RegistryHandler.SATELLITE_ROCKET, 1, 0), input2));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {   addSatelliteRocketRecipe();
        SchematicRegistry.registerSchematicRecipe(new SchematicSatelliteRocket());
        RegistryHandler.registerEntities();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        SpaceCapabilityHandler.register();
    }

    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        GalacticResearch.instance.logger.error("Got PacketUpdateItemStack on wrong side!");
    }
}