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

