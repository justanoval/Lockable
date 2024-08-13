package io.github.justanoval.lockable.api;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class Lockable {
	/**
	 * Get the BlockEntity as a LockableBlockEntity, then go through a consumer.
	 * This is just an extra step to not have to check whether it's null.
	 *
	 * @param blockEntity The BlockEntity to use as a LockableBlockEntity
	 * @param consumer This only runs if the BlockEntity isn't null, isn't blacklisted, and if it
	 *                 extends the expected classes.
	 */
	public static void as(BlockEntity blockEntity, @NotNull Consumer<LockableBlockEntity> consumer) {
		if (blockEntity == null) return;

		if (!isAllowed(blockEntity)) {
			return;
		}

		if (blockEntity instanceof LockableContainerBlockEntity || blockEntity instanceof LockableBlockEntity) {
			consumer.accept((LockableBlockEntity) blockEntity);
		}
	}

	/**
	 * Get the BlockEntity as a LockableBlockEntity.
	 * @param blockEntity The target block entity.
	 * @return The LockableBlockEntity instance of a BlockEntity, if possible. May return null if
	 * 		   the BlockEntity is null, if it is blacklisted, or if it doesn't extend the expected
	 * 		   classes.
	 */
	public static @Nullable LockableBlockEntity get(BlockEntity blockEntity) {
		if (blockEntity == null) return null;

		if (!isAllowed(blockEntity)) {
			return null;
		}

		if (blockEntity instanceof LockableContainerBlockEntity || blockEntity instanceof LockableBlockEntity) {
			return (LockableBlockEntity) blockEntity;
		}

		return null;
	}

	/**
	 * This will return whether the block entity is locked or not.
	 * If the block entity is null, it will return false.
	 * Makes it a bit easier to handle cases where the world can't find the block entity.
	 * If the block entity does not have a lock, it will return false.
	 * @param entity The block entity to check whether it is locked or not.
	 * @return Whether the block entity is locked or not.
	 */
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

	/**
	 * This will return whether the block entity has a lock or not. If the block entity is null, it will return false.
	 * Makes it a bit easier to handle cases where the world can't find the block entity.
	 * @param entity The block entity to check whether it has a lock.
	 * @return Whether the block entity has a lock or not.
	 */
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

	/**
	 * This will return whether the block entity is in the blacklist/whitelist of the config file.
	 * @param blockEntity The block entity to check if it's allowed or not.
	 * @return Whether the block entity is allowed to be locked, or be interacted with a lock.
	 */
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
