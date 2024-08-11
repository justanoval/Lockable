package io.github.justanoval.lockable.mixin;

import io.github.justanoval.lockable.api.Lockable;
import io.github.justanoval.lockable.api.block.LockableBlock;
import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import io.github.justanoval.lockable.api.key.KeyItem;
import io.github.justanoval.lockable.api.key.KeyItemInteraction;
import io.github.justanoval.lockable.api.lock.LockItem;
import io.github.justanoval.lockable.api.lock.LockItemInteraction;
import io.github.justanoval.lockable.util.ChestUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.feature_flags.GatedFeature;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemInteractionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements GatedFeature, LockableBlock {
	@Inject(method = "onInteract", at = @At("RETURN"), cancellable = true)
	public void onInteract(
			ItemStack stack,
			BlockState state,
			World world,
			BlockPos pos,
			PlayerEntity player,
			Hand hand,
			BlockHitResult hitResult,
			CallbackInfoReturnable<ItemInteractionResult> cir
	) {
		AbstractBlock abstractBlock = (AbstractBlock) (Object) this;

		if (abstractBlock instanceof BlockWithEntity && hand.equals(Hand.MAIN_HAND)) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			Lockable.as(blockEntity, lockable -> {
				BlockPos adjacentChestPos = ChestUtils.getAdjacentChest(state, pos);

				if (adjacentChestPos != null) {
					BlockEntity adjacentChestEntity = world.getBlockEntity(adjacentChestPos);
					BlockState adjacentState = world.getBlockState(adjacentChestPos);
					Block adjacentBlock = adjacentState.getBlock();

					LockableBlock adjacentLockable = (LockableBlock) adjacentBlock;

					Lockable.as(adjacentChestEntity, adjacentLockableEntity -> {
						if (adjacentLockableEntity.hasLock()) {
							boolean interaction = adjacentLockable.tryInteractLock(stack, world, pos, player, adjacentLockableEntity);

							cir.setReturnValue(interaction ? cir.getReturnValue() : ItemInteractionResult.FAIL);
						}
					});
				}

				boolean interaction = tryInteractLock(stack, world, pos, player, lockable);

				cir.setReturnValue(interaction ? cir.getReturnValue() : ItemInteractionResult.FAIL);
			});
		}
	}

	@Override
	public boolean tryInteractLock(
			ItemStack stack,
			World world,
			BlockPos pos,
			PlayerEntity player,
			LockableBlockEntity lockable
	) {
		if (stack.getItem() instanceof LockItem lockItemInHand) {
			LockItemInteraction result = lockItemInHand.tryUseLock(stack, lockable);

			switch (result) {
				case HAS_LOCK -> alertPlayer(player, Text.translatable("item.lockable.lock.has_lock.put_lock"));
				case PLACE -> lockItemInHand.place(world, pos, stack, lockable, player);
				case PASS -> {
					return true;
				}
			}

			return false;
		} else {
			ItemStack lock = lockable.getLock();
			Item item = lock.getItem();

			if (!(item instanceof LockItem lockItem)) {
				return true;
			}

			if (stack.getItem() instanceof KeyItem keyItem) {
				KeyItemInteraction result = keyItem.tryUseKey(stack, player, lockable);

				switch (result) {
					case LOCK -> {
						keyItem.lock(stack, player, world, pos, lockable);
						world.playSound(null, pos, lockItem.getLockSound(), SoundCategory.BLOCKS, 0.8f, 1.0f);
						lockItem.onLock(stack, lock, player, lockable);
					}
					case UNLOCK -> {
						keyItem.unlock(stack, player, world, pos, lockable);
						world.playSound(null, pos, lockItem.getUnlockSound(), SoundCategory.BLOCKS, 0.8f, 1.0f);
						lockItem.onUnlock(stack, lock, player, lockable);
					}
					case FAIL -> {
						alertPlayer(player, keyItem.getIncorrectKeyMessage());
						world.playSound(null, pos, lockItem.getKeyFailSound(), SoundCategory.PLAYERS, 1.0f, 1.0f);
					}
					case PASS -> {
						return true;
					}
				}

				player.swingHand(Hand.MAIN_HAND);

				return false;
			} else if (lockable.hasLock() && !lockable.isLocked() && stack.isEmpty()) {
				LockItem.remove(world, pos, lockable, player.getInventory());
				return false;
			} else {
				if (lockable.hasLock() && !lockable.isLocked() && !player.isSneaking()) {
					alertPlayer(player, Text.translatable("item.lockable.lock.has_lock"));
				} else if (lockable.isLocked()) {
					alertPlayer(player, Text.translatable("item.lockable.lock.locked"));
				} else {
					return true;
				}

				player.swingHand(Hand.MAIN_HAND);

				world.playSound(null, pos, lockItem.getLockFailSound(), SoundCategory.PLAYERS, 1.0f, 1.0f);

				return false;
			}
		}
	}

	@Inject(method = "getDroppedStacks", at = @At("RETURN"), cancellable = true)
	protected void getDroppedStacks(
			BlockState state,
			LootContextParameterSet.Builder lootParameterBuilder,
			CallbackInfoReturnable<List<ItemStack>> cir
	) {
		BlockEntity blockEntity = lootParameterBuilder.getOptionalParameter(LootContextParameters.BLOCK_ENTITY);

		if (blockEntity != null) {
			Lockable.as(blockEntity, lockable -> {
				if (lockable != null && lockable.hasLock() && lockable.getLock() != null) {
					List<ItemStack> loot = cir.getReturnValue();
					loot.add(lockable.getLock());
					cir.setReturnValue(loot);
				}
			});
		}
	}

	private static void alertPlayer(PlayerEntity player, Text text) {
		player.sendMessage(text, true);
	}
}
