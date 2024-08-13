package io.github.justanoval.lockable.config;

import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.annotations.IntegerRange;
import org.quiltmc.config.api.annotations.SerializedNameConvention;
import org.quiltmc.config.api.metadata.NamingSchemes;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.api.values.ValueList;
import org.quiltmc.config.api.values.ValueMap;

import java.util.List;

@SerializedNameConvention(NamingSchemes.SNAKE_CASE)
public class LockableConfig extends ReflectiveConfig {
	@Comment("Allows for tweaking of which blocks are lockable.")
	public final LockabilityConfig lockability = new LockabilityConfig();

	@Comment("Allows for tweaking functionality of the Keychain item.")
	public final KeychainConfig keychain = new KeychainConfig();

	@Comment("Allows for tweaking functionality of the Bone Key item.")
	public final BoneKeyConfig boneKey = new BoneKeyConfig();

	@Comment("Allows for tweaking functionality of the Weathered Lock item.")
	public final WeatheredLockConfig weatheredLock = new WeatheredLockConfig();

	public static class LockabilityConfig extends Section {
		@Comment("The IDs of blocks that are not lockable.")
		public final TrackedValue<ValueList<String>> blacklist = this.list(
				"",
				"minecraft:hopper",
				"minecraft:shulker_box"
		);

		@Comment("If true, then only the values in the blacklist are lockable.")
		public final TrackedValue<Boolean> isWhitelist = this.value(false);
	}

	public static class KeychainConfig extends Section {
		@Comment("The maximum amount of keys a keychain can hold. It's recommended to be greater than or equal to 3.")
		public final TrackedValue<Integer> maxKeys = this.value(6);
	}

	public static class BoneKeyConfig extends Section {
		@FloatRange(min = 0.0, max = 1.0)
		@Comment("The chance for the bone key to break upon use, between 0.0 and 1.0.")
		public final TrackedValue<Float> chanceToBreak = this.value(0.7F);
	}

	public static class WeatheredLockConfig extends Section {
		@Comment("Allows for tweaking the trade offers for the Weathered Lock.")
		public final WanderingTraderConfig wanderingTrader = new WanderingTraderConfig();

		public static class WanderingTraderConfig extends Section {
			@Comment("The chance for a Weathered Lock to appear in the trade offers for a Wandering Trader.")
			public final TrackedValue<Float> chance = this.value(0.3F);

			@Comment("The minimum amount of times a player can buy the trade from the Wandering Trader.")
			public final TrackedValue<Integer> minUses = this.value(1);

			@Comment("The maximum amount of times a player can buy the trade from the Wandering Trader.")
			public final TrackedValue<Integer> maxUses = this.value(3);

			@Comment("The minimum amount of experience the player gets from the trade.")
			public final TrackedValue<Integer> minMerchantExperience = this.value(40);

			@Comment("The maximum amount of experience the player gets from the trade.")
			public final TrackedValue<Integer> maxMerchantExperience = this.value(60);

			@Comment("The various trades that the Wandering Trader offers for the Weathered Lock.")
			public final TrackedValue<ValueMap<ValueList<Integer>>> tradeOffers = this.map(ValueList.create(1))
					.put("minecraft:golden_apple", ValueList.create(1, 1, 3))
					.put("minecraft:emerald", ValueList.create(1, 32, 64))
					.put("minecraft:diamond", ValueList.create(1, 2, 6))
					.put("minecraft:amethyst_shard", ValueList.create(1, 8, 32))
					.put("minecraft:rabbit_foot", ValueList.create(1, 1, 1))
					.put("minecraft:blaze_powder", ValueList.create(1, 8, 33))
					.put("minecraft:netherite_scrap", ValueList.create(1, 4, 7))
					.build();
		}
	}
}
