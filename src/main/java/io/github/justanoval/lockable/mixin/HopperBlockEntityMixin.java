package io.github.justanoval.lockable.mixin;

import io.github.justanoval.lockable.api.Lockable;
import io.github.justanoval.lockable.mixin.access.HopperBlockEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Inject(method = "extract", at = @At("HEAD"), cancellable = true)
	private static void extract(World world, Hopper hopper, CallbackInfoReturnable<Boolean> cir) {
		BlockPos pos = BlockPos.create(hopper.getHopperX(), hopper.getHopperY() + 1.0D, hopper.getHopperZ());
		BlockEntity blockEntity = world.getBlockEntity(pos);

		Lockable.as(blockEntity, lockable -> {
			if (lockable.isLocked()) {
				cir.setReturnValue(false);
			}
		});
	}

	@Inject(method = "insert", at = @At("HEAD"), cancellable = true)
	private static void insert(World world, BlockPos pos, HopperBlockEntity hopperBlockEntity, CallbackInfoReturnable<Boolean> cir) {
		Direction direction = ((HopperBlockEntityAccessor) hopperBlockEntity).lockable$getDirection();
		BlockEntity blockEntity = world.getBlockEntity(pos.offset(direction));

		if (blockEntity != null) {
			Lockable.as(blockEntity, lockable -> {
				if (lockable.isLocked()) {
					cir.setReturnValue(false);
				}
			});
		}
	}
}
