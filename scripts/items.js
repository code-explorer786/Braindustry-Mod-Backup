//exometal
const exoAlloy = extendContent(Item, "exotic-alloy", {});
exoAlloy.localizedName = "Exo-Metal"
exoAlloy.description = "Heavy organic metal with high durability and low weight."
exoAlloy.cost = 3;
exoAlloy.alwaysUnlocked = true;
exoAlloy.explosiveness = 0.08;
exoAlloy.radioactivity = 0;
exoAlloy.flammability = 0.12;
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "surge-alloy")),exoAlloy, ItemStack.with());
//graphenite
const graphenite = extendContent(Item, "graphenite", {});
graphenite.localizedName = "Graphenite"
graphenite.description = "Basic material for high-tec structures, has a low mass and normal durability.Produces in Graphenite forge."
graphenite.cost = 2;
graphenite.alwaysUnlocked = true;
graphenite.explosiveness = 0;
graphenite.radioactivity = 0;
graphenite.flammability = 0;
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "graphite")),graphenite, ItemStack.with());
