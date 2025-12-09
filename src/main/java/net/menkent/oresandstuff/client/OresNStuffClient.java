package net.menkent.oresandstuff.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.menkent.oresandstuff.block.ModBlocks;
import net.menkent.oresandstuff.client.screen.CrucibleScreen;
import net.menkent.oresandstuff.screen.ModScreens;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class OresNStuffClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(ModBlocks.CRUCIBLE_BLOCK, ChunkSectionLayer.CUTOUT);

        MenuScreens.register(ModScreens.CRUCIBLE_SCREEN_HANDLER, CrucibleScreen::new);
    }
    
}