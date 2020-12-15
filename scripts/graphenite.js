const graphenite = extendContent(Item, "graphenite", {});
graphenite.localizedName = "Exo Metal"
graphenite.description = "Progressed synthetic metal with low weight."
graphenite.cost = 2;
graphenite.alwaysUnlocked = true;
graphenite.explosiveness = 0.08;
graphenite.radioactivity = 0;
graphenite.color = "e2f723";
graphenite.flammability = 0.12;
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "graphite")), graphenite, ItemStack.with());
