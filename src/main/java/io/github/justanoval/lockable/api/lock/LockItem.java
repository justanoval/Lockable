package io.github.justanoval.lockable.api.lock;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface LockItem {
	static void remove(
			World world,
			BlockPos pos,
			LockableBlockEntity lockable,
			PlayerInventory inventory
	) {
		// if hotbar is full
		ItemStack lockItemStack = lockable.getLock();
		if (inventory.getMainHandStack().isEmpty()) {
			inventory.setStack(inventory.selectedSlot, lockItemStack);
		} else {
			inventory.setStack(inventory.getEmptySlot(), lockItemStack);
		}

		// remove lock
		lockable.removeLock();

		if (lockItemStack.getItem() instanceof LockItem lockItem) {
			world.playSound(null, pos, lockItem.getRemoveSound(), SoundCategory.BLOCKS, 0.8f, 0.8F + world.random.nextFloat() * 0.4F);
		}
	}

	LockItemInteraction tryUseLock(ItemStack stack, LockableBlockEntity lockable);

	void place(World world,
			   BlockPos pos,
			   ItemStack stack,
			   LockableBlockEntity lockable,
			   PlayerEntity player);

	void onLock(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable);
	void onUnlock(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable);

	boolean canUseKey(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable);

	Identifier getEntityTexturePath();

	SoundEvent getLockSound();
	SoundEvent getUnlockSound();
	SoundEvent getPlaceSound();
	SoundEvent getRemoveSound();
	SoundEvent getKeyFailSound();
	SoundEvent getLockFailSound();
}
