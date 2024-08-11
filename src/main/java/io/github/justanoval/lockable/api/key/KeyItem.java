package io.github.justanoval.lockable.api.key;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface KeyItem {
	boolean canUseKey(ItemStack itemStack, PlayerEntity player, LockableBlockEntity lockable);
	KeyItemInteraction tryUseKey(ItemStack stack, PlayerEntity player, LockableBlockEntity lockable);
	Text getIncorrectKeyMessage();
	void unlock(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, LockableBlockEntity lockable);
	void lock(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, LockableBlockEntity lockable);
}
