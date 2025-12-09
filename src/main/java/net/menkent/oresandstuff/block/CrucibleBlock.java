package net.menkent.oresandstuff.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.menkent.oresandstuff.blockentity.CrucibleBlockEntity;
import net.menkent.oresandstuff.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrucibleBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty IS_FILLED = BooleanProperty.create("IS_FILLED");
    
    private static final VoxelShape RIM_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    private static final VoxelShape LEG1 = Block.box(1.0, 0.0, 1.0, 4.0, 6.0, 4.0);
    private static final VoxelShape LEG2 = Block.box(12.0, 0.0, 1.0, 15.0, 6.0, 4.0);
    private static final VoxelShape LEG3 = Block.box(1.0, 0.0, 12.0, 4.0, 6.0, 15.0);
    private static final VoxelShape LEG4 = Block.box(12.0, 0.0, 12.0, 15.0, 6.0, 15.0);
    private static final VoxelShape CAULDRON_SHAPE = Shapes.or(RIM_SHAPE, LEG1, LEG2, LEG3, LEG4);

    public static final MapCodec<CrucibleBlock> CODEC = CrucibleBlock.simpleCodec(CrucibleBlock::new);

    public CrucibleBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition()
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(LIT, false)
            .setValue(IS_FILLED, true)
        );

    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CAULDRON_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CAULDRON_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, 
        Player player, InteractionHand hand, BlockHitResult hit
    ) {
        if (!world.isClientSide) {
            MenuProvider screenHandlerFactory = ((CrucibleBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) {
                player.openMenu(screenHandlerFactory);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if(world.isClientSide()) {
            return null;
        }

        return createTickerHelper(type, ModBlockEntities.CRUCIBLE_BLOCK_ENTITY, (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, IS_FILLED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
            .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
            .setValue(LIT, false)
            .setValue(IS_FILLED, false);
    }

    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrucibleBlockEntity crucible) {
                boolean isFilled = crucible.isFilled();
                if (state.getValue(IS_FILLED) != isFilled) {
                    world.setBlockAndUpdate(pos, state.setValue(IS_FILLED, isFilled));
                }
            }
        }
    }

    @Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}
}
