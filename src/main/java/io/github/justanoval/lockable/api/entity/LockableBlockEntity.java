package io.github.justanoval.lockable.api.entity;

import net.minecraft.item.ItemStack;

public interface LockableBlockEntity {
	void setLocked(boolean locked);
	boolean isLocked();
	boolean hasLock();

	void setLock(ItemStack itemStack);
	void removeLock();
	ItemStack getLock();
}
