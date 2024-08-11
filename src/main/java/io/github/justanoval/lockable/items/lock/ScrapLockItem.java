package io.github.justanoval.lockable.items.lock;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.api.lock.AbstractLockItem;
import net.minecraft.util.Identifier;

public class ScrapLockItem extends AbstractLockItem {
	public ScrapLockItem(Settings settings) {
		super(settings);
	}

	@Override
	public Identifier getEntityTexturePath() {
		return LockableMod.id("textures/entity/scrap_lock.png");
	}
}
