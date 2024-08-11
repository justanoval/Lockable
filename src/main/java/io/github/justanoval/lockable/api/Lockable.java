package io.github.justanoval.lockable.api;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class Lockable {
	public static void as(@NotNull AbstractBlock.AbstractBlockState blockState, @NotNull BlockView world, @NotNull BlockPos pos, @NotNull Consumer<LockableBlockEntity> consumer) {
		if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			if (blockEntity instanceof LockableContainerBlockEntity containerBlockEntity) {
				consumer.accept((LockableBlockEntity) containerBlockEntity);
			}
		}
	}

	public static void as(BlockEntity blockEntity, @NotNull Consumer<LockableBlockEntity> consumer) {
		if (blockEntity == null) return;

		if (!isAllowed(blockEntity)) {
			return;
		}

		if (blockEntity instanceof LockableContainerBlockEntity containerBlockEntity) {
			consumer.accept((LockableBlockEntity) containerBlockEntity);
		}
	}

	public static @Nullable LockableBlockEntity get(BlockEntity blockEntity) {
		if (blockEntity instanceof LockableContainerBlockEntity) {
			return (LockableBlockEntity) blockEntity;
		}

		return null;
	}

	public static boolean isLocked(@Nullable BlockEntity entity) {
		if (entity == null) {
			return false;
		}

		LockableBlockEntity lockable = get(entity);

		if (lockable != null) {
			return lockable.isLocked();
		}

		return false;
	}

	public static boolean hasLock(@Nullable BlockEntity entity) {
		if (entity == null) {
			return false;
		}

		LockableBlockEntity lockable = get(entity);

		if (lockable != null) {
			return lockable.hasLock();
		}

		return false;
	}

	public static boolean isAllowed(BlockEntity blockEntity) {
		Identifier blockEntityId = Registries.BLOCK_ENTITY_TYPE.getId(blockEntity.getType());
		boolean isWhitelist = LockableMod.CONFIG.lockability.isWhitelist.getRealValue();

		if (blockEntityId == null) {
			return !isWhitelist;
		}

		for (String blacklistedId : LockableMod.CONFIG.lockability.blacklist.getRealValue()) {
			if (blockEntityId.toString().equals(blacklistedId)) {
				return isWhitelist;
			}
		}

		return !isWhitelist;
	}
}
