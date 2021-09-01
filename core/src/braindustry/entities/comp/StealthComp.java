package braindustry.entities.comp;

import arc.Core;
import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Structs;
import arc.util.Time;
import arc.util.io.Writes;
import mma.annotations.ModAnnotations;
import braindustry.entities.ModUnits;
import braindustry.gen.BDCall;
import braindustry.gen.Stealthc;
import braindustry.input.ModBinding;
import braindustry.type.StealthUnitType;
import mindustry.Vars;
import mindustry.ai.formations.DistanceAssignmentStrategy;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationPattern;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;

@ModAnnotations.Component
public abstract class StealthComp implements Unitc, Entityc,Stealthc {
    public boolean inStealth = false;
    public float cooldownStealth = 0;
    public float durationStealth = 0;
    public transient boolean healing;
    public transient boolean longPress = false;
    public transient Interval timer=new Interval(10);
    private static final transient float stealthCheckDuration=12;
    transient  StealthUnitType stealthType;
    transient  boolean check = false, check2 = false;
    @ModAnnotations.Import UnitType type;
    @ModAnnotations.Import final Seq<Unit> units = new Seq();
    @ModAnnotations.Import float drag, maxHealth, armor, hitSize,health,x,y,rotation;
    @ModAnnotations.Import Seq<Ability> abilities;
    @ModAnnotations.Import boolean hovering;
    @ModAnnotations.Import UnitController controller;
    @ModAnnotations.Import Team team;
    @Override
    public void setType(UnitType type) {
        /*if (!(type instanceof StealthUnitType)) return;
        super.setType(type);
        type(type);*/
        if (!(type instanceof StealthUnitType)) {
            throw new RuntimeException("You can't place here non stealth unitType!!!");
//            return;
        }
        stealthType = (StealthUnitType)type;
    }

    public boolean selectStealth() {
        boolean bool;
        if (isLocal()) {
            bool = Core.input.keyTap(ModBinding.special_key);
            if (Vars.mobile) {

                if (!check &&longPress) {
                    check2 = true;
                    longPress = false;
                    return true;
                } else if(check &&!longPress){
                    check2 = false;
                    longPress = true;
                }
                return false;
            }
            return bool;
        }
        bool = mustHeal() && !isPlayer();
        if (bool && healing) {
            return inStealth;
        }
        return !inStealth && bool;
    }

    @Override
    public boolean healing() {
        return healing;
    }

    @Override
    public void healing(boolean healing) {
        this.healing = healing;
    }

    public boolean mustHeal() {
        boolean bool1 = health <= stealthType.minHealth;
        boolean bool2 = health > stealthType.maxHealth;
        if (!check && bool1) {
            check = true;
            return true;
        } else if (check && bool2) {
            check = false;
            return bool1;
        }
        return !bool2;
    }

    @Override
    public boolean inStealth() {
        return inStealth;
    }

    @Override
    public void inStealth(boolean inStealth) {
        this.inStealth = inStealth;
    }

    @Override
    public void longPress(boolean longPress) {
        this.longPress = longPress;
    }

    @Override
    public boolean longPress() {
        return longPress;
    }

    public void setStealth() {
        setStealth(0);
    }

    public void setStealth(float time) {
        if (!inStealth && cooldownStealth==0) {
            inStealth = true;
            BDCall.checkStealthStatus(Vars.player,self(),inStealth);
            durationStealth = 0;
        }
    }

    @Override
    public float stealthf() {
        if (inStealth){
            return 1f-durationStealth/stealthType.stealthDuration;
        }else {
            return 1f-cooldownStealth/stealthType.stealthCooldown;
        }
    }

    public void removeStealth() {
        removeStealth((durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
    }

    public void removeStealth(float time) {
        if (inStealth) {
            inStealth = false;
            BDCall.checkStealthStatus(Vars.player,self(),inStealth);
            cooldownStealth = Math.min(stealthType.stealthCooldown, time);
        }
    }
    @ModAnnotations.Replace
    public void commandNearby(FormationPattern pattern, Boolf<Unit> include) {

        Formation formation = new Formation(new Vec3(x, y, rotation), pattern);
        formation.slotAssignmentStrategy = new DistanceAssignmentStrategy(pattern);
        units.clear();
        ModUnits.nearby(team, x, y, 150.0F, (u)->{
            if (u.isAI() && include.get(u) && u != self() && u.type.flying == type.flying && u.hitSize <= hitSize * 1.1F) {
                units.add(u);
            }
        });
        if (units.isEmpty()) return;
        units.sort(Structs.comps(Structs.comparingFloat((u)->-u.hitSize), Structs.comparingFloat((u)->u.dst2(this))));
        units.truncate(type.commandLimit);
        command(formation, units);
    }
    @Override
    public void updateStealthStatus() {
        if (inStealth) {
            if(timer.get(0,stealthCheckDuration))BDCall.checkStealthStatus(Vars.player,self(),true);
            if (durationStealth >= stealthType.stealthDuration || selectStealth()) {
//                removeStealth((durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
                BDCall.setStealthStatus(self(),false,(durationStealth / stealthType.stealthDuration) * stealthType.stealthCooldown);
                Seq<Unit> Stealthc = controlling().select((u) -> u instanceof Stealthc);
                if (Stealthc.size > 0) {
                    Stealthc.each(unit -> {
                        BDCall.setStealthStatus(unit,false,-1);
                    });
                }
            }
        } else if (cooldownStealth == 0f && selectStealth()) {
            BDCall.setStealthStatus(self(),true,-1);
            Seq<Unit> Stealthc = controlling().select((u) -> u instanceof Stealthc);
            if (Stealthc.size > 0) {
                Stealthc.each(unit -> {
                    BDCall.setStealthStatus(unit,true,-1);
                });
            }
        }
    }

    public void drawAlpha() {
        Draw.alpha(getAlpha() * Draw.getColor().a);
    }

    public float getAlpha() {

        try {
            return !inStealth ? Draw.getColor().a : Vars.player.team() == team() ? 0.25f : 0f;
        } catch (NullPointerException exception) {
            return 0;
        }
    }
    @Override
    public void writeSync(Writes write) {
//        write.s(modClassId());
        if (!Groups.unit.contains(u -> u.equals(self()))) Groups.unit.add(self());
    }

    @Override
    public void draw() {
        stealthType.alpha = getAlpha();
    }

    @Override
    public void update() {
        if (!Groups.unit.contains(u -> u == self())) {
            updateLastPosition();
//            Groups.unit.tree().insert(this);
        }
        if (Vars.net.server() || isLocal()) {
            cooldownStealth = Math.max(0, cooldownStealth - Time.delta);
            if (inStealth) {
                durationStealth = Math.min(stealthType.stealthDuration, durationStealth + Time.delta);
                //            Groups.unit.remove(this);
            }
        }
        updateStealthStatus();

    }
}
