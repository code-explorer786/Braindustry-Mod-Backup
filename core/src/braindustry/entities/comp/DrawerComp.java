package braindustry.entities.comp;


import braindustry.entities.BuilderDrawer;
import braindustry.gen.Drawer;
import braindustry.gen.Drawerc;
import mindustry.gen.Drawc;
import mindustry.gen.Entityc;
import mindustry.gen.Posc;
import mma.annotations.ModAnnotations;

@ModAnnotations.EntityDef(value = {Drawerc.class})
@ModAnnotations.Component(base = true)
abstract class DrawerComp implements Drawc, Posc, Entityc {
    public BuilderDrawer build;

    public static Drawer create(BuilderDrawer build) {
        Drawer drawer = Drawer.create();
        drawer.build = build;
        return drawer;
    }

    @Override
    public void draw() {
        if (build != null) build.drawer();

    }

    @Override
    public void update() {
        if (build == null || !build.isValid()) {
            remove();
        }
    }

    @Override
    public void remove() {
        build = null;
    }

    @Override
    @ModAnnotations.Replace
    public float clipSize() {

        return build.block().size * 8.0F;
    }
}
