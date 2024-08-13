package io.github.justanoval.lockable.sounds;

import io.github.justanoval.lockable.LockableMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class LockableSoundEvents {
	private static final Identifier KEY_JIGGLE_ID = LockableMod.id("item.key.jiggle");
	private static final Identifier LOCK_JIGGLE_ID = LockableMod.id("entity.lock.jiggle");
	private static final Identifier SCRAP_LOCK_PLACE_ID = LockableMod.id("entity.scrap_lock.place");
	private static final Identifier SCRAP_LOCK_REMOVE_ID = LockableMod.id("entity.scrap_lock.remove");
	private static final Identifier SCRAP_LOCK_UNLOCK_ID = LockableMod.id("entity.scrap_lock.unlock");
	private static final Identifier SCRAP_LOCK_LOCK_ID = LockableMod.id("entity.scrap_lock.lock");
	private static final Identifier WEATHERED_LOCK_PLACE_ID = LockableMod.id("entity.weathered_lock.place");
	private static final Identifier WEATHERED_LOCK_REMOVE_ID = LockableMod.id("entity.weathered_lock.remove");
	private static final Identifier WEATHERED_LOCK_UNLOCK_ID = LockableMod.id("entity.weathered_lock.unlock");
	private static final Identifier WEATHERED_LOCK_LOCK_ID = LockableMod.id("entity.weathered_lock.lock");

	public static final SoundEvent KEY_JIGGLE = SoundEvent.createVariableRangeEvent(KEY_JIGGLE_ID);
	public static final SoundEvent LOCK_JIGGLE = SoundEvent.createVariableRangeEvent(LOCK_JIGGLE_ID);
	public static final SoundEvent SCRAP_LOCK_PLACE = SoundEvent.createVariableRangeEvent(SCRAP_LOCK_PLACE_ID);
	public static final SoundEvent SCRAP_LOCK_REMOVE = SoundEvent.createVariableRangeEvent(SCRAP_LOCK_REMOVE_ID);
	public static final SoundEvent SCRAP_LOCK_UNLOCK = SoundEvent.createVariableRangeEvent(SCRAP_LOCK_UNLOCK_ID);
	public static final SoundEvent SCRAP_LOCK_LOCK = SoundEvent.createVariableRangeEvent(SCRAP_LOCK_LOCK_ID);
	public static final SoundEvent WEATHERED_LOCK_PLACE = SoundEvent.createVariableRangeEvent(WEATHERED_LOCK_PLACE_ID);
	public static final SoundEvent WEATHERED_LOCK_REMOVE = SoundEvent.createVariableRangeEvent(WEATHERED_LOCK_REMOVE_ID);
	public static final SoundEvent WEATHERED_LOCK_UNLOCK = SoundEvent.createVariableRangeEvent(WEATHERED_LOCK_UNLOCK_ID);
	public static final SoundEvent WEATHERED_LOCK_LOCK = SoundEvent.createVariableRangeEvent(WEATHERED_LOCK_LOCK_ID);

	public static void register() {
		Registry.register(Registries.SOUND_EVENT, KEY_JIGGLE_ID, KEY_JIGGLE);
		Registry.register(Registries.SOUND_EVENT, LOCK_JIGGLE_ID, LOCK_JIGGLE);
		Registry.register(Registries.SOUND_EVENT, SCRAP_LOCK_PLACE_ID, SCRAP_LOCK_PLACE);
		Registry.register(Registries.SOUND_EVENT, SCRAP_LOCK_REMOVE_ID, SCRAP_LOCK_REMOVE);
		Registry.register(Registries.SOUND_EVENT, SCRAP_LOCK_UNLOCK_ID, SCRAP_LOCK_UNLOCK);
		Registry.register(Registries.SOUND_EVENT, SCRAP_LOCK_LOCK_ID, SCRAP_LOCK_LOCK);
		Registry.register(Registries.SOUND_EVENT, WEATHERED_LOCK_PLACE_ID, WEATHERED_LOCK_PLACE);
		Registry.register(Registries.SOUND_EVENT, WEATHERED_LOCK_REMOVE_ID, WEATHERED_LOCK_REMOVE);
		Registry.register(Registries.SOUND_EVENT, WEATHERED_LOCK_UNLOCK_ID, WEATHERED_LOCK_UNLOCK);
		Registry.register(Registries.SOUND_EVENT, WEATHERED_LOCK_LOCK_ID, WEATHERED_LOCK_LOCK);
	}
}
