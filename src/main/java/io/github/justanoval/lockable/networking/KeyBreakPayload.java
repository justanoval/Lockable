package io.github.justanoval.lockable.networking;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.mixin.access.LivingEntityInvoker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.util.Identifier;

public record KeyBreakPayload(ItemStack itemStack) implements CustomPayload {
	public static final Identifier IDENTIFIER = LockableMod.id("key_break");
	public static final CustomPayload.Id<KeyBreakPayload> ID = new CustomPayload.Id<>(IDENTIFIER);
	public static final PacketCodec<RegistryByteBuf, KeyBreakPayload> CODEC = PacketCodec.tuple(
			ItemStack.PACKET_CODEC, KeyBreakPayload::itemStack, KeyBreakPayload::new
	);

	public static void receivePayload(KeyBreakPayload payload, ClientPlayNetworking.Context context) {
		((LivingEntityInvoker) context.player()).lockable$playEquipmentBreakEffects(payload.itemStack());
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}
}
