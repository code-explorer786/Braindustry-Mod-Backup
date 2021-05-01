package braindustry.gen;

import arc.func.Cons;
import arc.math.geom.Position;
import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import mindustry.entities.EntityCollisions;
import mindustry.gen.Entityc;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

final class NullFlying implements Flyingc {
  @Override
  @ModAnnotations.OverrideCallSuper
  public final <T> T as() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final <T extends Entityc> T self() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final <T> T with(Cons<T> arg0) {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void add() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void afterRead() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float angleTo(Position other) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float angleTo(float x, float y) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Block blockOn() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canDrown() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canPass(int arg0, int arg1) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canPassOn() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean checkTarget(boolean arg0, boolean arg1) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void clampHealth() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int classId() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean collides(Hitboxc arg0) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void collision(Hitboxc arg0, float arg1, float arg2) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void damage(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void damage(float arg0, boolean arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void damageContinuous(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void damageContinuousPierce(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void damagePierce(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void damagePierce(float arg0, boolean arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean damaged() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean dead() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void dead(boolean arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float deltaAngle() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float deltaLen() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float deltaX() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void deltaX(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float deltaY() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void deltaY(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float drag() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void drag(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float drownTime() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void drownTime(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float dst(Position other) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float dst(float x, float y) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float dst2(Position other) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float dst2(float x, float y) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float elevation() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void elevation(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Floor floorOn() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float floorSpeedMultiplier() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void getCollisions(Cons<QuadTree> arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float getX() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float getY() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void heal() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void heal(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void healFract(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float health() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void health(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float healthf() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float hitSize() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void hitSize(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float hitTime() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void hitTime(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void hitbox(Rect arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void hitboxTile(Rect arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean hovering() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void hovering(boolean arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int id() {
    return -1;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void id(int arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isAdded() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isFlying() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isGrounded() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isLocal() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isNull() {
    return true;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isRemote() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isValid() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void kill() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void killed() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void landed() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float lastX() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void lastX(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float lastY() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void lastY(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float maxHealth() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void maxHealth(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void move(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void moveAt(Vec2 arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean moving() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean onSolid() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void read(Reads arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void remove() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean serialize() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void set(Position arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void set(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final EntityCollisions.SolidPred solidity() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float splashTimer() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void splashTimer(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Tile tileOn() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int tileX() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int tileY() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void trns(Position arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void trns(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void update() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void updateLastPosition() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Vec2 vel() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean within(Position other, float dst) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean within(float x, float y, float dst) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void wobble() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void write(Writes arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float x() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void x(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float y() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void y(float arg0) {
  }
}
