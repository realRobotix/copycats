package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.network.CCPackets;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Copycats {
	public static final String MODID = "copycats";
	//Only used for the data fixers!!!
	public static final int DATA_FIXER_VERSION = 1;
	public static final Logger LOGGER = LoggerFactory.getLogger("Copycats+");

	private static final CopycatRegistrate REGISTRATE = CopycatRegistrate.create(MODID);

	static {
		REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
				.andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}

	public static void init() {

		CCCreativeTabs.setCreativeTab();

		CCBlocks.register();
		CCBlockEntityTypes.register();
		CCCatVariants.register();
		CCItems.register();

		CCConfigs.register();

		CCPackets.PACKETS.registerC2SListener();

		finalizeRegistrate();
	}

	public static void gatherData(DataGenerator.PackGenerator gen) {
		gen.addProvider(CCStandardRecipes::new);
	}

	public static CopycatRegistrate getRegistrate() {
		return REGISTRATE;
	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MODID, path);
	}

	@ExpectPlatform
	public static void finalizeRegistrate() {
		throw new AssertionError();
	}

}
