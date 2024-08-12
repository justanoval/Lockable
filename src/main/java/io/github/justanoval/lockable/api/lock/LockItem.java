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
	/**
	 * Removing the lock from the block entity.
	 * @param world The world the block entity is in.
	 * @param pos The position of the block entity.
	 * @param lockable The block entity the lock is being removed from.
	 * @param inventory The inventory of the player removing the lock.
	 */
	static void remove(
			World world,
			BlockPos pos,
			LockableBlockEntity lockable,
			PlayerInventory inventory
	) {
		ItemStack lockItemStack = lockable.getLock();
		if (inventory.getMainHandStack().isEmpty()) {
			inventory.setStack(inventory.selectedSlot, lockItemStack);
		} else {
			inventory.setStack(inventory.getEmptySlot(), lockItemStack);
		}

		lockable.removeLock();

		if (lockItemStack.getItem() instanceof LockItem lockItem) {
			world.playSound(null, pos, lockItem.getRemoveSound(), SoundCategory.BLOCKS, 0.8f, 0.8F + world.random.nextFloat() * 0.4F);
		}
	}

	/**
	 * Determines how exactly a lock functions. Changing this changes how locks function completely.
	 * A lock has several capabilities, those of which you can see {@link io.github.justanoval.lockable.api.lock.LockItemInteraction here}.
	 * @param stack The ItemStack being interacted with the lock.
	 * @param lockable The block entity the lock is on.
	 * @return The type of interaction that goes through, handled in {@link io.github.justanoval.lockable.mixin.AbstractBlockMixin AbstractBlockMixin}.
	 */
	LockItemInteraction tryUseLock(ItemStack stack, LockableBlockEntity lockable);

	/**
	 * Determines the functionality of how a lock is placed. Changing this will completely change how a lock is placed.
	 * @param world The world the lock is being placed in.
	 * @param pos The position of the block entity the lock is being placed on.
	 * @param stack The ItemStack of the lock.
	 * @param lockable The block entity the lock is being placed on.
	 * @param player The player that is placing the lock.
	 */
	void place(World world, BlockPos pos, ItemStack stack, LockableBlockEntity lockable, PlayerEntity player);

	/**
	 * Determine what happens after the lock is locked.
	 * @param key The key that was used on the lock.
	 * @param lock The ItemStack of the lock.
	 * @param player The playre that used the key on the lock.
	 * @param lockable The block entity being interacted with.
	 */
	void onLock(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable);

	/**
	 * Determine what happens after the lock is unlocked.
	 * @param key The key that was used on the lock.
	 * @param lock The ItemStack of the lock.
	 * @param player The player that used the key on the lock.
	 * @param lockable The block entity being interacted with.
	 */
	void onUnlock(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable);

	/**
	 * Whether the key being used on the lock can be used in the first place.
	 * @param key The key being used on the lock.
	 * @param lock The ItemStack of the lock.
	 * @param player The player using the key on the lock.
	 * @param lockable The block entity being interacted with.
	 * @return Whether the key being used on the lock can be used.
	 */
	boolean canUseKey(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable);

	/**
	 * This points to the exact path in assets/modid of the texture that the lock model will use for this specific lock.
	 * e.g. "lockable:textures/entity/scrap_lock.png"
	 * @return The exact path to the texture the lock will display.
	 */
	Identifier getEntityTexturePath();

	SoundEvent getLockSound();
	SoundEvent getUnlockSound();
	SoundEvent getPlaceSound();
	SoundEvent getRemoveSound();
	SoundEvent getKeyFailSound();
	SoundEvent getLockFailSound();
}
