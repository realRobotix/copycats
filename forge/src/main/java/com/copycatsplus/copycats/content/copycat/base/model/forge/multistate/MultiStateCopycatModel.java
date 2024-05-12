package com.copycatsplus.copycats.content.copycat.base.model.forge.multistate;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.content.decoration.copycat.FilteredBlockAndTintGetter;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class MultiStateCopycatModel extends BakedModelWrapperWithData {

    public static final ModelProperty<Map<String, BlockState>> MATERIALS_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<OcclusionData> OCCLUSION_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<ModelData> WRAPPED_DATA_PROPERTY = new ModelProperty<>();

    public MultiStateCopycatModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                ModelData blockEntityData) {
        @NotNull Map<String, BlockState> material = getMaterials(blockEntityData);
        if (material.isEmpty())
            return builder;

        builder.with(MATERIALS_PROPERTY, material);

        if (!(state.getBlock() instanceof CopycatBlock copycatBlock))
            return builder;

        OcclusionData occlusionData = new OcclusionData();
        gatherOcclusionData(world, pos, state, material.values().stream().findFirst().get(), occlusionData, copycatBlock);
        builder.with(OCCLUSION_PROPERTY, occlusionData);


        ModelData wrappedData = getModelOf(material.values().stream().findFirst().get()).getModelData(
                new FilteredBlockAndTintGetter(world,
                        targetPos -> copycatBlock.canConnectTexturesToward(world, pos, targetPos, state)),
                pos, material.values().stream().findFirst().get(), ModelData.EMPTY);
        return builder.with(WRAPPED_DATA_PROPERTY, wrappedData);
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     OcclusionData occlusionData, CopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {

            // Rubidium: Run an additional IForgeBlock.hidesNeighborFace check because it
            // seems to be missing in Block.shouldRenderFace
            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            BlockState neighbourState = world.getBlockState(neighbourPos);
            if (state.supportsExternalFaceHiding()
                    && neighbourState.hidesNeighborFace(world, neighbourPos, state, face.getOpposite())) {
                occlusionData.occlude(face);
                continue;
            }

            if (!copycatBlock.canFaceBeOccluded(state, face))
                continue;
            if (!Block.shouldRenderFace(material, world, pos, face, neighbourPos))
                occlusionData.occlude(face);
        }
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {

        // Rubidium: see below
        if (side != null && state.getBlock() instanceof CopycatBlock ccb && ccb.shouldFaceAlwaysRender(state, side))
            return Collections.emptyList();

        @NotNull Map<String, BlockState> materials = getMaterials(data);

        if (materials.isEmpty())
            return super.getQuads(state, side, rand, data, renderType);

        OcclusionData occlusionData = data.get(OCCLUSION_PROPERTY);
        if (occlusionData != null && occlusionData.isOccluded(side))
            return super.getQuads(state, side, rand, data, renderType);

        ModelData wrappedData = data.get(WRAPPED_DATA_PROPERTY);
        if (wrappedData == null)
            wrappedData = ModelData.EMPTY;
        for (BlockState material : materials.values()) {
            if (renderType != null && !Minecraft.getInstance()
                    .getBlockRenderer()
                    .getBlockModel(material)
                    .getRenderTypes(material, rand, wrappedData)
                    .contains(renderType))
                return super.getQuads(state, side, rand, data, renderType);
        }

        List<BakedQuad> croppedQuads = getCroppedQuads(state, side, rand, materials, wrappedData, renderType);

        // Rubidium: render side!=null versions of the base material during side==null,
        // to avoid getting culled away
        if (side == null && state.getBlock() instanceof CopycatBlock ccb)
            for (Direction nonOcclusionSide : Iterate.directions)
                if (ccb.shouldFaceAlwaysRender(state, nonOcclusionSide))
                    croppedQuads.addAll(getCroppedQuads(state, nonOcclusionSide, rand, materials, wrappedData, renderType));

        return croppedQuads;
    }

    protected abstract List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand,
                                                       Map<String, BlockState> propertyMaterials, ModelData wrappedData, RenderType renderType);

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        @NotNull Map<String, BlockState> material = getMaterials(data);

        if (material.isEmpty())
            return super.getParticleIcon(data);

        ModelData wrappedData = data.get(WRAPPED_DATA_PROPERTY);
        if (wrappedData == null)
            wrappedData = ModelData.EMPTY;

        return getModelOf(material.entrySet().stream().findFirst().get().getValue()).getParticleIcon(wrappedData);
    }

    public static @NotNull Map<String, BlockState> getMaterials(ModelData data) {
        Map<String, BlockState> materials = data == null ? null : data.get(MATERIALS_PROPERTY);
        return materials == null ? Map.of() : materials;
    }

    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }


    //Copied from com.simibubi.create.content.decoration.copycat.CopycatModel.OcclusionData as it was private
    private static class OcclusionData {
        private final boolean[] occluded;

        public OcclusionData() {
            occluded = new boolean[6];
        }

        public void occlude(Direction face) {
            occluded[face.get3DDataValue()] = true;
        }

        public boolean isOccluded(Direction face) {
            return face == null ? false : occluded[face.get3DDataValue()];
        }
    }
}
