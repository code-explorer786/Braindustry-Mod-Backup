package braindustry.cfunc;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.graphics.ModMenuRenderer;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Leg;
import mindustry.graphics.*;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import java.util.Arrays;

import static mindustry.Vars.world;

public class BackgroundUnitData {
    private static final Vec2 legOffset = new Vec2();
    public static ModMenuRenderer parent;
    public Trail tleft, tright;
    public Color trailColor;
    public Leg[] legs;
    public float x, y, deltaX, deltaY;
    public float totalLength;

    public BackgroundUnitData() {
        reset();
    }

    public void reset() {
        clear();
        resetLegs();
        set(new Trail(1), new Trail(1), Blocks.water.mapColor.cpy().mul(1.5f));
    }

    public void set(Trail tleft, Trail tright, Color trailColor) {
        this.tleft = tleft;
        this.tright = tright;
        this.trailColor = trailColor;
    }

    public void updatePos(float x, float y) {
        deltaX = x - this.x;
        deltaY = y - this.y;
        if (deltaX<0 || deltaY<0){
//            reset();
        }
        this.x = x;
        this.y = y;
    }

    public void clear() {
        tleft = null;
        tright = null;
        trailColor = null;
        deltaX = deltaY = x = y = totalLength = 0;
    }

    public void resetLegs() {
        UnitType type = parent.unitType();
        float rot = parent.unitRot();
        int count = type.legCount;
        float legLength = type.legLength;
        if (legs != null) {
            Arrays.fill(legs,null);
            legs=null;
        }
        this.legs = new Leg[count];

        float spacing = 360f / count;

        for (int i = 0; i < legs.length; i++) {
            Leg l = new Leg();

            l.joint.trns(i * spacing + rot, legLength / 2f + type.legBaseOffset);
            l.base.trns(i * spacing + rot, legLength + type.legBaseOffset);

            legs[i] = l;
        }
    }

    public void drawLegs(float x, float y) {
        updatePos(x, y);
        updateLegs();
        UnitType type = parent.unitType();
        float ssize = type.footRegion.width * Draw.scl * 1.5f;
        float rotation = parent.unitRot();

        for (Leg leg : legs) {
            Drawf.shadow(leg.base.x, leg.base.y, ssize);
        }

        //legs are drawn front first
        for (int j = legs.length - 1; j >= 0; j--) {
            int i = (j % 2 == 0 ? j / 2 : legs.length - 1 - j / 2);
            Leg leg = legs[i];
            float angle = legAngle(rotation, i);
            boolean flip = i >= legs.length / 2f;
            int flips = Mathf.sign(flip);

            Vec2 position = legOffset.trns(angle, type.legBaseOffset).add(x, y);

            Tmp.v1.set(leg.base).sub(leg.joint).inv().setLength(type.legExtension);

            if (leg.moving && type.visualElevation > 0) {
                float scl = type.visualElevation;
                float elev = Mathf.slope(1f - leg.stage) * scl;
                Draw.color(Pal.shadow);
                Draw.rect(type.footRegion, leg.base.x + UnitType.shadowTX * elev, leg.base.y + UnitType.shadowTY * elev, position.angleTo(leg.base));
                Draw.color();
            }

            Draw.rect(type.footRegion, leg.base.x, leg.base.y, position.angleTo(leg.base));

            Lines.stroke(type.legRegion.height * Draw.scl * flips);
            Lines.line(type.legRegion, position.x, position.y, leg.joint.x, leg.joint.y, false);

            Lines.stroke(type.legBaseRegion.height * Draw.scl * flips);
            Lines.line(type.legBaseRegion, leg.joint.x + Tmp.v1.x, leg.joint.y + Tmp.v1.y, leg.base.x, leg.base.y, false);

            if (type.jointRegion.found()) {
                Draw.rect(type.jointRegion, leg.joint.x, leg.joint.y);
            }

            if (type.baseJointRegion.found()) {
                Draw.rect(type.baseJointRegion, position.x, position.y, rotation);
            }
        }

        if (type.baseRegion.found()) {
            Draw.rect(type.baseRegion, x, y, rotation - 90);
        }

        Draw.reset();
    }

    public void drawTrail(float x, float y) {
        updatePos(x, y);
        UnitType unitType = parent.unitType();
        float flyerRot = parent.unitRot();
        Tmp.v1.trns(parent.unitRot(), parent.time() * (2.0F + unitType.speed));
        for (int i2 = 0; i2 < 2; i2++) {
            Trail t = i2 == 0 ? tleft : tright;
            t.length = unitType.trailLength;
            int sign = i2 == 0 ? -1 : 1;
            float cx = Angles.trnsx(flyerRot - 90, unitType.trailX * sign, unitType.trailY) + x, cy = Angles.trnsy(flyerRot - 90, unitType.trailX * sign, unitType.trailY) + y;
            t.update(cx, cy, world.floorWorld(cx, cy).isLiquid ? 1 : 0);
        }
        float z = Draw.z();
        Draw.z(Layer.debris);
        Tile tileOn = world.tileWorld(x, y);
        Floor floor = tileOn == null ? Blocks.air.asFloor() : tileOn.floor();
        Color color = Tmp.c1.set(floor.mapColor.equals(Color.black) ? Blocks.water.mapColor : floor.mapColor).mul(1.5f);
        trailColor.lerp(color, Mathf.clamp(Time.delta * 0.04f));
        tleft.draw(trailColor, unitType.trailScl);
        tright.draw(trailColor, unitType.trailScl);
        Draw.z(z);
    }

    float legAngle(float rotation, int index) {
        return rotation + 360f / legs.length * index + (360f / legs.length / 2f);
    }

    void updateLegs() {
        UnitType type = parent.unitType();
        float rot = parent.unitRot();
        float legLength = type.legLength;

        //set up initial leg positions
        if (legs.length != type.legCount) {
            resetLegs();
        }

        float moveSpeed = type.legSpeed;
        int div = Math.max(legs.length / type.legGroupSize, 2);
        float moveSpace = legLength / 1.6f / (div / 2f) * type.legMoveSpace;
        totalLength += Mathf.dst(deltaX, deltaY);

        float trns = moveSpace * 0.85f * type.legTrns;

        //rotation + offset vector
        Vec2 moveOffset = Tmp.v4.trns(rot, trns);
        boolean moving = true;

        for (int i = 0; i < legs.length; i++) {
            float dstRot = legAngle(rot, i);
            Vec2 baseOffset = Tmp.v5.trns(dstRot, type.legBaseOffset).add(x, y);
            Leg l = legs[i];

            l.joint.sub(baseOffset).limit(type.maxStretch * legLength / 2f).add(baseOffset);
            l.base.sub(baseOffset).limit(type.maxStretch * legLength).add(baseOffset);

            float stageF = (totalLength + i * type.legPairOffset) / moveSpace;
            int stage = (int) stageF;
            int group = stage % div;
            boolean move = i % div == group;
            boolean side = i < legs.length / 2;
            //back legs have reversed directions
            boolean backLeg = Math.abs((i + 0.5f) - legs.length / 2f) <= 0.501f;
            if (backLeg && type.flipBackLegs) side = !side;

            l.moving = move;
            l.stage = moving ? stageF % 1f : Mathf.lerpDelta(l.stage, 0f, 0.1f);

            if (l.group != group) {

                //create effect when transitioning to a group it can't move in
                if (!move && i % div == l.group) {
                    Floor floor = Vars.world.floorWorld(l.base.x, l.base.y);
                    if (floor.isLiquid) {
                        floor.walkEffect.at(l.base.x, l.base.y, type.rippleScale, floor.mapColor);
                        floor.walkSound.at(x, y, 1f, floor.walkSoundVolume*0.60f);
                    } else {
                        Fx.unitLandSmall.at(l.base.x, l.base.y, type.rippleScale, floor.mapColor);
                    }

                    //shake when legs contact ground
                    if (type.landShake > 0) {
                        Effect.shake(type.landShake, type.landShake, l.base);
                    }
                }

                l.group = group;
            }

            //leg destination
            Vec2 legDest = Tmp.v1.trns(dstRot, legLength * type.legLengthScl).add(baseOffset).add(moveOffset);
            //join destination
            Vec2 jointDest = Tmp.v2;//.trns(rot2, legLength / 2f + type.legBaseOffset).add(moveOffset);
            InverseKinematics.solve(legLength / 2f, legLength / 2f, Tmp.v6.set(l.base).sub(baseOffset), side, jointDest);
            jointDest.add(baseOffset);
            //lerp between kinematic and linear
            jointDest.lerp(Tmp.v6.set(baseOffset).lerp(l.base, 0.5f), 1f - type.kinematicScl);

            if (move) {
                float moveFract = stageF % 1f;

                l.base.lerpDelta(legDest, moveFract);
                l.joint.lerpDelta(jointDest, moveFract / 2f);
            }

            l.joint.lerpDelta(jointDest, moveSpeed / 4f);
        }
    }

    public void update() {

    }
}
