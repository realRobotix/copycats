package com.copycatsplus.copycats.content.copycat.shaft.forge;

import com.copycatsplus.copycats.content.copycat.base.model.functional.forge.BakedModelWithDataBuilder;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;


public class CopycatShaftBlockEntityForge extends CopycatShaftBlockEntity {
    public CopycatShaftBlockEntityForge(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return getCopycatBlockEntity().getModelData();
    }
}
