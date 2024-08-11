package io.github.justanoval.lockable.items.key;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.api.key.AbstractKeyItem;
import io.github.justanoval.lockable.items.LockableDataComponents;
import net.minecraft.client.item.TooltipConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class KeychainItem extends AbstractKeyItem {
	private static final int MAX_KEYS = LockableMod.CONFIG.keychain.maxKeys.getRealValue();

	public KeychainItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean onClickedOnOther(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT) {
			ItemStack otherStack = otherSlot.getStack();
			List<ItemStack> stacks = thisStack.get(LockableDataComponents.KEYCHAIN_COMPONENT);

			if (otherSlot.hasStack() && canAddItem(thisStack, otherStack)) {
				addItem(thisStack, otherStack);
				playInsertSound(player);
				return true;
			} else if (otherStack.isEmpty() && stacks != null && !stacks.isEmpty()) {
				ItemStack lastKey = stacks.get(stacks.size() - 1);
				otherSlot.setStack(lastKey.copy());
				removeLast(thisStack);
				playRemoveOneSound(player);
				return false;
			}
		}

		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipConfig config) {
		List<ItemStack> stacks = stack.get(LockableDataComponents.KEYCHAIN_COMPONENT);

		if (stacks != null) {
			tooltip.add(Text.translatable("item.lockable.keychain.count", stacks.size(), MAX_KEYS).formatted(Formatting.GRAY));

			for (ItemStack itemStack : stacks) {
				Text itemText = itemStack.getName();

				tooltip.add(Text.of("- ").copy()
					.formatted(Formatting.GRAY)
					.append(itemText)
					.formatted(Formatting.ITALIC)
				);
			}
		}
	}

	@Override
	public boolean onClicked(
			ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference
	) {
		List<ItemStack> stacks = thisStack.get(LockableDataComponents.KEYCHAIN_COMPONENT);

		if (clickType == ClickType.RIGHT && thisSlot.canTakePartial(player) && player.getWorld().isClient) {
			if (stacks != null && otherStack.isEmpty() && !stacks.isEmpty()) {
				ItemStack lastKey = stacks.get(stacks.size() - 1);
				cursorStackReference.set(lastKey.copy());
				removeLast(thisStack);
				playRemoveOneSound(player);
				return true;
			} else if (canAddItem(thisStack, otherStack)) {
				addItem(thisStack, otherStack);
				playInsertSound(player);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canUseKey(ItemStack stack, PlayerEntity player, LockableBlockEntity lockable) {
		List<ItemStack> stacks = stack.get(LockableDataComponents.KEYCHAIN_COMPONENT);

		if (stacks == null) {
			return false;
		}

		for (ItemStack keyStack : stacks) {
			if (keyStack.getItem() instanceof NormalKeyItem keyItem && keyItem.canUseKey(keyStack, player, lockable)) {
				return super.canUseKey(stack, player, lockable);
			}
		}

		return false;
	}

	@Override
	public Text getIncorrectKeyMessage() {
		return Text.translatable("item.lockable.keychain.incorrect");
	}

	public static float getKeys(ItemStack stack) {
		return stack.getOrDefault(LockableDataComponents.KEYCHAIN_COMPONENT, DefaultedList.of()).size() / (float) MAX_KEYS;
	}

	private boolean canAddItem(ItemStack keychainStack, ItemStack otherStack) {
		List<ItemStack> stacks = keychainStack.get(LockableDataComponents.KEYCHAIN_COMPONENT);
		if (stacks != null) {
			return otherStack.getItem() instanceof NormalKeyItem && stacks.size() < MAX_KEYS;
		} else {
			return false;
		}
	}

	private void addItem(ItemStack keychainStack, ItemStack keyStack) {
		List<ItemStack> stacks = keychainStack.get(LockableDataComponents.KEYCHAIN_COMPONENT);

		if (stacks != null) {
			List<ItemStack> copy = new ArrayList<>(stacks);
			copy.add(keyStack.split(1));
			keychainStack.set(LockableDataComponents.KEYCHAIN_COMPONENT, copy);
		}
	}

	private void removeLast(ItemStack keychainStack) {
		List<ItemStack> stacks = keychainStack.get(LockableDataComponents.KEYCHAIN_COMPONENT);

		if (stacks != null) {
			List<ItemStack> copy = new ArrayList<>(stacks);
			copy.remove(copy.size() - 1);
			keychainStack.set(LockableDataComponents.KEYCHAIN_COMPONENT, copy);
		}
	}

	private void playRemoveOneSound(PlayerEntity player) {
		player.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.getWorld().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(PlayerEntity player) {
		player.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + player.getWorld().getRandom().nextFloat() * 0.4F);
	}
}
