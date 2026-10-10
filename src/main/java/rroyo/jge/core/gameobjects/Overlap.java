package rroyo.jge.core.gameobjects;

/**
 * The {@code Overlap} class is a data container used to store the amount of intersection depth
 * (penetration) between two colliding objects along the horizontal and vertical axes.
 */
public class Overlap {

    /**
     * The depth of the overlap along the X-axis.
     */
    private double overlapX;

    /**
     * The depth of the overlap along the Y-axis.
     */
    private double overlapY;

    /**
     * Constructs a new {@code Overlap} data object with specified values.
     *
     * @param overlapX The X-axis intersection depth.
     * @param overlapY The Y-axis intersection depth.
     */
    public Overlap(double overlapX, double overlapY) {
        this.overlapX = overlapX;
        this.overlapY = overlapY;
    }

    /**
     * Retrieves the calculated overlap depth along the X-axis.
     *
     * @return The X overlap value.
     */
    public double getOverlapX() {
        return overlapX;
    }

    /**
     * Retrieves the calculated overlap depth along the Y-axis.
     *
     * @return The Y overlap value.
     */
    public double getOverlapY() {
        return overlapY;
    }

    /**
     * Updates the intersection depth values for both axes.
     * Used primarily to recycle an existing {@code Overlap} instance and avoid garbage collection overhead.
     *
     * @param overlapX The new X-axis overlap depth.
     * @param overlapY The new Y-axis overlap depth.
     */
    public void setOverlap(double overlapX, double overlapY) {
        this.overlapX = overlapX;
        this.overlapY = overlapY;
    }

}
