package io.github.justanoval.lockable.items.key;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.api.key.AbstractKeyItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class MasterKeyItem extends AbstractKeyItem {
	public MasterKeyItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable) {
		return true;
	}
}
