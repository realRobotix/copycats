package com.copycatsplus.copycats.content.copycat.base.state_switcher;

import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.CCKeys;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.utility.ClientUtils;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.equipment.toolbox.*;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.simibubi.create.content.equipment.toolbox.ToolboxInventory.STACKS_PER_COMPARTMENT;

//Largely copied from the toolbox screen
public class StateSwitcherMenu extends AbstractSimiScreen {

    private static int COOLDOWN;
    private int ticksOpen;
    private int hoveredSlot;
    private boolean scrollMode;
    private int scrollSlot = 0;

    private List<ItemStack> availableStates;
    private State state;

    public StateSwitcherMenu(List<ItemStack> availableStates) {
        this.availableStates = availableStates;
    }

    @SuppressWarnings({"IntegerDivisionInFloatingPointContext", "DuplicatedCode"})
    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float fade = Mth.clamp((ticksOpen + AnimationTickHolder.getPartialTicks()) / 10f, 1 / 512f, 1);

        hoveredSlot = -1;
        Window window = Minecraft.getInstance().getWindow();
        float hoveredX = mouseX - window.getGuiScaledWidth() / 2;
        float hoveredY = mouseY - window.getGuiScaledHeight() / 2;

        float distance = hoveredX * hoveredX + hoveredY * hoveredY;
        if (distance > 25 && distance < 10000)
            hoveredSlot =
                    (Mth.floor((AngleHelper.deg(Mth.atan2(hoveredY, hoveredX)) + 360 + 180 - 22.5f)) % 360)
                            / 45;
        if (scrollMode && distance > 150)
            scrollMode = false;

        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(width / 2, height / 2, 0);
        Component tip = null;
        ResourceLocation tagLoc = Copycats.asResource("test");
        Component title = Components.translatable("tag.item." + tagLoc.getNamespace() + "." + tagLoc.getPath().replace('/', '.'));

        /*
        core rendering
         */


        for (int slot = 0; slot < 8; slot++) {
            ms.pushPose();
            double radius = -40 + (10 * (1 - fade) * (1 - fade));
            double angle = slot * 45 - 45;
            TransformStack.cast(ms)
                    .rotateZ(angle)
                    .translate(0, radius, 0)
                    .rotateZ(-angle);
            ms.translate(-12, -12, 0);

            boolean selected = (slot == (scrollMode ? scrollSlot : hoveredSlot));

            if (slot < availableStates.size()) {
                ItemStack stack = availableStates.get(slot);

                if (minecraft != null) {

                    AllGuiTextures.TOOLBELT_SLOT.render(graphics, 0, 0, Color.RED);
                    GuiGameElement.of(stack)
                            .at(3, 3)
                            .render(graphics);


                    if (selected) {
                        AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, -1, -1, Color.GREEN);
                        tip = Components.empty().append(stack.getHoverName())
                                .withStyle(ChatFormatting.GOLD);
                    }
                }
            } else {
                AllGuiTextures.TOOLBELT_EMPTY_SLOT.render(graphics, 0, 0, Color.TRANSPARENT_BLACK);
            }

            ms.popPose();
        }

        /*
        end core rendering
         */

        {
            int i1 = (int) (fade * 255.0F);
            if (i1 > 255)
                i1 = 255;

            if (i1 > 8) {
                ms.pushPose();
                ms.translate(0, -80, 0.0F);
                drawComponent(ms, title, i1);
            }
        }


        ms.popPose();

        if (tip != null) {
            int i1 = (int) (fade * 255.0F);
            if (i1 > 255)
                i1 = 255;

            if (i1 > 8) {
                ms.pushPose();
                ms.translate((float) (width / 2), (float) (height - 68), 0.0F);
                drawComponent(ms, tip, i1);
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void drawComponent(PoseStack ms, Component title, int i1) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int k1 = 16777215;
        int k = i1 << 24 & -16777216;
        int l = font.width(title);
        /*font.draws(ms, title, (float) (-l / 2), -4.0F, k1 | k);*/
        RenderSystem.disableBlend();
        ms.popPose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        int a = ((int) (0x50 * Math.min(1, (ticksOpen + AnimationTickHolder.getPartialTicks()) / 20f))) << 24;
        graphics.fillGradient(0, 0, this.width, this.height, 0x101010 | a, 0x101010 | a);
    }

    @Override
    public void tick() {
        ticksOpen++;
        super.tick();
    }

    @Override
    public void removed() {
        super.removed();

        int selected = (scrollMode ? scrollSlot : hoveredSlot);

        if (selected < 0)
            return;
        ItemStack stackInSlot = availableStates.get(selected);
        if (stackInSlot.isEmpty())
            return;

/*        AllPackets.getChannel().sendToServer(new ToolboxEquipPacket(selectedBox.getBlockPos(), selected,
                minecraft.player.getInventory().selected));*/
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        Window window = Minecraft.getInstance().getWindow();
        double hoveredX = mouseX - window.getGuiScaledWidth() / 2;
        double hoveredY = mouseY - window.getGuiScaledHeight() / 2;
        double distance = hoveredX * hoveredX + hoveredY * hoveredY;
        if (distance <= 150) {
            scrollMode = true;
            scrollSlot = (((int) (scrollSlot - delta)) + 8) % 8;
            for (int i = 0; i < 10; i++) {

                if (state == State.SELECT_ITEM || state == State.SELECT_ITEM_UNEQUIP) {
                    ItemStack stackInSlot = availableStates.get(i);
                    if (!stackInSlot.isEmpty())
                        break;
                }

                scrollSlot -= Mth.sign(delta);
                scrollSlot = (scrollSlot + 8) % 8;
            }
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int code, int scanCode, int modifiers) {
        KeyMapping[] hotbarBinds = minecraft.options.keyHotbarSlots;
        for (int i = 0; i < hotbarBinds.length && i < 8; i++) {
            if (hotbarBinds[i].matches(code, scanCode)) {

                if (state == State.SELECT_ITEM || state == State.SELECT_ITEM_UNEQUIP) {
                    ItemStack stackInSlot = availableStates.get(i);
                    if (stackInSlot.isEmpty())
                        return false;
                }

                scrollMode = true;
                scrollSlot = i;
                mouseClicked(0, 0, 0);
                return true;
            }
        }

        return super.keyPressed(code, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int code, int scanCode, int modifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(code, scanCode);
        if (ClientUtils.isActiveAndMatches(CCKeys.STATE_SWITCHER_MENU.getKeyMapping(), mouseKey)) {
            onClose();
            return true;
        }
        return super.keyReleased(code, scanCode, modifiers);
    }

    public static enum State {
        SELECT_ITEM, SELECT_ITEM_UNEQUIP
    }
}
