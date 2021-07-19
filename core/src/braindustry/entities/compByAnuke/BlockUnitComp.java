package braindustry.entities.compByAnuke;

import arc.graphics.g2d.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;

import static mindustry.Vars.*;


@braindustry.annotations.ModAnnotations.Component
abstract class BlockUnitComp implements Unitc {

    @braindustry.annotations.ModAnnotations.Import
    Team team;

    @braindustry.annotations.ModAnnotations.ReadOnly
    transient Building tile;

    public void tile(Building tile) {
        this.tile = tile;
        // sets up block stats
        maxHealth(tile.block.health);
        health(tile.health());
        hitSize(tile.block.size * tilesize * 0.7f);
        set(tile);
    }

    @Override
    public void update() {
        if (tile != null) {
            team = tile.team;
        }
    }

    @braindustry.annotations.ModAnnotations.Replace
    @Override
    public TextureRegion icon() {
        return tile.block.fullIcon;
    }

    @Override
    public void killed() {
        tile.kill();
    }

    @braindustry.annotations.ModAnnotations.Replace
    public void damage(float v, boolean b) {
        tile.damage(v, b);
    }

    @braindustry.annotations.ModAnnotations.Replace
    public boolean dead() {
        return tile == null || tile.dead();
    }

    @braindustry.annotations.ModAnnotations.Replace
    public boolean isValid() {
        return tile != null && tile.isValid();
    }

    @braindustry.annotations.ModAnnotations.Replace
    public void team(Team team) {
        if (tile != null && this.team != team) {
            this.team = team;
            if (tile.team != team) {
                tile.team(team);
            }
        }
    }
}