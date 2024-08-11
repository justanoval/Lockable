package io.github.justanoval.lockable.mixin.block;

import io.github.justanoval.lockable.api.Lockable;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock<ChestBlockEntity> implements Waterloggable {
	private static final Direction[] HORIZONTAL_DIRECTIONS = { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

	protected ChestBlockMixin(Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityTypeSupplier) {
		super(settings, blockEntityTypeSupplier);
	}

	@Inject(method = "getPlacementState", at = @At(value = "HEAD"), cancellable = true)
	public void onGetPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
		ChestBlock chestBlock = (ChestBlock) (Object) this;
		World world = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		Direction direction = ctx.getPlayerFacing().getOpposite();
		FluidState fluidState = world.getFluidState(blockPos);

		for (Direction horizontalDirection : HORIZONTAL_DIRECTIONS) {
			BlockPos adjacentPos = blockPos.offset(horizontalDirection);
			BlockState adjacentState = world.getBlockState(adjacentPos);

			if (adjacentState.getBlock() instanceof ChestBlock) {
				BlockEntity adjacentBlockEntity = world.getBlockEntity(adjacentPos);

				Lockable.as(adjacentBlockEntity, lockable -> {
					if (lockable.hasLock()) {
						BlockState blockState = chestBlock.getDefaultState()
								.with(ChestBlock.FACING, direction)
								.with(ChestBlock.CHEST_TYPE, ChestType.SINGLE)
								.with(ChestBlock.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

						cir.setReturnValue(blockState);
						cir.cancel();
					}
				});

				break;
			}
		}
	}
}
