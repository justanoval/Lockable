package io.github.justanoval.lockable.api.block;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface LockableBlock {
	boolean tryInteractLock(
			ItemStack stack,
			World world,
			BlockPos pos,
			PlayerEntity player,
			LockableBlockEntity lockable
	);
}
