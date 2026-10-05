package rroyo.jgameengine.core.gameobjects;

public class Dimension {

    private double halfWidth = 0, halfHeight = 0;

    public Dimension(double width, double height) {
        this.halfWidth = width < 0 ? 0 : width / 2;
        this.halfHeight = height < 0 ? 0 : height / 2;
    }

    public double getHalfWidth() {
        return halfWidth;
    }

    public double getHalfHeight() {
        return halfHeight;
    }

    public double getWidth() {
        return halfWidth * 2;
    }

    public double getHeight() {
        return halfHeight * 2;
    }

    public void setWidth(double width) {
        if (width < 0) width = 0;
        this.halfWidth = width / 2;
    }

    public void setHeight(double height) {
        if (height < 0) height = 0;
        this.halfHeight = height / 2;
    }
}
