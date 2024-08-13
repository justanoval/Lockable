package io.github.justanoval.lockable.api.key;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BreakableKeyItem extends AbstractKeyItem {
	public BreakableKeyItem(Settings settings) {
		super(settings);
	}

	public Text getBrokenMessage() {
		return Text.translatable("item.lockable.key.break");
	}

	public abstract Float getBreakChance();

	public void breakKey(ItemStack itemStack, ServerPlayerEntity player) {
		// TODO: future proofing for when i add offhand stuff, but not sure if this works
		if (player.getOffHandStack() == itemStack) {
			player.onEquippedItemBroken(itemStack.getItem(), EquipmentSlot.OFFHAND);
		} else {
			player.onEquippedItemBroken(itemStack.getItem(), EquipmentSlot.MAINHAND);
		}

		player.sendMessage(this.getBrokenMessage(), true);
		player.getInventory().removeStack(player.getInventory().getSlotWithStack(itemStack), 1);
	}

	public void tryBreakKey(ItemStack itemStack, PlayerEntity player) {
		Float breakChance = getBreakChance();
		World world = player.getWorld();

		if (!world.isClient && world.random.nextFloat() < breakChance) {
			this.breakKey(itemStack, (ServerPlayerEntity) player);
		}
	}

	@Override
	public void unlock(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, LockableBlockEntity lockable) {
		this.tryBreakKey(itemStack, player);
		super.unlock(itemStack, player, world, pos, lockable);
	}

	@Override
	public void lock(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, LockableBlockEntity lockable) {
		this.tryBreakKey(itemStack, player);
		super.lock(itemStack, player, world, pos, lockable);
	}
}
