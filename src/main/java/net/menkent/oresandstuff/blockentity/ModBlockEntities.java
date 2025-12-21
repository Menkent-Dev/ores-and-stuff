package net.menkent.oresandstuff.blockentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.menkent.oresandstuff.OresNStuff;
import net.menkent.oresandstuff.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<CrucibleBlockEntity> CRUCIBLE_BLOCK_ENTITY = Registry.register(
      BuiltInRegistries.BLOCK_ENTITY_TYPE, 
      ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "crucible_block_entity"),
      FabricBlockEntityTypeBuilder.create(CrucibleBlockEntity::new, ModBlocks.CRUCIBLE).build()
    );

  public static void register() {
    OresNStuff.LOGGER.info("Registering Block Entities");
  }
}
