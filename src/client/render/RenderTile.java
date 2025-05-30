package client.render;


import com.almasb.fxgl.entity.Entity;
import obj.map.Tile;
import util.Position;


public record RenderTile(Tile tile, Position position, Entity entity) {

}
