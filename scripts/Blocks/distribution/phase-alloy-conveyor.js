phaseAlloyConveyor = new ItemBridge("phase-alloy-conveyor"){{
requirements(Category.distribution, ItemStack.with(Items.braindustry-phase-alloy, 5, Items.silicon, 7, Items.titanium, 10, Items.braindustry-graphenite, 10));
range = 12;
canOverdrive = true;
hasPower = true;
consumes.power(0.30f);
}};