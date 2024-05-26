package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.HashMap;
import java.util.Map;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.CopycatRenderContext;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.assemblePiece;
import static com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock.byDirection;

public class CopycatMultiBoardModel implements SimpleMultiStateCopycatPart {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Map<Direction, Boolean> topEdges = new HashMap<>();
        Map<Direction, Boolean> bottomEdges = new HashMap<>();
        Map<Direction, Boolean> leftEdges = new HashMap<>();

        for (Direction direction : Iterate.horizontalDirections) {
            topEdges.put(direction, false);
            bottomEdges.put(direction, false);
            leftEdges.put(direction, false);
        }

        for (Direction direction : Iterate.directions) {
            BooleanProperty prop = byDirection(direction);
            if (state.getValue(prop))
                if (direction.getAxis().isVertical()) {
                    Map<Direction, Boolean> edges = direction == Direction.DOWN ? bottomEdges : topEdges;
                    int north = !edges.get(Direction.NORTH) ? 1 : 0;
                    int south = !edges.get(Direction.SOUTH) ? 1 : 0;
                    int east = !edges.get(Direction.EAST) ? 1 : 0;
                    int west = !edges.get(Direction.WEST) ? 1 : 0;
                    if (north == 1) edges.put(Direction.NORTH, true);
                    if (south == 1) edges.put(Direction.SOUTH, true);
                    if (east == 1) edges.put(Direction.EAST, true);
                    if (west == 1) edges.put(Direction.WEST, true);
                    if (key.equals(prop.getName()))
                        assemblePiece(context, 0, direction == Direction.UP,
                                vec3(1 - west, 0, 1 - north),
                                aabb(14 + east + west, 1, 14 + north + south).move(1 - west, 0, 1 - north),
                                cull(NORTH * (1 - north) | SOUTH * (1 - south) | EAST * (1 - east) | WEST * (1 - west))
                        );
                } else {
                    int up = !topEdges.get(direction) ? 1 : 0;
                    int down = !bottomEdges.get(direction) ? 1 : 0;
                    int left = !leftEdges.get(direction) ? 1 : 0;
                    int right = !leftEdges.get(direction.getCounterClockWise()) ? 1 : 0;
                    if (up == 1) topEdges.put(direction, true);
                    if (down == 1) bottomEdges.put(direction, true);
                    if (left == 1) leftEdges.put(direction, true);
                    if (right == 1) leftEdges.put(direction.getCounterClockWise(), true);
                    if (key.equals(prop.getName()))
                        assemblePiece(context, (int) direction.toYRot() + 180, false,
                                vec3(1 - right, 1 - down, 0),
                                aabb(14 + left + right, 14 + up + down, 1).move(1 - right, 1 - down, 0),
                                cull(UP * (1 - up) | DOWN * (1 - down) | EAST * (1 - left) | WEST * (1 - right))
                        );
                }
        }
    }
}
