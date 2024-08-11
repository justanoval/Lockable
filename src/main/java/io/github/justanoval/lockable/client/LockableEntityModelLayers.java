package io.github.justanoval.lockable.client;

import io.github.justanoval.lockable.LockableMod;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public final class LockableEntityModelLayers {
    public static EntityModelLayer LOCK = new EntityModelLayer(LockableMod.id("lock"), "main");
    public static EntityModelLayer LOCKED_BLOCK = new EntityModelLayer(LockableMod.id("locked_block"), "main");
}
