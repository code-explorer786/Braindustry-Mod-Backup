package braindustry.world.meta;

import arc.Core;
import arc.struct.Seq;
import java.util.Locale;
import braindustry.world.meta.AStatCat;

import mindustry.world.meta.Stat;

public enum AStat {
  maxConnections(AStatCat.function),

  recipes(AStatCat.function),

  gasCapacity(AStatCat.gasses),

  health,

  armor,

  size,

  displaySize,

  buildTime,

  buildCost,

  memoryCapacity,

  explosiveness,

  flammability,

  radioactivity,

  charge,

  heatCapacity,

  viscosity,

  temperature,

  flying,

  speed,

  buildSpeed,

  mineSpeed,

  mineTier,

  payloadCapacity,

  commandLimit,

  baseDeflectChance,

  lightningChance,

  lightningDamage,

  abilities,

  canBoost,

  maxUnits,

  damageMultiplier,

  healthMultiplier,

  speedMultiplier,

  reloadMultiplier,

  buildSpeedMultiplier,

  reactive,

  itemCapacity(AStatCat.items),

  itemsMoved(AStatCat.items),

  launchTime(AStatCat.items),

  maxConsecutive(AStatCat.items),

  liquidCapacity(AStatCat.liquids),

  powerCapacity(AStatCat.power),

  powerUse(AStatCat.power),

  powerDamage(AStatCat.power),

  powerRange(AStatCat.power),

  powerConnections(AStatCat.power),

  basePowerGeneration(AStatCat.power),

  tiles(AStatCat.crafting),

  input(AStatCat.crafting),

  output(AStatCat.crafting),

  productionTime(AStatCat.crafting),

  drillTier(AStatCat.crafting),

  drillSpeed(AStatCat.crafting),

  linkRange(AStatCat.crafting),

  instructions(AStatCat.crafting),

  weapons(AStatCat.function),

  bullet(AStatCat.function),

  speedIncrease(AStatCat.function),

  repairTime(AStatCat.function),

  range(AStatCat.function),

  shootRange(AStatCat.function),

  inaccuracy(AStatCat.function),

  shots(AStatCat.function),

  reload(AStatCat.function),

  powerShot(AStatCat.function),

  targetsAir(AStatCat.function),

  targetsGround(AStatCat.function),

  damage(AStatCat.function),

  ammo(AStatCat.function),

  ammoUse(AStatCat.function),

  shieldHealth(AStatCat.function),

  cooldownTime(AStatCat.function),

  booster(AStatCat.optional),

  boostEffect(AStatCat.optional),

  affinities(AStatCat.optional),

  opposites(AStatCat.optional);

  public final AStatCat category;

  AStat(AStatCat category) {

        this.category = category;
  }

  AStat() {

        this.category = AStatCat.general;
  }

  public String localized() {

        return Core.bundle.get("stat." + this.name().toLowerCase(Locale.ROOT));
  }

  public static AStat fromExist(Stat stat) {

        return Seq.with(values()).find((v)->v.name().equals(stat.name()));
  }
}
