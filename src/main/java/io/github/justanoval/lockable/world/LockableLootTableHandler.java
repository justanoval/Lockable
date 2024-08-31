package io.github.justanoval.lockable.world;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.items.LockableItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;

public final class LockableLootTableHandler {
	public static Integer KEYCHAIN_WEIGHT = LockableMod.CONFIG.keychain.lootWeight.value();
	public static Integer WEATHERED_LOCK_OCEAN_RUINS_WEIGHT = LockableMod.CONFIG.weatheredLock.oceanRuinsWeight.value();
	public static Integer WEATHERED_LOCK_SHIPWRECK_WEIGHT = LockableMod.CONFIG.weatheredLock.shipwreckWeight.value();
	public static Integer BONE_KEY_WEIGHT = LockableMod.CONFIG.boneKey.lootWeight.value();

	public static void register() {
		LootTableEvents.MODIFY.register(((key, tableBuilder, source, registries) -> {
			if (key == LootTables.NETHER_BRIDGE_CHEST) {
				tableBuilder.modifyPools(a -> a.with(ItemEntry.builder(LockableItems.KEYCHAIN).weight(KEYCHAIN_WEIGHT)).build());
			}

			if (key == LootTables.SHIPWRECK_SUPPLY_CHEST) {
				tableBuilder.modifyPools(a -> a.with(ItemEntry.builder(LockableItems.WEATHERED_LOCK).weight(WEATHERED_LOCK_SHIPWRECK_WEIGHT)).build());
			}

			if (key == LootTables.UNDERWATER_RUIN_BIG_CHEST) {
				tableBuilder.modifyPools(a -> a.with(ItemEntry.builder(LockableItems.WEATHERED_LOCK).weight(WEATHERED_LOCK_OCEAN_RUINS_WEIGHT)).build());
			}

			if (key.getValue().getPath().startsWith("archaeology")) {
				tableBuilder.modifyPools(a -> a.with(ItemEntry.builder(LockableItems.BONE_KEY).weight(BONE_KEY_WEIGHT).build()));
			}
		}));
	}
}
