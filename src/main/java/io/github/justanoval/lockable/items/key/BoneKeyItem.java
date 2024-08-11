package io.github.justanoval.lockable.items.key;

import io.github.justanoval.lockable.LockableMod;
import net.minecraft.client.item.TooltipConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class BoneKeyItem extends BreakableKeyItem {
	public BoneKeyItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public Float getBreakChance() {
		return LockableMod.CONFIG.boneKey.chanceToBreak.getRealValue();
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipConfig config) {
		MutableText use = Text.translatable("item.lockable.bone_key.description.use");
		MutableText chance = Text.translatable("item.lockable.bone_key.description.chance", (int) (this.getBreakChance() * 100));

		tooltip.add(use.formatted(Formatting.GRAY));
		tooltip.add(chance.formatted(Formatting.DARK_GRAY));
	}
}
