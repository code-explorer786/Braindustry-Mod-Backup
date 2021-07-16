package braindustry.entities.compByAnuke;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ChildComp implements Posc{
    @braindustry.annotations.ModAnnotations.Import float x, y;

    @Nullable Posc parent;
    float offsetX, offsetY;

    @Override
    public void add(){
        if(parent != null){
            offsetX = x - parent.getX();
            offsetY = y - parent.getY();
        }
    }

    @Override
    public void update(){
        if(parent != null){
            x = parent.getX() + offsetX;
            y = parent.getY() + offsetY;
        }
    }
}
