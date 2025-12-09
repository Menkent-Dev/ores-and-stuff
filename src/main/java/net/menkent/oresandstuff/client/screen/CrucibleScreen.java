package net.menkent.oresandstuff.client.screen;

import net.fabricmc.api.Environment;

import net.fabricmc.api.EnvType;
import net.menkent.oresandstuff.OresNStuff;
import net.menkent.oresandstuff.screen.CrucibleScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class CrucibleScreen extends AbstractContainerScreen<CrucibleScreenHandler> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "textures/gui/crucible_block/crucible_block_gui.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "textures/gui/common/arrow_progress.png");
    private static final ResourceLocation FUEL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "textures/gui/common/fuel_lava_progress.png");

    private static final int PROGRESS_ARROW_WIDTH = 24;
    private static final int PROGRESS_ARROW_HEIGHT = 16;

    private static final int FUEL_LAVA_WIDTH = 16;
    private static final int FUEL_LAVA_HEIGHT = 32;

    public CrucibleScreen(CrucibleScreenHandler handler, Inventory inventory,Component title) {
        super(handler, inventory, title);
    }
    
    @Override
	public void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        
        /**
         * coming from yarn:
         * 
         * 	drawTexture(
         *      RenderPipeline pipeline, 
         *      Identifier sprite, 
         *      int x, 
         *      int y, 
         *      float u, 
         *      float v, 
         *      int width, 
         *      int height,
         *      int textureWidth, 
         *      int textureHeight
         *  )
         */

        if (menu.isCrafting()) {
            context.blit(
                RenderPipelines.GUI_TEXTURED, 
                ARROW_TEXTURE, 
                x + 101, 
                y + 35, 
                0, 
                0, 
                menu.getScaledArrowProgress(), 
                PROGRESS_ARROW_HEIGHT, 
                PROGRESS_ARROW_WIDTH, 
                PROGRESS_ARROW_HEIGHT
            );
        }

        if (menu.hasFuel()) {
            context.blit(
                RenderPipelines.GUI_TEXTURED, 
                FUEL_TEXTURE, 
                x + 8, 
                y + 18 + FUEL_LAVA_HEIGHT - menu.getScaledFuelProgress(), 
                0, 
                FUEL_LAVA_WIDTH - menu.getScaledFuelProgress(), 
                FUEL_LAVA_WIDTH, 
                menu.getScaledFuelProgress(), 
                FUEL_LAVA_WIDTH, 
                FUEL_LAVA_HEIGHT
            );
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
