package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import javax.annotation.Nullable;

public interface IFunctionalCopycatBlockInstance {

    @Nullable
    KineticCopycatRenderData getRenderData();

    void setRenderData(KineticCopycatRenderData renderData);

    IFunctionalCopycatBlockEntity getBlockEntity();

    Material<RotatingData> getRotatingMaterial();

    default Instancer<RotatingData> getModel() {
        KineticCopycatRenderData renderData = KineticCopycatRenderData.of(getBlockEntity());
        setRenderData(renderData);
        return getRotatingMaterial().model(renderData, () -> FunctionalCopycatRenderHelper.getInstanceModel(getBlockEntity(), renderData));
    }

    default boolean shouldReset() {
        if (getRenderData() == null)
            return true;
        if (getRenderData().enableCT() != getBlockEntity().isCTEnabled())
            return true;
        if (!getRenderData().material().equals(getBlockEntity().getMaterial()))
            return true;
        if (!getRenderData().state().equals(getBlockEntity().getBlockState()))
            return true;
        return false;
    }
}
