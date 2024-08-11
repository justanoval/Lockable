package io.github.justanoval.lockable.mixin;

import io.github.justanoval.lockable.api.entity.LockableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.HolderLookup;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LockableContainerBlockEntity.class)
public abstract class LockableContainerBlockEntityMixin extends BlockEntity implements LockableBlockEntity {
	private boolean hasLock = false;
	private boolean locked = false;
	private ItemStack itemStack = ItemStack.EMPTY;

	public LockableContainerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, HolderLookup.Provider lookupProvider, CallbackInfo ci) {
		if (this.hasLock) {
			NbtCompound compound = new NbtCompound();
			compound.putBoolean("Locked", this.locked);

			if (itemStack != ItemStack.EMPTY) {
				compound.put("ItemStack", itemStack.encode(lookupProvider));
			}

			nbt.put("KeyLock", compound);
		} else {
			nbt.remove("KeyLock");
		}
	}

	@Inject(method = "method_11014", at = @At("TAIL"))
	public void method_11014(NbtCompound nbt, HolderLookup.Provider lookupProvider, CallbackInfo ci) {
		this.hasLock = nbt.contains("KeyLock");
		NbtCompound keyLock = nbt.getCompound("KeyLock");
		this.locked = keyLock.getBoolean("Locked");
		this.itemStack = ItemStack.fromNbt(lookupProvider, keyLock.getCompound("ItemStack"));
	}

	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	@Override
	public NbtCompound toSyncedNbt(HolderLookup.Provider lookupProvider) {
		NbtCompound nbt = new NbtCompound();
		this.writeNbt(nbt, lookupProvider);
		return nbt;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
		}
	}

	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
		markDirty();
	}

	@Override
	public boolean isLocked() {
		return this.hasLock && this.locked;
	}

	@Override
	public void setLock(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.hasLock = true;
		this.locked = false;
		markDirty();
	}

	@Override
	public void removeLock() {
		this.hasLock = false;
		this.locked = false;
		markDirty();
	}

	@Override
	public boolean hasLock() {
		return this.hasLock;
	}

	@Override
	public ItemStack getLock() {
		return this.itemStack;
	}
}
