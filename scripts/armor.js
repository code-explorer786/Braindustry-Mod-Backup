const armor = extendContent(UnitType, "armor", {
load() {
this.super$load();
}
});

armor.constructor = () => {
const unit = extend(UnitEntity, {
})
return unit
}
armor.abilities.add(new ForceFieldAbility(150, 5, 10000, 1200));
