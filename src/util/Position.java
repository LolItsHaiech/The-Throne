package util;

public record Position(int x, int y) {

    public int distanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y); // Manhattan distance
    }
}
