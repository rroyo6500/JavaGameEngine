package rroyo.jgameengine.objects.gameutils;

public class Point {

    private double x = 0;
    private double y = 0;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setX(int x) {
        setX((double) x);
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        setY((double) y);
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPoint(double x, double y) {
        setX(x);
        setY(y);
    }

}
