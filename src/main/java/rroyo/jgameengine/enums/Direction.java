package rroyo.jgameengine.enums;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Direction getOposite() {
        return switch (this) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }

}
