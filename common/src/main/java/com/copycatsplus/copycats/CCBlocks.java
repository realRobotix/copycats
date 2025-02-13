package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.ToggleableCopycatModel;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamBlock;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamModel;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockBlock;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockModel;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.board.CopycatMultiBoardModel;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonModel;
import com.copycatsplus.copycats.content.copycat.button.CopycatStoneButtonBlock;
import com.copycatsplus.copycats.content.copycat.button.CopycatWoodenButtonBlock;
import com.copycatsplus.copycats.content.copycat.button.WrappedButton;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatMultiByteModel;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceBlock;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceModel;
import com.copycatsplus.copycats.content.copycat.fence.WrappedFenceBlock;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateModel;
import com.copycatsplus.copycats.content.copycat.fence_gate.WrappedFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.ghost_block.CopycatGhostBlock;
import com.copycatsplus.copycats.content.copycat.ghost_block.CopycatGhostBlockModel;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatMultiHalfLayerModel;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelModel;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderModel;
import com.copycatsplus.copycats.content.copycat.ladder.WrappedLadderBlock;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerBlock;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerModel;
import com.copycatsplus.copycats.content.copycat.pressure_plate.*;
import com.copycatsplus.copycats.content.copycat.slab.CopycatMultiSlabModel;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceModel;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeBlock;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeEnhancedModel;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModel;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsEnhancedModel;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsModel;
import com.copycatsplus.copycats.content.copycat.stairs.WrappedStairsBlock;
import com.copycatsplus.copycats.content.copycat.test_block.CopycatTestBlock;
import com.copycatsplus.copycats.content.copycat.test_block.CopycatTestBlockModel;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorModel;
import com.copycatsplus.copycats.content.copycat.trapdoor.WrappedTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceModel;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairsEnhancedModel;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairsModel;
import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepBlock;
import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepModel;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallBlock;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallModel;
import com.copycatsplus.copycats.content.copycat.wall.WrappedWallBlock;
import com.copycatsplus.copycats.datagen.CCLootGen;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.simibubi.create.foundation.data.CreateRegistrate.blockModel;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

@SuppressWarnings("unused")
//noinspection unchecked
public class CCBlocks {

    private static final CopycatRegistrate REGISTRATE = Copycats.getRegistrate();

    public static final BlockEntry<CopycatBlockBlock> COPYCAT_BLOCK =
            REGISTRATE.block("copycat_block", CopycatBlockBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatBlockModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_BLOCK.tag)
                    .transform(customItemModel("copycat_base", "block"))
                    .register();

    public static final BlockEntry<CopycatBeamBlock> COPYCAT_BEAM =
            REGISTRATE.block("copycat_beam", CopycatBeamBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatBeamModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_BEAM.tag)
                    .transform(customItemModel("copycat_base", "beam"))
                    .register();

    public static final BlockEntry<CopycatBoardBlock> COPYCAT_BOARD =
            REGISTRATE.block("copycat_board", CopycatBoardBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleMultiStateCopycatPart.create(model, new CopycatMultiBoardModel())))
                    .loot(CCLootGen.build(CCLootGen.lootForDirections()))
                    .item()
                    .tag(CCTags.Items.COPYCAT_BOARD.tag)
                    .transform(customItemModel("copycat_base", "board"))
                    .register();

    public static final BlockEntry<CopycatWoodenButtonBlock> COPYCAT_WOODEN_BUTTON =
            REGISTRATE.block("copycat_wooden_button", CopycatWoodenButtonBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.WOODEN_BUTTONS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatButtonModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "button"))
                    .register();

    public static final BlockEntry<CopycatStoneButtonBlock> COPYCAT_STONE_BUTTON =
            REGISTRATE.block("copycat_stone_button", CopycatStoneButtonBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.STONE_BUTTONS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatButtonModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "button"))
                    .register();

    public static final BlockEntry<WrappedButton.Wood> WRAPPED_COPYCAT_WOODEN_BUTTON =
            REGISTRATE.block("wrapped_copycat_wooden_button", p -> new WrappedButton().wood(p, BlockSetType.OAK, 30, true))
                    .initialProperties(() -> Blocks.OAK_BUTTON)
                    .onRegister(b -> CopycatWoodenButtonBlock.button = b)
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.WOODEN_BUTTONS)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_wooden_button"))
                    .register();

    public static final BlockEntry<WrappedButton.Stone> WRAPPED_COPYCAT_STONE_BUTTON =
            REGISTRATE.block("wrapped_copycat_stone_button", p -> new WrappedButton().stone(p, BlockSetType.STONE, 20, false))
                    .initialProperties(() -> Blocks.STONE_BUTTON)
                    .onRegister(b -> CopycatStoneButtonBlock.button = b)
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.STONE_BUTTONS)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_stone_button"))
                    .register();

    public static final BlockEntry<CopycatByteBlock> COPYCAT_BYTE =
            REGISTRATE.block("copycat_byte", CopycatByteBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleMultiStateCopycatPart.create(model, new CopycatMultiByteModel())))
                    .loot(CCLootGen.build(CCLootGen.lootForBytes()))
                    .item()
                    .transform(customItemModel("copycat_base", "byte"))
                    .register();

    public static final BlockEntry<CopycatFenceBlock> COPYCAT_FENCE =
            REGISTRATE.block("copycat_fence", CopycatFenceBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.FENCES, CCTags.commonBlockTag("fences"))
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatFenceModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_FENCE.tag)
                    .transform(customItemModel("copycat_base", "fence"))
                    .register();

    public static final BlockEntry<WrappedFenceBlock> WRAPPED_COPYCAT_FENCE =
            REGISTRATE.block("wrapped_copycat_fence", WrappedFenceBlock::new)
                    .initialProperties(() -> Blocks.OAK_FENCE)
                    .onRegister(b -> CopycatFenceBlock.fence = b)
                    .tag(BlockTags.FENCES, CCTags.commonBlockTag("fences"))
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_fence"))
                    .register();

    public static final BlockEntry<CopycatFenceGateBlock> COPYCAT_FENCE_GATE =
            REGISTRATE.block("copycat_fence_gate", CopycatFenceGateBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(BlockBehaviour.Properties::forceSolidOn)
                    .tag(BlockTags.FENCE_GATES, CCTags.commonBlockTag("fence_gates"), BlockTags.UNSTABLE_BOTTOM_CENTER, AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatFenceGateModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_FENCE_GATE.tag)
                    .transform(customItemModel("copycat_base", "fence_gate"))
                    .register();

    public static final BlockEntry<WrappedFenceGateBlock> WRAPPED_COPYCAT_FENCE_GATE =
            REGISTRATE.block("wrapped_copycat_fence_gate", p -> new WrappedFenceGateBlock(p, WoodType.OAK))
                    .initialProperties(() -> Blocks.OAK_FENCE_GATE)
                    .onRegister(b -> CopycatFenceGateBlock.fenceGate = b)
                    .tag(BlockTags.FENCE_GATES, CCTags.commonBlockTag("fence_gates"), BlockTags.UNSTABLE_BOTTOM_CENTER, AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_fence_gate"))
                    .register();

    public static final BlockEntry<CopycatGhostBlock> COPYCAT_GHOST_BLOCK =
            REGISTRATE.block("copycat_ghost_block", CopycatGhostBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatGhostBlockModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "ghost_block"))
                    .register();

    public static final BlockEntry<CopycatHalfLayerBlock> COPYCAT_HALF_LAYER =
            REGISTRATE.block("copycat_half_layer", CopycatHalfLayerBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleMultiStateCopycatPart.create(model, new CopycatMultiHalfLayerModel())))
                    .loot(CCLootGen.build(
                            CCLootGen.lootForLayers(CopycatHalfLayerBlock.NEGATIVE_LAYERS),
                            CCLootGen.lootForLayers(CopycatHalfLayerBlock.POSITIVE_LAYERS)
                    ))
                    .item()
                    .transform(customItemModel("copycat_base", "half_layer"))
                    .register();

    public static final BlockEntry<CopycatHalfPanelBlock> COPYCAT_HALF_PANEL =
            REGISTRATE.block("copycat_half_panel", CopycatHalfPanelBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatHalfPanelModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "half_panel"))
                    .register();

    public static final BlockEntry<WrappedLadderBlock> WRAPPED_COPYCAT_LADDER =
            REGISTRATE.block("wrapped_copycat_ladder", WrappedLadderBlock::new)
                    .initialProperties(() -> Blocks.LADDER)
                    .onRegister(b -> CopycatLadderBlock.ladder = b)
                    .tag(BlockTags.CLIMBABLE)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_ladder"))
                    .register();

    public static final BlockEntry<CopycatLadderBlock> COPYCAT_LADDER =
            REGISTRATE.block("copycat_ladder", CopycatLadderBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.CLIMBABLE)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatLadderModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "ladder"))
                    .register();

    public static final BlockEntry<CopycatLayerBlock> COPYCAT_LAYER =
            REGISTRATE.block("copycat_layer", CopycatLayerBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatLayerModel())))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .transform(customItemModel("copycat_base", "layer"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.Wooden> WRAPPED_COPYCAT_WOODEN_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_wooden_pressure_plate", p -> new WrappedPressurePlate().wooden(PressurePlateBlock.Sensitivity.EVERYTHING, p, BlockSetType.OAK))
                    .initialProperties(() -> Blocks.OAK_BUTTON)
                    .onRegister(b -> CopycatWoodenPressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.WOODEN_PRESSURE_PLATES)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_wooden_pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatWoodenPressurePlate> COPYCAT_WOODEN_PRESSURE_PLATE =
            REGISTRATE.block("copycat_wooden_pressure_plate", CopycatWoodenPressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.WOODEN_PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatPressurePlateModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.Stone> WRAPPED_COPYCAT_STONE_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_stone_pressure_plate", p -> new WrappedPressurePlate().stone(PressurePlateBlock.Sensitivity.MOBS, p, BlockSetType.STONE))
                    .initialProperties(() -> Blocks.STONE_BUTTON)
                    .onRegister(b -> CopycatStonePressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.STONE_PRESSURE_PLATES)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_stone_pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatStonePressurePlate> COPYCAT_STONE_PRESSURE_PLATE =
            REGISTRATE.block("copycat_stone_pressure_plate", CopycatStonePressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.STONE_PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatPressurePlateModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.HeavyWeighted> WRAPPED_COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_heavy_weighted_pressure_plate", p -> new WrappedPressurePlate().heavyWeighted(150, p, BlockSetType.IRON))
                    .initialProperties(() -> Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                    .onRegister(b -> CopycatHeavyWeightedPressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_heavy_weighted_pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatHeavyWeightedPressurePlate> COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("copycat_heavy_weighted_pressure_plate", CopycatHeavyWeightedPressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatPressurePlateModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.LightWeighted> WRAPPED_COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_light_weighted_pressure_plate", p -> new WrappedPressurePlate().lightWeighted(15, p, BlockSetType.GOLD))
                    .initialProperties(() -> Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                    .onRegister(b -> CopycatLightWeightedPressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_light_weighted_pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatLightWeightedPressurePlate> COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("copycat_light_weighted_pressure_plate", CopycatLightWeightedPressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatPressurePlateModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatSlabBlock> COPYCAT_SLAB =
            REGISTRATE.block("copycat_slab", CopycatSlabBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .tag(BlockTags.SLABS)
                    .transform(FeatureToggle.register())
                    .loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)))
                    .onRegister(blockModel(() -> model -> SimpleMultiStateCopycatPart.create(model, new CopycatMultiSlabModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_SLAB.tag)
                    .transform(customItemModel("copycat_base", "slab"))
                    .register();

    public static final BlockEntry<CopycatSliceBlock> COPYCAT_SLICE =
            REGISTRATE.block("copycat_slice", CopycatSliceBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatSliceModel())))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .transform(customItemModel("copycat_base", "slice"))
                    .register();

    public static final BlockEntry<CopycatStairsBlock> COPYCAT_STAIRS =
            REGISTRATE.block("copycat_stairs", CopycatStairsBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.STAIRS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> ToggleableCopycatModel.with(new CopycatStairsModel(), new CopycatStairsEnhancedModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_STAIRS.tag)
                    .transform(customItemModel("copycat_base", "stairs"))
                    .register();

    public static final BlockEntry<CopycatVerticalStairBlock> COPYCAT_VERTICAL_STAIRS =
            REGISTRATE.block("copycat_vertical_stairs", CopycatVerticalStairBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.STAIRS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> ToggleableCopycatModel.with(new CopycatVerticalStairsModel(), new CopycatVerticalStairsEnhancedModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_STAIRS.tag)
                    .transform(customItemModel("copycat_base", "vertical_stairs"))
                    .register();

    public static final BlockEntry<WrappedStairsBlock> WRAPPED_COPYCAT_STAIRS =
            REGISTRATE.block("wrapped_copycat_stairs", p -> new WrappedStairsBlock(Blocks.STONE.defaultBlockState(), p))
                    .initialProperties(() -> Blocks.STONE_STAIRS)
                    .onRegister(b -> CopycatStairsBlock.stairs = b)
                    .tag(BlockTags.STAIRS)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_stairs"))
                    .register();

    public static final BlockEntry<CopycatTrapdoorBlock> COPYCAT_TRAPDOOR =
            REGISTRATE.block("copycat_trapdoor", CopycatTrapdoorBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.TRAPDOORS)
                    .tag(BlockTags.WOODEN_TRAPDOORS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatTrapdoorModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "trapdoor"))
                    .register();

    public static final BlockEntry<WrappedTrapdoorBlock> WRAPPED_COPYCAT_TRAPDOOR =
            REGISTRATE.block("wrapped_copycat_trapdoor", p -> new WrappedTrapdoorBlock(p, BlockSetType.OAK))
                    .initialProperties(() -> Blocks.OAK_TRAPDOOR)
                    .onRegister(b -> CopycatTrapdoorBlock.trapdoor = b)
                    .tag(BlockTags.TRAPDOORS)
                    .tag(BlockTags.WOODEN_TRAPDOORS)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_trapdoor"))
                    .register();

    public static final BlockEntry<CopycatVerticalSliceBlock> COPYCAT_VERTICAL_SLICE =
            REGISTRATE.block("copycat_vertical_slice", CopycatVerticalSliceBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatVerticalSliceModel())))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .transform(customItemModel("copycat_base", "vertical_slice"))
                    .register();

    public static final BlockEntry<CopycatVerticalStepBlock> COPYCAT_VERTICAL_STEP =
            REGISTRATE.block("copycat_vertical_step", CopycatVerticalStepBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatVerticalStepModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_VERTICAL_STEP.tag)
                    .transform(customItemModel("copycat_base", "vertical_step"))
                    .register();

    public static final BlockEntry<CopycatWallBlock> COPYCAT_WALL =
            REGISTRATE.block("copycat_wall", CopycatWallBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(BlockBehaviour.Properties::forceSolidOn)
                    .tag(BlockTags.WALLS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleCopycatPart.create(model, new CopycatWallModel())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_WALL.tag)
                    .transform(customItemModel("copycat_base", "wall"))
                    .register();

    public static final BlockEntry<WrappedWallBlock> WRAPPED_COPYCAT_WALL =
            REGISTRATE.block("wrapped_copycat_wall", WrappedWallBlock::new)
                    .initialProperties(() -> Blocks.COBBLESTONE_WALL)
                    .onRegister(b -> CopycatWallBlock.wall = b)
                    .tag(BlockTags.WALLS)
                    .blockstate((c, p) -> getWrappedBlockState(c, p, "wrapped_copycat_wall"))
                    .register();

    public static final BlockEntry<CopycatSlopeBlock> COPYCAT_SLOPE =
            REGISTRATE.block("copycat_slope", CopycatSlopeBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> ToggleableCopycatModel.with(new CopycatSlopeModel(), new CopycatSlopeEnhancedModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "slope"))
                    .register();

    public static @Nullable BlockEntry<CopycatTestBlock> COPYCAT_TEST_BLOCK;

    @ExpectPlatform
    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
        throw new AssertionError();
    }

    public static void register() {
        if (AbstractRegistrate.isDevEnvironment()) {
            COPYCAT_TEST_BLOCK = REGISTRATE.block("copycat_test_block", CopycatTestBlock::new)
                    .transform(CCBuilderTransformers.testBlockMultiCopycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> SimpleMultiStateCopycatPart.create(model, new CopycatTestBlockModel())))
                    .item()
                    .transform(customItemModel("copycat_base", "test_block"))
                    .register();
        }
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredBlocks() {
        return new HashSet<>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key()));
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredBlocksWithoutWrapped() {
        return new HashSet<>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key())).stream().filter(entry -> !(entry.getId().getPath().startsWith("wrapped"))).collect(Collectors.toSet());
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredMultiStateBlocks() {
        return new HashSet<>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key())).stream().filter(entry -> entry.get() instanceof MultiStateCopycatBlock).collect(Collectors.toSet());
    }
}
