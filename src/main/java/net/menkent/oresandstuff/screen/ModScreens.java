package net.menkent.oresandstuff.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class ModScreens {
    public static MenuType<CrucibleScreenHandler> CRUCIBLE_SCREEN_HANDLER = Registry.register(
        BuiltInRegistries.MENU,
        ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "crucible_screen_handler"),
        new ExtendedScreenHandlerType<>(CrucibleScreenHandler::new, BlockPos.STREAM_CODEC)

    );
    
    public static void register() {
        OresNStuff.LOGGER.info("Registering Screen Handlers");
    }
}
