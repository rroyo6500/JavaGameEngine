package rroyo.jge.core.gameobjects;

/**
 * The {@code Point} class represents a specific location in a 2D coordinate space.
 * It utilizes double precision to allow for sub-pixel accuracy, which is essential for smooth movement.
 */
public class Point {

    /**
     * The X coordinate (horizontal position).
     */
    private double x = 0;

    /**
     * The Y coordinate (vertical position).
     */
    private double y = 0;

    /**
     * Constructs a new {@code Point} initialized at the origin (0, 0).
     */
    public Point() {}

    /**
     * Constructs a new {@code Point} at the specified (x, y) coordinates.
     *
     * @param x The initial X coordinate.
     * @param y The initial Y coordinate.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the current X coordinate.
     *
     * @return The X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X coordinate using double precision.
     *
     * @param x The new X coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the X coordinate using an integer value.
     *
     * @param x The new X coordinate.
     */
    public void setX(int x) {
        setX((double) x);
    }

    /**
     * Retrieves the current Y coordinate.
     *
     * @return The Y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y coordinate using an integer value.
     *
     * @param y The new Y coordinate.
     */
    public void setY(int y) {
        setY((double) y);
    }

    /**
     * Sets the Y coordinate using double precision.
     *
     * @param y The new Y coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets both the X and Y coordinates simultaneously.
     *
     * @param x The new X coordinate.
     * @param y The new Y coordinate.
     */
    public void setPoint(double x, double y) {
        setX(x);
        setY(y);
    }

}
