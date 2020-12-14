const exoAlloy = extendContent(Item, "exotic-alloy", {});
exoAlloy.localizedName = "Exo Metal"
exoAlloy.description = "Heavy organic metal with high durability and low weight."
exoAlloy.cost = 3;
exoAlloy.alwaysUnlocked = true;
exoAlloy.explosiveness = 0.08;
exoAlloy.radioactivity = 0;
exoticAlloy.color = "e2f723";
exoAlloy.flammability = 0.12;
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "surge-alloy")),exoAlloy, ItemStack.with());
//graphenite
const graphenIte = extendContent(Item, "graphenite", {});
graphenIte.localizedName = "Graphenite"
graphenIte.description = "Basic material for high-tec structures, has a low mass and normal durability.Produces in Graphenite forge."
graphenIte.cost = 2;
graphenIte.alwaysUnlocked = true;
graphenIte.explosiveness = 0;
graphenIte.radioactivity = 0;
graphenIte.flammability = 0;
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "graphite")),graphenite, ItemStack.with());

