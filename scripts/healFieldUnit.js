const chainmail = extendContent(UnitType, "chainmail", {});
/*T1 unit*/
chainmail.constructor = () => {
const unit = extend(UnitEntity, {
})
return unit
}
chainmail.abilities.add(new ForceFieldAbility(140, 4, 7000, 60), new RepairFieldAbility(130, 60, 140));
