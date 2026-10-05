package rroyo.jgameengine.core.gameutils;

import rroyo.jgameengine.core.gameobjects.Dimension;
import rroyo.jgameengine.core.gameobjects.Point;

public final class Camera {

    private static final Point position = new Point();
    private static double zoom = 1;

    public static void moveX(int dx) {
        position.setX(position.getX() + dx);
    }

    public static void moveY(int dy) {
        position.setY(position.getY() + dy);
    }

    public static void move(int dx, int dy) {
        moveX(dx);
        moveY(dy);
    }

    public static void setX(int x) {
        position.setX(x);
    }

    public static void setY(int y) {
        position.setY(y);
    }

    public static void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public static void setWindowDimensions(Dimension windowDimensions) {
        move(
                (int) windowDimensions.getHalfWidth(),
                (int) windowDimensions.getHalfHeight()
        );
    }

    public static double getZoom() {
        return zoom;
    }

    public static void setZoom(double zoom) {
        if (zoom < 0) zoom = 0;
        Camera.zoom = zoom;
    }

    public static Point getCameraPosition() {
        return position;
    }

}
