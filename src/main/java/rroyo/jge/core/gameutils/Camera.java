package rroyo.jge.core.gameutils;

/**
 * The {@code Camera} class manages the global viewpoint of the 2D game world.
 * It controls the coordinate offset (position) and the magnification level (zoom)
 * used by the rendering system to draw game elements relative to the screen.
 */
public final class Camera {

    /**
     * The current X and Y position of the camera in the game world.
     */
    private final Point position = new Point();

    /**
     * The current zoom scale of the camera. A value of 1 represents default scale.
     */
    private double zoom = 1;


    /** Creates a camera at the world origin with the default zoom level of {@code 1.0}. */
    public Camera() {}

    /**
     * Moves the camera horizontally by a specified delta.
     *
     * @param dx The amount to shift the camera on the X-axis.
     */
    public void moveX(int dx) {
        position.setX(position.getX() + dx);
    }

    /**
     * Moves the camera vertically by a specified delta.
     *
     * @param dy The amount to shift the camera on the Y-axis.
     */
    public void moveY(int dy) {
        position.setY(position.getY() + dy);
    }

    /**
     * Translates the camera by a specified amount on both the X and Y axes.
     *
     * @param dx The amount to shift the camera horizontally.
     * @param dy The amount to shift the camera vertically.
     */
    public void move(int dx, int dy) {
        moveX(dx);
        moveY(dy);
    }

    /**
     * Sets the absolute X coordinate of the camera's position.
     *
     * @param x The new X position.
     */
    public void setX(int x) {
        position.setX(x);
    }

    /**
     * Sets the absolute Y coordinate of the camera's position.
     *
     * @param y The new Y position.
     */
    public void setY(int y) {
        position.setY(y);
    }

    /**
     * Sets the absolute position of the camera in the game world.
     *
     * @param x The new X position.
     * @param y The new Y position.
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Centers the camera based on the provided dimensions.
     * Typically used to adjust the camera based on the window or screen size upon initialization.
     *
     * @param windowDimensions The {@link Dimension} representing the size of the view area.
     */
    public void setWindowDimensions(Dimension windowDimensions) {
        setPosition(
                (int) windowDimensions.getHalfWidth(),
                (int) windowDimensions.getHalfHeight()
        );
    }

    /**
     * Retrieves the current zoom level of the camera.
     *
     * @return The zoom multiplier.
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Sets a new zoom level for the camera. Prevents negative zoom values.
     *
     * @param zoom The new zoom multiplier.
     */
    public void setZoom(double zoom) {
        if (zoom <= 0) zoom = 0.01;
        this.zoom = zoom;
    }

    /**
     * Retrieves the reference to the camera's position point.
     *
     * @return The {@link Point} object representing the camera's coordinates.
     */
    public Point getCameraPosition() {
        return position;
    }

}
