package client.render;

import com.almasb.fxgl.entity.Entity;
import obj.soldier.Soldier;
import util.Position;

import java.util.Objects;

public final class RenderSoldier {
    private final Soldier soldier;
    private Position position;
    private final Entity entity;

    public RenderSoldier(Soldier soldier, Position position, Entity entity) {
        this.soldier = soldier;
        this.position = position;
        this.entity = entity;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public Position getPosition() {
        return position;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

