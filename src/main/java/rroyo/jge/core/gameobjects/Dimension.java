package rroyo.jge.core.gameobjects;

/**
 * The {@code Dimension} class represents the 2D size (width and height) of a game object.
 * To optimize position calculations (which are often center-based), it internally stores
 * the dimensions as half-width and half-height. Negative dimensions are constrained to zero.
 */
public class Dimension {

    /**
     * Half of the total width of the dimension.
     */
    private double halfWidth = 0;

    /**
     * Half of the total height of the dimension.
     */
    private double halfHeight = 0;

    /**
     * Constructs a new {@code Dimension} with the specified width and height.
     * If a negative value is provided, it is automatically set to 0.
     *
     * @param width  The total width of the dimension.
     * @param height The total height of the dimension.
     */
    public Dimension(double width, double height) {
        this.halfWidth = width < 0 ? 0 : width / 2;
        this.halfHeight = height < 0 ? 0 : height / 2;
    }

    /**
     * Retrieves the half-width of this dimension.
     *
     * @return Half of the total width.
     */
    public double getHalfWidth() {
        return halfWidth;
    }

    /**
     * Retrieves the half-height of this dimension.
     *
     * @return Half of the total height.
     */
    public double getHalfHeight() {
        return halfHeight;
    }

    /**
     * Retrieves the total width of this dimension.
     *
     * @return The total width.
     */
    public double getWidth() {
        return halfWidth * 2;
    }

    /**
     * Retrieves the total height of this dimension.
     *
     * @return The total height.
     */
    public double getHeight() {
        return halfHeight * 2;
    }

    /**
     * Sets a new total width for this dimension.
     * If a negative value is provided, it is constrained to 0.
     *
     * @param width The new total width.
     */
    public void setWidth(double width) {
        if (width < 0) width = 0;
        this.halfWidth = width / 2;
    }

    /**
     * Sets a new total height for this dimension.
     * If a negative value is provided, it is constrained to 0.
     *
     * @param height The new total height.
     */
    public void setHeight(double height) {
        if (height < 0) height = 0;
        this.halfHeight = height / 2;
    }
}
