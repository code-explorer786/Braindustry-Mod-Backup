package braindustry.gen;

import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Position;
import arc.math.geom.QuadTree;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.type.StealthUnitType;
import java.nio.FloatBuffer;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationPattern;
import mindustry.async.PhysicsProcess;
import mindustry.ctype.Content;
import mindustry.entities.EntityCollisions;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.BuildPlan;
import mindustry.entities.units.UnitController;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Entityc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

final class NullStealth implements Stealthc {
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
  public final Seq<Ability> abilities() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void abilities(Seq<Ability> arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean acceptsItem(Item arg0) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean activelyBuilding() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void add() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void addBuild(BuildPlan arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void addBuild(BuildPlan arg0, boolean arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void addItem(Item arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void addItem(Item arg0, int arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void afterRead() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void afterSync() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void aim(Position arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void aim(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void aimLook(Position arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void aimLook(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float aimX() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void aimX(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float aimY() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void aimY(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float ammo() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void ammo(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float ammof() {
    return 0;
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
  public final void apply(StatusEffect arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void apply(StatusEffect arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void approach(Vec2 arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float armor() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void armor(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Block blockOn() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float bounds() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final BuildPlan buildPlan() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float buildSpeedMultiplier() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canBuild() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canDrown() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canMine() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean canMine(Item arg0) {
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
  public final boolean canShoot() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int cap() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean cheating() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean check() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void check(boolean check) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean check2() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void check2(boolean check2) {
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
  public final void clearBuilding() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void clearCommand() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void clearItem() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void clearStatuses() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float clipSize() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Building closestCore() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Building closestEnemyCore() {
    return null;
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
  public final void command(Formation arg0, Seq<Unit> arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void commandNearby(FormationPattern arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void commandNearby(FormationPattern arg0, Boolf<Unit> arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void controlWeapons(boolean arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void controlWeapons(boolean arg0, boolean arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final UnitController controller() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void controller(UnitController arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Seq<Unit> controlling() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void controlling(Seq<Unit> arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float cooldownStealth() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void cooldownStealth(float cooldownStealth) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Building core() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int count() {
    return 0;
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
  public final float damageMultiplier() {
    return 0;
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
  public final void destroy() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean disarmed() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void display(Table arg0) {
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
  public final float dragMultiplier() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void draw() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void drawAlpha() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void drawBuildPlans() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void drawPlan(BuildPlan arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void drawPlanTop(BuildPlan arg0, float arg1) {
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
  public final float durationStealth() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void durationStealth(float durationStealth) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void eachGroup(Cons<Unit> arg0) {
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
  public final double flag() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void flag(double arg0) {
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
  public final Formation formation() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void formation(Formation arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float getAlpha() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void getCollisions(Cons<QuadTree> arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final String getControllerName() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Player getPlayer() {
    return null;
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
  public final boolean hasEffect(StatusEffect arg0) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean hasItem() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean hasWeapons() {
    return false;
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
  public final boolean healing() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void healing(boolean healing) {
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
  public final float healthMultiplier() {
    return 0;
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
  public final TextureRegion icon() {
    return null;
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
  public final void impulse(Vec2 arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void impulse(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void impulseNet(Vec2 arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean inRange(Position arg0) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean inStealth() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void inStealth(boolean inStealth) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void interpolate() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isAI() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isAdded() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isBoss() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isBuilding() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isCommanding() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isCounted() {
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
  public final boolean isImmune(StatusEffect arg0) {
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
  public final boolean isPlayer() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isRemote() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isRotate() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isShooting() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void isShooting(boolean arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean isValid() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Item item() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int itemCapacity() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float itemTime() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void itemTime(float arg0) {
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
  public final long lastUpdated() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void lastUpdated(long arg0) {
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
  public final boolean longPress() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void longPress(boolean longPress) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void lookAt(Position arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void lookAt(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void lookAt(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float mass() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int maxAccepted(Item arg0) {
    return 0;
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
  public final float minFormationSpeed() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void minFormationSpeed(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Tile mineTile() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void mineTile(Tile arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float mineTimer() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void mineTimer(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean mining() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int modClassId() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final WeaponMount[] mounts() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void mounts(WeaponMount[] arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void move(float arg0, float arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void moveAt(Vec2 arg0) {
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
  public final boolean mustHeal() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean offloadImmediately() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean onSolid() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final int pathType() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final PhysicsProcess.PhysicRef physref() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void physref(PhysicsProcess.PhysicRef arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Queue<BuildPlan> plans() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void plans(Queue<BuildPlan> arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float prefRotation() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float range() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void read(Reads arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void readSync(Reads arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void readSyncManual(FloatBuffer arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float realSpeed() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float reloadMultiplier() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void remove() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void removeBuild(int arg0, int arg1, boolean arg2) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void removeStealth() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void removeStealth(float time) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void resetController() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float rotation() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void rotation(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean selectStealth() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final double sense(Content arg0) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final double sense(LAccess arg0) {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Object senseObject(LAccess sensor) {
    return null;
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
  public final void set(UnitType arg0, UnitController arg1) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void setOldType(UnitType type) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void setStealth() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void setStealth(float time) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void setType(UnitType arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void setWeaponRotation(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void setupWeapons(UnitType arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float shield() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void shield(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float shieldAlpha() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void shieldAlpha(float arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean shouldSkip(BuildPlan arg0, Building arg1) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void snapInterpolation() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void snapSync() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final EntityCollisions.SolidPred solidity() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean spawnedByCore() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void spawnedByCore(boolean arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float speed() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float speedMultiplier() {
    return 0;
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
  public final ItemStack stack() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void stack(ItemStack arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Color statusColor() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final StealthUnitType stealthType() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void stealthType(StealthUnitType stealthType) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final float stealthf() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final Team team() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void team(Team arg0) {
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
  public final UnitType type() {
    return null;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void type(UnitType arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void unapply(StatusEffect arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void update() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean updateBuilding() {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void updateBuilding(boolean arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void updateLastPosition() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final long updateSpacing() {
    return 0;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void updateSpacing(long arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void updateStealthStatus() {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean validMine(Tile arg0) {
    return false;
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final boolean validMine(Tile arg0, boolean arg1) {
    return false;
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
  public final void writeSync(Writes arg0) {
  }

  @Override
  @ModAnnotations.OverrideCallSuper
  public final void writeSyncManual(FloatBuffer arg0) {
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
