package io.github.justanoval.lockable.items.key;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.api.key.AbstractKeyItem;
import io.github.justanoval.lockable.LockableMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Objects;

public class NormalKeyItem extends AbstractKeyItem {
	public NormalKeyItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable) {
		Text keyText = itemStack.get(DataComponentTypes.CUSTOM_NAME);
		String keyName = keyText == null ? LockableMod.NULL_PIN : keyText.getString();

		Text lockText = lockable.getLock().get(DataComponentTypes.CUSTOM_NAME);
		String lockName = lockText == null ? LockableMod.NULL_PIN : lockText.getString();

		return (Objects.equals(lockName, keyName) || Objects.equals(lockName, LockableMod.NULL_PIN)) && super.canUseKey(itemStack, player, lockable);
	}
}
