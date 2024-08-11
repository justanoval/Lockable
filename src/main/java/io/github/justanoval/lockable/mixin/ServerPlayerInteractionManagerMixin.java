package io.github.justanoval.lockable.mixin;

import io.github.justanoval.lockable.api.Lockable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
	@Shadow
	protected ServerWorld world;

	@Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
	public void onTryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity != null && Lockable.isLocked(blockEntity)) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
