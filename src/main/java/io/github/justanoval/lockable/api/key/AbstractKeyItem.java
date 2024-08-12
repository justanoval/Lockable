package io.github.justanoval.lockable.api.key;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.api.lock.LockItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractKeyItem extends Item implements KeyItem {
	public AbstractKeyItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable) {
		return !(lockable.hasLock() && lockable.getLock().getItem() instanceof LockItem lockItem && !lockItem.canUseKey(itemStack, lockable.getLock(), player, lockable));
	}

	@Override
	public KeyItemInteraction tryUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable) {
		if (lockable.hasLock() && itemStack.getItem() instanceof KeyItem && this.canUseKey(itemStack, player, lockable)) {
			if (lockable.isLocked()) {
				return KeyItemInteraction.UNLOCK;
			} else {
				return KeyItemInteraction.LOCK;
			}
		} else if (lockable.hasLock() && itemStack.getItem() instanceof KeyItem) {
			return KeyItemInteraction.FAIL;
		}

		return KeyItemInteraction.PASS;
	}

	@Override
	public Text getIncorrectKeyMessage() {
		return Text.translatable("item.lockable.key.fail");
	}

	@Override
	public void lock(
			ItemStack itemStack,
			PlayerEntity player,
			World world,
			BlockPos pos,
			LockableBlockEntity lockable
	) {
		lockable.setLocked(true);
	}

	@Override
	public void unlock(
			ItemStack itemStack,
			PlayerEntity player,
			World world,
			BlockPos pos,
			LockableBlockEntity lockable
	) {
		lockable.setLocked(false);
	}
}
