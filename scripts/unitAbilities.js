const armor = extendContent(UnitType, "armor", {});
/*T1 unit*/
armor.constructor = function(){
  return extend(PayloadUnit, {});
};
armor.abilities.add(new ForceFieldAbility(30, 5, 100, 1200));

const shield = extendContent(UnitType, "shield", {});
/*T2 unit*/
shield.constructor = function(){
  return extend(PayloadUnit, {});
};
shield.abilities.add(new ForceFieldAbility(50, 6, 150, 1200));

const chestplate = extendContent(UnitType, "chestplate", {});
/*T3 unit*/
chestplate.constructor = function(){
  return extend(PayloadUnit, {});
};
chestplate.abilities.add(new ForceFieldAbility(70, 7, 220, 1200));

const chainmail = extendContent(UnitType, "chainmail", {});
/*T4 unit with heal field*/
chainmail.constructor = function(){
  return extend(PayloadUnit, {});
};
chainmail.abilities.add(new ForceFieldAbility(120, 7, 220, 1200), new RepairFieldAbility(90, 60, 140));

const broadsword = extendContent(UnitType, "broadsword", {});
/*T5 unit with heal field*/
broadsword.constructor = function(){
  return extend(PayloadUnit, {});
};
broadsword.abilities.add(new ForceFieldAbility(160, 8, 220, 1200), new RepairFieldAbility(120, 80, 210));
/////////////////////////FLYING REGION ENDS/////////////////////////
/////////////////////////NAVAL REGION STARTS/////////////////////////
const lyra = extendContent(UnitType, "lyra", {});
/*T2 Naval*/
lyra.constructor = function(){
  return extend(UnitWaterMove, {});
};
lyra.abilities.add(new StatusFieldAbility(StatusEffects.overclock, 60 * 8, 60 * 8, 70));

const tropsy = extendContent(UnitType, "tropsy", {});
/*T3 Naval*/
tropsy.constructor = function(){
  return extend(UnitWaterMove, {});
};
tropsy.abilities.add(new StatusFieldAbility(StatusEffects.overclock, 60 * 6, 60 *6, 60), new ShieldRegenFieldAbility(12, 35, 60 * 4, 60));

const cenda = extendContent(UnitType, "cenda", {});
/*T4 Naval*/
cenda.constructor = function(){
  return extend(UnitWaterMove, {});
};
cenda.abilities.add(new StatusFieldAbility(StatusEffects.overclock, 60 * 8, 60 *8, 60), new ShieldRegenFieldAbility(22, 45, 60 * 4, 60), new UnitSpawnAbilitycenda.abilities.add(new StatusFieldAbility(StatusEffects.overclock, 60 * 8, 60 *8, 60), new ShieldRegenFieldAbility(22, 45, 60 * 4, 60), new UnitSpawnAbility(armor, 2500, 19.25, -31.75));
