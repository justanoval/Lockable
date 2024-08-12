package io.github.justanoval.lockable.api.key;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface KeyItem {
	/**
	 * Whether the key can be used on the given block entity.
	 * @param itemStack The key being used.
	 * @param player The player using the key.
	 * @param lockable The block entity being interacted with.
	 * @return Whether they key can be used.
	 */
	boolean canUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable);

	/**
	 * Determines how exactly a key functions. Changing this changes how keys function completely.
	 * A key has several capabilities, those of which you can see {@link io.github.justanoval.lockable.api.key.KeyItemInteraction here}.
	 * @param itemStack The key being used.
	 * @param player The player using the key.
	 * @param lockable The block entity being interacted with.
	 * @return The type of interaction that goes through, handled in {@link io.github.justanoval.lockable.mixin.AbstractBlockMixin AbstractBlockMixin}.
	 */
	KeyItemInteraction tryUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable);

	/**
	 *
	 * @return The message to send when this key is for the wrong lock.
	 */
	Text getIncorrectKeyMessage();

	/**
	 * The definition of how the key unlocks the lock.
	 * @param itemStack The key being used.
	 * @param player The player using the key.
	 * @param world The world the player is in.
	 * @param pos The position of the target block entity.
	 * @param lockable The block entity being interacted with.
	 */
	void unlock(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, LockableBlockEntity lockable);

	/**
	 * The definition of how the key locks the lock.
	 * @param itemStack The key being used.
	 * @param player The player using the key.
	 * @param world The world the player is in.
	 * @param pos The position of the target block entity.
	 * @param lockable The block entity being interacted with.
	 */
	void lock(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, LockableBlockEntity lockable);
}
