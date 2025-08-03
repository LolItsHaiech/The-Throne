package client.render;

import com.almasb.fxgl.entity.Entity;
import util.Position;

public class RenderTile {
    private final Position position;
    private final Entity tileRender;
    private Entity treeRender;
    private Entity buildingRender;
    private Entity soldierEntity;
    private boolean hasFog;

    public RenderTile(Position position, Entity tileRender, Entity treeRender, Entity buildingRender, Entity soldierEntity, boolean hasFog) {
        this.position = position;
        this.tileRender = tileRender;
        this.treeRender = treeRender;
        this.buildingRender = buildingRender;
        this.soldierEntity = soldierEntity;
        this.hasFog = hasFog;
    }

    public Position getPosition() {
        return position;
    }

    public Entity getTileRender() {
        return tileRender;
    }

    public Entity getTreeRender() {
        return treeRender;
    }

    public void setTreeRender(Entity treeRender) {
        this.treeRender = treeRender;
    }

    public Entity getSoldierEntity() {
        return soldierEntity;
    }

    public void setSoldierEntity(Entity soldierEntity) {
        this.soldierEntity = soldierEntity;
    }

    public Entity getBuildingRender() {
        return buildingRender;
    }

    public void setBuildingRender(Entity buildingRender) {
        this.buildingRender = buildingRender;
    }

    public boolean hasFog() {
        return hasFog;
    }

    public void setHasFog(boolean hasFog) {
        this.hasFog = hasFog;
    }
}