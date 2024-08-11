package io.github.justanoval.lockable.client.renderer;

import io.github.justanoval.lockable.api.Lockable;
import io.github.justanoval.lockable.api.lock.LockItem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public final class LockedBlockRenderer {
	private static final List<BlockPos> LOCKABLE_BLOCK_ENTITIES = new ArrayList<>();

	public static void register() {
		ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> {
			BlockPos pos = blockEntity.getPos();
			if (!(blockEntity instanceof ChestBlockEntity) && blockEntity instanceof LockableContainerBlockEntity) {
				Lockable.as(blockEntity, lockable -> {
					if (!LOCKABLE_BLOCK_ENTITIES.contains(pos)) {
						LOCKABLE_BLOCK_ENTITIES.add(pos);
					}
				});
			}
		});

		ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
			BlockPos pos = blockEntity.getPos();
			LOCKABLE_BLOCK_ENTITIES.remove(pos);
		});

		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
			MatrixStack matrices = context.matrixStack();
			VertexConsumerProvider consumers = context.consumers();
			Camera camera = context.camera();
			ClientWorld world = context.world();

			if (matrices != null && consumers != null) {
				for (BlockPos pos : LOCKABLE_BLOCK_ENTITIES) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					Lockable.as(blockEntity, lockable -> {
						if (lockable.hasLock()) {
							Item item = lockable.getLock().getItem();
							if (item instanceof LockItem lockItem) {
								renderLockedBlock(lockItem, matrices, consumers, camera, world, pos, lockable.isLocked());
							}
						}
					});
				}
			}
		});
	}

	public static void renderLockedBlock(
			LockItem item,
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			Camera camera,
			ClientWorld world,
			BlockPos pos,
			boolean isLocked
	) {
		Vec3d cameraPos = camera.getPos();
		BlockState state = world.getBlockState(pos);

		Direction direction = Direction.EAST;

		if (state.contains(FacingBlock.FACING)) {
			direction = state.get(FacingBlock.FACING);
		}

		if (state.contains(HorizontalFacingBlock.FACING)) {
			direction = state.get(HorizontalFacingBlock.FACING);
		}

		matrices.push();

		matrices.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y - 1.5, pos.getZ() - cameraPos.z);

		LockedBlockRenderer.render(
				direction,
				matrices,
				vertexConsumers,
				WorldRenderer.getLightmapCoordinates(world, camera.getBlockPos()),
				OverlayTexture.DEFAULT_UV,
				isLocked,
				item.getEntityTexturePath()
		);

		matrices.pop();
	}

	public static void render(
			Direction direction,
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			int light,
			int overlay,
			boolean isLocked,
			Identifier texture
	) {
		VertexConsumer chainsConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
		float f = direction.asRotation();

		// set facing
		matrices.translate(0.5F, 0.5F, 0.5F);
		matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-f));
		matrices.translate(-0.5F, -0.5F, -0.5F);

		// fix weird rendering bug? just renders in the wrong position
		matrices.rotate(Axis.X_POSITIVE.rotationDegrees(180.0f));
		matrices.translate(0.5f, -1.5f, -0.5f);

		// create model part
		ModelPart modelPart = getTexturedModelData().createModel();
		ModelPart base = modelPart.getChild("base");
		ModelPart shackle = modelPart.getChild("shackle");
		ModelPart chainR1 = modelPart.getChild("chain_r1");
		ModelPart chainR2 = modelPart.getChild("chain_r2");

		matrices.translate(0.02f, 0, 0);

		// render chains
		chainR1.render(matrices, chainsConsumer, light, overlay);
		chainR2.render(matrices, chainsConsumer, light, overlay);

		// render lock
		matrices.translate(-0.02f, -1.35f, 0);

		if (isLocked) {
			base.translate(new Vector3f(0.0f, 0.0f, 0.0f));
			base.rotate(new Vector3f(0.0f, 0.0f, 0.0f));
		} else {
			base.translate(new Vector3f(3.0f, 0.0f, -17f));
			base.rotate(new Vector3f(0.0f, MathHelper.PI, 0.0f));
		}

		base.render(matrices, chainsConsumer, light, overlay);
		shackle.render(matrices, chainsConsumer, light, overlay);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("base", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-3.0F, -11.0F, -9.5F, 6.0F, 5.0F, 2.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("shackle", ModelPartBuilder.create()
						.uv(0, 7)
						.cuboid(-2.0F, -14.0F, -8.5F, 4.0F, 5.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("chain_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -9.0F, -9.0F, 3.0F, 18.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -8.5F, 0.0F, 0.0F, 0.0F, 1.8326F));
		modelPartData.addChild("chain_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -9.0F, -9.0F, 3.0F, 18.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -9.5F, 0.25F, 0.0F, 0.0F, 1.3526F));

		return TexturedModelData.of(modelData, 64, 64);
	}
}
