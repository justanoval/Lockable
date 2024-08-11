package io.github.justanoval.lockable.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public final class LockablePackets {
	public static void registerServerPackets() {
		PayloadTypeRegistry.playS2C().register(KeyBreakPayload.ID, KeyBreakPayload.CODEC);
	}

	@ClientOnly
	public static void registerClientReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(KeyBreakPayload.ID, KeyBreakPayload::receivePayload);
	}
}
