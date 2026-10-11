package rroyo.jge.core.gameutils;

/**
 * The {@code Overlap} class is a data container used to store the amount of intersection depth
 * (penetration) between two colliding objects along the horizontal and vertical axes.
 *
 * @param overlapX The depth of the overlap along the X-axis.
 * @param overlapY The depth of the overlap along the Y-axis.
 */
public record Overlap(double overlapX, double overlapY) {

    /**
     * Constructs a new {@code Overlap} data object with specified values.
     *
     * @param overlapX The X-axis intersection depth.
     * @param overlapY The Y-axis intersection depth.
     */
    public Overlap {
    }

    /**
     * Retrieves the calculated overlap depth along the X-axis.
     *
     * @return The X overlap value.
     */
    @Override
    public double overlapX() {
        return overlapX;
    }

    /**
     * Retrieves the calculated overlap depth along the Y-axis.
     *
     * @return The Y overlap value.
     */
    @Override
    public double overlapY() {
        return overlapY;
    }

}
