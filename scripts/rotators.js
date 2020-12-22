const hyperPhase = extendContent (GenericCrafter,"hyper-phase-weaver",{});
hyperPhase.drawer = new DrawRotator();

const exoSmelter = extendContent (GenericSmelter,"exotic-alloy-smelter",{});
exoSmelter.drawer = new DrawWeave();
