package igentuman.galacticresearch.client.gui.MCS;

import igentuman.galacticresearch.GalacticResearch;
import igentuman.galacticresearch.ModConfig;
import igentuman.galacticresearch.common.container.ContainerMissionControlStation;
import igentuman.galacticresearch.common.tile.TileMissionControlStation;
import igentuman.galacticresearch.network.GRPacketSimple;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuiMissionControlStation extends GuiContainerGC {
    private static final ResourceLocation guiTexture = new ResourceLocation(GalacticResearch.MODID, "textures/gui/container/mission_control_station.png");
    private final TileMissionControlStation tile;

    private GuiButtonImage btnUp;
    private GuiButtonImage btnDown;
    private GuiButtonImage btnHelp;
    private GuiButtonImage btnLocator;
    private GuiButtonImage btnMissions;
    private GuiButton activateBtn;
    private GuiButton analyzeDataBtn;

    private GuiElementInfoRegion electricInfoRegion;
    private GuiElementInfoRegion helpRegion;

    public GuiMissionControlStation(InventoryPlayer par1InventoryPlayer, TileMissionControlStation tile) {
        super(new ContainerMissionControlStation(par1InventoryPlayer, tile));
        this.electricInfoRegion = new GuiElementInfoRegion(guiLeft + 30, guiTop + 167, 64, 9, new ArrayList(), this.width, this.height, this);
        this.helpRegion = new GuiElementInfoRegion(guiLeft + 154, guiTop + 164, 12, 12, new ArrayList(), this.width, this.height, this);
        this.tile = tile;
        this.ySize = 201;
    }

    private void tickButtons()
    {
        if(selectedButton != null && selectedButton.isMouseOver()) {
            btnClick(selectedButton);
            selectedButton = null;
        }
        if(tile.rocketState != 1) {
            activateBtn.enabled = false;
        } else {
            activateBtn.enabled = true;
        }
    }

    protected void btnClick(GuiButton btn) {
        switch(btn.id) {
            case 0:
                GalacticResearch.packetPipeline.sendToServer(new GRPacketSimple(GRPacketSimple.EnumSimplePacket.PREV_MISSION_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[]{this.tile.getPos(), 0}));
                break;
            case 1:
                GalacticResearch.packetPipeline.sendToServer(new GRPacketSimple(GRPacketSimple.EnumSimplePacket.NEXT_MISSION_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[]{this.tile.getPos(), 0}));
                break;
            case 3:
                GalacticResearch.packetPipeline.sendToServer(new GRPacketSimple(GRPacketSimple.EnumSimplePacket.ACTIVATE_MISSION_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[]{this.tile.getPos(), 0}));
                break;
            case 4:
                GalacticResearch.packetPipeline.sendToServer(new GRPacketSimple(GRPacketSimple.EnumSimplePacket.ANALYZE_DATA_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[]{this.tile.getPos(), 0}));
                break;
            case 5:
                GalacticResearch.packetPipeline.sendToServer(new GRPacketSimple(GRPacketSimple.EnumSimplePacket.OPEN_GUI_LOCATOR, GCCoreUtil.getDimensionID(this.mc.world), new Object[]{this.tile.getPos(), 0}));
                break;
            default:
        }
    }


    public void initButtons()
    {
        int xpos = guiLeft+7;
        int ypos = guiTop+24;
        int size = 12;
        this.buttonList.add(btnUp = new GuiButtonImage(0, xpos, ypos, size, size, 176, 83, 12, guiTexture));
        this.buttonList.add(btnDown = new GuiButtonImage(1, xpos, ypos+26, size, size, 176, 59, 12, guiTexture));
        this.buttonList.add(btnHelp = new GuiButtonImage(2, xpos+149, guiTop + 164, 13, 14, 176, 109, 0, guiTexture));
        this.buttonList.add(activateBtn = new GuiButton(3, (this.width - this.xSize) / 2 + 7, guiTop + 141, 90, 20,  GCCoreUtil.translate("gui.mission_control_station.activate")));
        this.buttonList.add(analyzeDataBtn = new GuiButton(4, (this.width - this.xSize) / 2 + 7, guiTop + 87, 161, 20,  GCCoreUtil.translate("gui.mission_control_station.analyze")));
        this.buttonList.add(btnLocator = new GuiButtonImage(5, guiLeft+74 , guiTop - 7, 70, 16, 176, 124, 0, guiTexture));
        this.buttonList.add(btnMissions = new GuiButtonImage(6, guiLeft+4 , guiTop - 7, 70, 16, 176, 140, 0, guiTexture));
    }

    private void addHelpRegion()
    {
        List<String> help = new ArrayList();
        help.add(GCCoreUtil.translate("gui.help.mcs.desc.0"));
        help.add(GCCoreUtil.translate("gui.help.mcs.desc.1"));
        help.add(GCCoreUtil.translate("gui.help.mcs.desc.2"));
        if(GalaxyRegistry.getRegisteredSolarSystems().size() > 1) {
            help.add(GCCoreUtil.translate("gui.help.mcs.desc.3"));
            help.add(GCCoreUtil.translate("gui.help.mcs.desc.4"));
        }

        this.helpRegion.tooltipStrings = help;
        this.helpRegion.xPosition = guiLeft + 156;
        this.helpRegion.yPosition = guiTop + 164;
        this.helpRegion.parentWidth = this.width;
        this.helpRegion.parentHeight = this.height;

        this.infoRegions.add(this.helpRegion);
    }

    private void addElectricInfoRegion()
    {
        List<String> electricityDesc = new ArrayList();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + (int)Math.floor((double)this.tile.getEnergyStoredGC()) + " / " + (int)Math.floor((double)this.tile.getMaxEnergyStoredGC()));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 32;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 165;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);

        List<String> batterySlotDesc = new ArrayList();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 165, 18, 18, batterySlotDesc, this.width, this.height, this));

    }

    public void initGui() {
        super.initGui();
        addElectricInfoRegion();
        addHelpRegion();
        initButtons();
    }


    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

        this.fontRenderer.drawString(GCCoreUtil.translate("gui.mission_control_station"), 10, 10, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.mission_control_station.tab.missions"), 10, -3, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.mission_control_station.tab.locator"), 80, -3, 4210752);
        String curMission =tile.currentMission;
        if(curMission.isEmpty()) {
            curMission = "none";
        }
        String status = tile.getMissionStatusKey(tile.currentMission);
        int info =  tile.getMissionInfo(tile.currentMission);
        this.fontRenderer.drawString(I18n.format("gui.mission_control_station.mission"), 22, 25, 4210752);
        String planet = I18n.format("planet."+curMission);
        if(planet.equals("planet."+curMission)) {
            planet = I18n.format("moon."+curMission);
        }
        if(planet.equals("moon."+curMission)) {
            planet = I18n.format("solarsystem."+curMission);
        }
        if(curMission.toUpperCase().contains("ASTEROID-")) {
            planet = curMission;
        }
        this.fontRenderer.drawString(I18n.format(planet), 22, 39, 4210752);
        String st = I18n.format(status);

        if(tile.currentMission.isEmpty()) {
            st = "";
            info = -1;
        }
        if(info < ModConfig.machines.satellite_mission_duration*20 && info > -1) {
            st +=" "+tile.getMissonPercent(tile.currentMission)+"%";
        }
        this.fontRenderer.drawString(I18n.format("gui.mission_control_station.mission_status", st), 22, 53, 4210752);

        if(tile.getTelescope() == null) {
            this.fontRenderer.drawString(I18n.format("gui.mission_control_station.no_telescope"), 7, 120, ColorUtil.to32BitColor(255,255, 0, 0));
        }
        if(tile.rocketState == -1) {
            this.fontRenderer.drawString(I18n.format("gui.mission_control_station.no_rocket"), 7, 130, 4210752);
        } else if(tile.rocketState == 0) {
            this.fontRenderer.drawString(I18n.format("gui.mission_control_station.rocket_not_ready"), 7, 130, ColorUtil.to32BitColor(255,255, 0, 0));
        } else if (tile.rocketState == -2) {
            this.fontRenderer.drawString(I18n.format("gui.mission_control_station.wrong_rocket"), 7, 130, 4210752);
        } else{
            this.fontRenderer.drawString(I18n.format("gui.mission_control_station.rocket_ready"), 7, 130, 4210752);
        }
        tickButtons();
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTexture);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);
        List<String> electricityDesc = new ArrayList();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(tile.getEnergyStoredGC(), tile.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        if (tile.getEnergyStoredGC() > 0.0F) {
            this.drawTexturedModalRect(guiLeft+29, guiTop + 165, 176, 0, 11, 10);
        }

        this.drawTexturedModalRect(guiLeft+42, guiTop + 167, 187, 0, Math.min(tile.getScaledElecticalLevel(54), 54), 7);

        mc.getTextureManager().bindTexture(guiTexture);

    }
}
