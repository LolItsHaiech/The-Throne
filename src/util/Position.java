package util;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {

    public double distanceTo(Position other) {
        return Math.sqrt((this.x - other.x)*(this.x - other.x) + (this.y - other.y)*(this.y - other.y)); // Manhattan distance
    }
}
