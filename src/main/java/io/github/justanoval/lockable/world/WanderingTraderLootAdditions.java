package io.github.justanoval.lockable.world;

import io.github.justanoval.lockable.LockableMod;
import io.github.justanoval.lockable.config.LockableConfig;
import io.github.justanoval.lockable.items.LockableItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WanderingTraderLootAdditions {
	private static final Random RANDOM = new Random();
	private static final LockableConfig.WeatheredLockConfig.WanderingTraderConfig CONFIG = LockableMod.CONFIG.weatheredLock.wanderingTrader;
	private static final Float WEATHERED_LOCK_CHANCE = CONFIG.chance.getRealValue();
	private static final int WEATHERED_LOCK_MIN_USES = CONFIG.minUses.getRealValue();
	private static final int WEATHERED_LOCK_MAX_USES = CONFIG.maxUses.getRealValue();
	private static final int WEATHERED_LOCK_MIN_EXP = CONFIG.minMerchantExperience.getRealValue();
	private static final int WEATHERED_LOCK_MAX_EXP = CONFIG.maxMerchantExperience.getRealValue();
	private static final List<TradeableItem> WEATHERED_LOCK_OFFERS = new ArrayList<>();

	static {
		CONFIG.tradeOffers.getRealValue().forEach(((key, integers) -> {
			String[] split = key.split(":");
			String namespace = split[0];
			String id = split[1];

			ItemConvertible item = Registries.ITEM.get(Identifier.of(namespace, id));
			int origin = integers.get(0);
			int bound = integers.get(1) + 1;
			int count;

			if (origin == bound || origin > bound) {
				count = origin;
			} else {
				count = RANDOM.nextInt(origin, bound);
			}

			WEATHERED_LOCK_OFFERS.add(new TradeableItem(item, count));
		}));
	}

	public static void register() {
		ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
			if (entity instanceof WanderingTraderEntity wanderingTrader) {
				if (world.random.nextFloat() < WEATHERED_LOCK_CHANCE) {
					TradeOffer tradeOffer = new TradeOffer(
							WEATHERED_LOCK_OFFERS.get(world.random.nextInt(WEATHERED_LOCK_OFFERS.size() - 1)),
							new ItemStack(LockableItems.WEATHERED_LOCK, 1),
							world.random.rangeInclusive(WEATHERED_LOCK_MIN_USES, WEATHERED_LOCK_MAX_USES),
							world.random.rangeInclusive(WEATHERED_LOCK_MIN_EXP, WEATHERED_LOCK_MAX_EXP),
							1.0F
					);

					wanderingTrader.getOffers().add(tradeOffer);
				}
			}
		}));
	}
}
