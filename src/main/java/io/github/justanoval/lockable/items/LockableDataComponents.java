package io.github.justanoval.lockable.items;

import io.github.justanoval.lockable.LockableMod;
import com.mojang.serialization.Codec;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;

public final class LockableDataComponents {
	public static final DataComponentType<List<ItemStack>> KEYCHAIN_COMPONENT = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			LockableMod.id("keychain_contents"),
			DataComponentType.<List<ItemStack>>builder()
					.codec(Codec.list(ItemStack.CODEC))
					.build()
	);
}
