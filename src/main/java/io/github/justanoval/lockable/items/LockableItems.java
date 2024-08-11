package io.github.justanoval.lockable.items;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.items.key.NormalKeyItem;
import io.github.justanoval.lockable.items.key.KeychainItem;
import io.github.justanoval.lockable.items.key.BoneKeyItem;
import io.github.justanoval.lockable.items.key.MasterKeyItem;
import io.github.justanoval.lockable.items.lock.ScrapLockItem;
import io.github.justanoval.lockable.items.lock.WeatheredLockItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;

public final class LockableItems {
	public static final Item GOLDEN_KEY = new NormalKeyItem(new Item.Settings()
			.maxCount(1)
	);

	public static final Item MASTER_KEY = new MasterKeyItem(new Item.Settings()
			.maxCount(1)
			.rarity(Rarity.EPIC)
			.fireproof()
	);

	public static final Item SCRAP_LOCK = new ScrapLockItem(new Item.Settings()
			.maxCount(1)
			.rarity(Rarity.UNCOMMON)
			.fireproof()
	);

	public static final Item WEATHERED_LOCK = new WeatheredLockItem(new Item.Settings()
			.maxCount(1)
			.rarity(Rarity.RARE)
	);

	public static final Item KEYCHAIN = new KeychainItem(new Item.Settings()
			.maxCount(1)
			.rarity(Rarity.RARE)
			.component(LockableDataComponents.KEYCHAIN_COMPONENT, DefaultedList.of())
	);

	public static final Item BONE_KEY = new BoneKeyItem(new Item.Settings()
			.maxCount(3)
			.rarity(Rarity.RARE)
	);

	public static void register() {
		registerItem("golden_key", GOLDEN_KEY, ItemGroups.TOOLS_AND_UTILITIES);
		registerItem("scrap_lock", SCRAP_LOCK, ItemGroups.TOOLS_AND_UTILITIES);
		registerItem("weathered_lock", WEATHERED_LOCK, ItemGroups.TOOLS_AND_UTILITIES);
		registerItem("master_key", MASTER_KEY, ItemGroups.TOOLS_AND_UTILITIES);
		registerItem("keychain", KEYCHAIN, ItemGroups.TOOLS_AND_UTILITIES);
		registerItem("bone_key", BONE_KEY, ItemGroups.TOOLS_AND_UTILITIES);

		ModelPredicateProviderRegistry.register(KEYCHAIN, LockableMod.id("keys"),
			(stack, world, entity, seed) -> KeychainItem.getKeys(stack)
		);
	}

	public static void registerItem(String id, Item item, RegistryKey<ItemGroup> itemGroup) {
		Registry.register(Registries.ITEM, LockableMod.id(id), item);
		ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.addItem(item));
	}
}
