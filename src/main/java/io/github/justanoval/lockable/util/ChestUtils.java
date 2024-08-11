package io.github.justanoval.lockable.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public final class ChestUtils {
	public static @Nullable BlockPos getAdjacentChest(BlockState state, BlockPos pos) {
		if (state.contains(ChestBlock.CHEST_TYPE) && state.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE) {
			ChestType chestType = state.get(ChestBlock.CHEST_TYPE);
			Direction facing = state.get(ChestBlock.FACING);
			BlockPos adjacentChestPos = null;

			if (chestType == ChestType.LEFT) {
				adjacentChestPos = pos.offset(facing.rotateYClockwise());
			} else if (chestType == ChestType.RIGHT) {
				adjacentChestPos = pos.offset(facing.rotateYCounterclockwise());
			}

			return adjacentChestPos;
		}

		return null;
	}
}
