package io.github.justanoval.lockable.client.renderer;

import io.github.justanoval.lockable.api.lock.LockItem;
import io.github.justanoval.lockable.client.LockableEntityModelLayers;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Axis;

public class LockedChestRenderer {
	private final ModelPart shackle;
	private final ModelPart base;

	public LockedChestRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(LockableEntityModelLayers.LOCK);
		this.base = modelPart.getChild("base");
		this.shackle = modelPart.getChild("shackle");
	}

	public void render(LockItem lockItem, BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, boolean isLocked) {
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(lockItem.getEntityTexturePath()));
		BlockState blockState = entity.getCachedState();
		float f = (blockState.get(ChestBlock.FACING)).asRotation();

		matrices.push();

		matrices.translate(0.5F, 0.5F, 0.5F);
		matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-f));
		matrices.translate(-0.5F, -0.5F, -0.5F);

		matrices.rotate(Axis.X_POSITIVE.rotationDegrees(180.0f));
		matrices.translate(0.5f, -1.5f, -0.5f);

		this.render(matrices, vertexConsumer, this.base, this.shackle, isLocked, light, overlay);
		matrices.pop();
	}

	private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart base, ModelPart shackle, boolean isLocked, int light, int overlay) {
		if (isLocked) {
			base.pivotY = 24.0f;
		} else {
			base.pivotY = 24.0f + 1.0f;
		}

		base.render(matrices, vertices, light, overlay);
		shackle.render(matrices, vertices, light, overlay);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("base", ModelPartBuilder.create()
				.uv(0, 0)
				.cuboid(-3.0F, -7.0F, -8.5F, 6.0F, 5.0F, 2.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		modelPartData.addChild("shackle", ModelPartBuilder.create()
				.uv(0, 7)
				.cuboid(-2.0F, -10.0F, -7.5F, 4.0F, 5.0F, 1.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}
}
