package io.github.justanoval.lockable;

import io.github.justanoval.lockable.config.LockableConfig;
import io.github.justanoval.lockable.items.LockableItems;
import io.github.justanoval.lockable.networking.LockablePackets;
import io.github.justanoval.lockable.sounds.LockableSoundEvents;
import io.github.justanoval.lockable.world.LockableLootTableHandler;
import io.github.justanoval.lockable.world.WanderingTraderLootAdditions;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.config.v2.QuiltConfig;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LockableMod implements ModInitializer {
	public static final String ID = "lockable";
	public static final String NULL_PIN = "null";
	public static final Logger LOGGER = LoggerFactory.getLogger("Lockable");
	public static final LockableConfig CONFIG = QuiltConfig.create(ID, "config", LockableConfig.class);

	@Override
	public void onInitialize(ModContainer mod) {
		LockablePackets.registerServerPackets();
		LockableItems.register();
		LockableLootTableHandler.register();
		LockableSoundEvents.register();
		WanderingTraderLootAdditions.register();
	}

	public static Identifier id(String name) {
		return Identifier.of(ID, name);
	}
}
