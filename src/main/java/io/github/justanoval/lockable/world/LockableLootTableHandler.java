package io.github.justanoval.lockable.world;

import io.github.justanoval.lockable.items.LockableItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;

public final class LockableLootTableHandler {
	public static void register() {
		LootTableEvents.MODIFY.register(((key, tableBuilder, source, registries) -> {
			if (key == LootTables.NETHER_BRIDGE_CHEST) {
				tableBuilder.modifyPools(a -> a.with(ItemEntry.builder(LockableItems.KEYCHAIN).weight(3)).build());
			}

			if (key.getValue().getPath().startsWith("archaeology")) {
				tableBuilder.modifyPools(a -> a.with(ItemEntry.builder(LockableItems.BONE_KEY).weight(3).build()));
			}
		}));
	}
}
