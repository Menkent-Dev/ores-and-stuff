package net.menkent.oresandstuff.client;

import net.fabricmc.api.ClientModInitializer;
import net.menkent.oresandstuff.client.screen.CrucibleScreen;
import net.menkent.oresandstuff.screen.ModScreens;
import net.minecraft.client.gui.screens.MenuScreens;

public class OresNStuffClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModScreens.CRUCIBLE_SCREEN_HANDLER, CrucibleScreen::new);
    }
    
}