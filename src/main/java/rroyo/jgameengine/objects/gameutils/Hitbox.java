package rroyo.jgameengine.objects.gameutils;

public class Hitbox {

    public double x, y;
    public double halfWidth, halfHeight;

    public Hitbox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHalfWidth() {
        return halfWidth;
    }

    public void setHalfWidth(double halfWidth) {
        this.halfWidth = halfWidth;
    }

    public double getHalfHeight() {
        return halfHeight;
    }

    public void setHalfHeight(double halfHeight) {
        this.halfHeight = halfHeight;
    }
}
