package io.github.justanoval.lockable.client;

import io.github.justanoval.lockable.client.renderer.LockedChestRenderer;
import io.github.justanoval.lockable.client.renderer.LockedBlockRenderer;
import io.github.justanoval.lockable.items.LockableItems;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public final class LockableClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		LockableItems.registerPredicates();

		EntityModelLayerRegistry.registerModelLayer(LockableEntityModelLayers.LOCK, LockedChestRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LockableEntityModelLayers.LOCKED_BLOCK, LockedBlockRenderer::getTexturedModelData);

		LockedBlockRenderer.register();
	}
}
