package io.github.justanoval.lockable.mixin.client;

import io.github.justanoval.lockable.api.Lockable;
import io.github.justanoval.lockable.util.ChestUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	@Shadow
	private MinecraftClient client;

	@Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
	public void onUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (this.client.world != null) {
			BlockEntity blockEntity = this.client.world.getBlockEntity(pos);
			BlockState state = this.client.world.getBlockState(pos);
			BlockPos adjacentChestPos = ChestUtils.getAdjacentChest(state, pos);

			if (adjacentChestPos != null) {
				BlockEntity adjacentChestEntity = this.client.world.getBlockEntity(adjacentChestPos);

				Lockable.as(adjacentChestEntity, adjacentLockableEntity -> {
					if (adjacentLockableEntity.isLocked()) {
						cir.setReturnValue(false);
						cir.cancel();
					}
				});
			}

			if (Lockable.isLocked(blockEntity)) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}
	}
}
