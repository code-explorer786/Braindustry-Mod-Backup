const graphenIte = extendContent(Item, "graphenite", {});
graphenIte.localizedName = "Graphenite"
graphenIte.description = "Basic material for high-tec structures, has a low mass and normal durability.Produces in Graphenite forge."
graphenIte.cost = 2;
graphenIte.alwaysUnlocked = true;
graphenIte.explosiveness = 0;
graphenIte.radioactivity = 0;
graphenIte.flammability = 0;
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "graphite")),graphenite, ItemStack.with());
