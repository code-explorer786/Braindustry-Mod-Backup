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
