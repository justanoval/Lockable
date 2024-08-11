package io.github.justanoval.lockable.mixin.client.renderers;

import io.github.justanoval.lockable.api.Lockable;
import io.github.justanoval.lockable.api.lock.LockItem;
import io.github.justanoval.lockable.client.renderer.LockedChestRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ChestBlockEntityRendererMixin<T extends BlockEntity & ChestAnimationProgress> implements BlockEntityRenderer<T> {
	private LockedChestRenderer lockRenderer;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(BlockEntityRendererFactory.Context ctx, CallbackInfo ci) {
		this.lockRenderer = new LockedChestRenderer(ctx);
	}

	@Inject(method = "render", at = @At("TAIL"))
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		Lockable.as(entity, lockable -> {
			// handle double chests
			BlockState state = entity.getCachedState();
			if (state.contains(ChestBlock.CHEST_TYPE) && state.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE) {
				Direction facing = state.get(ChestBlock.FACING);
				ChestType chestType = state.get(ChestBlock.CHEST_TYPE);
				Direction offsetDirection = getOffsetDirection(facing, chestType);

				double offset = -0.5;

				if (offsetDirection != null) {
					switch (offsetDirection) {
						case NORTH:
							matrices.translate(0, 0, -offset);
							break;
						case SOUTH:
							matrices.translate(0, 0, offset);
							break;
						case WEST:
							matrices.translate(-offset, 0, 0);
							break;
						case EAST:
							matrices.translate(offset, 0, 0);
							break;
						default:
							break;
					}
				}
			}

			// render lock
			if (lockable.hasLock() && lockable.getLock().getItem() instanceof LockItem lockItem) {
				this.lockRenderer.render(lockItem, entity, tickDelta, matrices, vertexConsumers, light, overlay, lockable.isLocked());
			}
		});
	}

	private static @Nullable Direction getOffsetDirection(Direction facing, ChestType chestType) {
		return switch (facing) {
			case NORTH -> (chestType == ChestType.LEFT) ? Direction.WEST : Direction.EAST;
			case SOUTH -> (chestType == ChestType.LEFT) ? Direction.EAST : Direction.WEST;
			case WEST -> (chestType == ChestType.LEFT) ? Direction.SOUTH : Direction.NORTH;
			case EAST -> (chestType == ChestType.LEFT) ? Direction.NORTH : Direction.SOUTH;
			default -> null;
		};
	}
}
