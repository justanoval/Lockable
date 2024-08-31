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

		@Comment("The weight of how likely it is to be in a Nether Fortress chest. The higher, the more likely.")
		public final TrackedValue<Integer> lootWeight = this.value(3);
	}

	public static class BoneKeyConfig extends Section {
		@FloatRange(min = 0.0, max = 1.0)
		@Comment("The chance for the bone key to break upon use, between 0.0 and 1.0.")
		public final TrackedValue<Float> chanceToBreak = this.value(0.7F);

		@Comment("The weight of how likely it is to be in suspicious sand or gravel. The higher, the more likely.")
		public final TrackedValue<Integer> lootWeight = this.value(3);
	}

	public static class WeatheredLockConfig extends Section {
		@Comment("The weight of how likely it is to be in ocean ruins. The higher, the more likely.")
		public final TrackedValue<Integer> oceanRuinsWeight = this.value(3);

		@Comment("The weight of how likely it is to be in shipwreck treasure chests. The higher, the more likely.")
		public final TrackedValue<Integer> shipwreckWeight = this.value(3);
	}
}
