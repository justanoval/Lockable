package io.github.justanoval.lockable.items.lock;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.api.lock.AbstractLockItem;
import io.github.justanoval.lockable.items.key.BoneKeyItem;
import net.minecraft.client.item.TooltipConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class WeatheredLockItem extends AbstractLockItem {
	public WeatheredLockItem(Settings settings) {
		super(settings);
	}

	@Override
	public Identifier getEntityTexturePath() {
		return LockableMod.id("textures/entity/weathered_lock.png");
	}

	@Override
	public boolean canUseKey(ItemStack key, ItemStack lock, PlayerEntity player, LockableBlockEntity lockable) {
		if (!(key.getItem() instanceof BoneKeyItem)) {
			return false;
		}

		return super.canUseKey(key, lock, player, lockable);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipConfig config) {
		tooltip.add(Text.translatable("item.lockable.weathered_lock.description").formatted(Formatting.GRAY));
	}

	@Override
	public SoundEvent getLockSound() {
		return SoundEvents.BLOCK_COPPER_DOOR_CLOSE;
	}

	@Override
	public SoundEvent getUnlockSound() {
		return SoundEvents.BLOCK_COPPER_DOOR_OPEN;
	}
}
