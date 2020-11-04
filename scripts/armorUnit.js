const armor = extendContent(UnitType, "armor", {});
/*T1 unit*/
armor.constructor = () => {
const unit = extend(UnitEntity, {
})
return unit
}
armor.abilities.add(new ForceFieldAbility(75, 5, 100, 1200));

const shield = extendContent(UnitType, "shield", {});
/*T2 unit*/
shield.constructor = () => {
const unit = extend(UnitEntity, {
})
return unit
}
shield.abilities.add(new ForceFieldAbility(120, 6, 150, 1200));

const chestplate = extendContent(UnitType, "chestplate", {});
/*T3 unit*/
chestplate.constructor = () => {
const unit = extend(UnitEntity, {
})
return unit
}
chestplate.abilities.add(new ForceFieldAbility(130, 7, 220, 1200));
