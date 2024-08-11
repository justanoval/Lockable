package io.github.justanoval.lockable.api.lock;

import io.github.justanoval.lockable.api.Lockable;
import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.sounds.LockableSoundEvents;
import io.github.justanoval.lockable.util.ChestUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractLockItem extends Item implements LockItem {
	public AbstractLockItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canUseKey(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable) {
		return true;
	}

	@Override
	public void onLock(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable) {

	}

	@Override
	public void onUnlock(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable) {

	}

	@Override
	public void place(
		World world,
		BlockPos pos,
		ItemStack stack,
		LockableBlockEntity lockable,
		PlayerEntity player
	) {
		BlockPos adjacentChest = ChestUtils.getAdjacentChest(world.getBlockState(pos), pos);
		if (adjacentChest != null) {
			BlockEntity adjacentEntity = world.getBlockEntity(adjacentChest);

			if (Lockable.hasLock(adjacentEntity)) {
				return;
			}
		}

		// reset count
		ItemStack lockStack = stack.copy();
		lockStack.setCount(1);

		// put one lock
		lockable.setLock(lockStack);

		// consume item
		if (!player.isCreative()) {
			PlayerInventory inventory = player.getInventory();
			inventory.removeStack(inventory.selectedSlot, 1);
		}

		// play sound
		world.playSound(null, pos, this.getPlaceSound(), SoundCategory.BLOCKS, 0.8f, 0.8F + world.random.nextFloat() * 0.4F);
	}

	@Override
	public LockItemInteraction tryUseLock(ItemStack stack, LockableBlockEntity lockable) {
		if (lockable.hasLock()) {
			return LockItemInteraction.HAS_LOCK;
		} else {
			return LockItemInteraction.PLACE;
		}
	}

	@Override
	public SoundEvent getLockSound() {
		return SoundEvents.BLOCK_IRON_DOOR_CLOSE;
	}

	@Override
	public SoundEvent getUnlockSound() {
		return SoundEvents.BLOCK_IRON_DOOR_OPEN;
	}

	@Override
	public SoundEvent getPlaceSound() {
		return SoundEvents.BLOCK_CHAIN_PLACE;
	}

	@Override
	public SoundEvent getRemoveSound() {
		return SoundEvents.BLOCK_CHAIN_BREAK;
	}

	@Override
	public SoundEvent getKeyFailSound() {
		return LockableSoundEvents.KEY_JIGGLE;
	}

	@Override
	public SoundEvent getLockFailSound() {
		return LockableSoundEvents.LOCK_JIGGLE;
	}
}
