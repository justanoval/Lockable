package io.github.justanoval.lockable.items.lock;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.api.lock.AbstractLockItem;
import io.github.justanoval.lockable.sounds.LockableSoundEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ScrapLockItem extends AbstractLockItem {
	public ScrapLockItem(Settings settings) {
		super(settings);
	}

	@Override
	public Identifier getEntityTexturePath() {
		return LockableMod.id("textures/entity/scrap_lock.png");
	}

	@Override
	public SoundEvent getLockSound() {
		return LockableSoundEvents.SCRAP_LOCK_LOCK;
	}

	@Override
	public SoundEvent getUnlockSound() {
		return LockableSoundEvents.SCRAP_LOCK_UNLOCK;
	}

	@Override
	public SoundEvent getPlaceSound() {
		return LockableSoundEvents.SCRAP_LOCK_PLACE;
	}

	@Override
	public SoundEvent getRemoveSound() {
		return LockableSoundEvents.SCRAP_LOCK_REMOVE;
	}
}
