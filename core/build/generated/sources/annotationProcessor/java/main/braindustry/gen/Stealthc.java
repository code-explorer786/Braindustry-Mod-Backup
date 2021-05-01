package braindustry.gen;

import braindustry.annotations.ModAnnotations;
import braindustry.type.StealthUnitType;
import braindustry.versions.ModEntityc;
import mindustry.type.UnitType;

/**
 * Interface for {@link braindustry.entities.comp.StealthComp}
 */
@ModAnnotations.EntityInterface
public interface Stealthc extends StealthUnitc, ModEntityc {
  void setOldType(UnitType type);

  void setType(UnitType type);

  boolean selectStealth();

  boolean healing();

  void healing(boolean healing);

  boolean mustHeal();

  boolean inStealth();

  void inStealth(boolean inStealth);

  void longPress(boolean longPress);

  boolean longPress();

  void setStealth();

  void setStealth(float time);

  float stealthf();

  void removeStealth();

  void removeStealth(float time);

  void drawAlpha();

  float getAlpha();

  void draw();

  void updateStealthStatus();

  void update();

  float cooldownStealth();

  void cooldownStealth(float cooldownStealth);

  float durationStealth();

  void durationStealth(float durationStealth);

  StealthUnitType stealthType();

  void stealthType(StealthUnitType stealthType);

  boolean check();

  void check(boolean check);

  boolean check2();

  void check2(boolean check2);
}
