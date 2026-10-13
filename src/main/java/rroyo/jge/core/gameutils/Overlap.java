package rroyo.jge.core.gameutils;

import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;

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

    /**
     * Internal optimized broad-phase check to see if another element is within a general secure distance,
     * quickly eliminating the need for complex AABB checks on distant objects.
     *
     * @param gameObject The other {@link SpriteGameObject}.
     * @return {@code true} if the elements are close enough to potentially interact; {@code false} otherwise.
     */
    public static boolean ir(GameObject self, PositionableGameObject gameObject) {
        if (self instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            double secureRange = (pgo.getSize().getWidth() + pgo.getSize().getHeight()) +
                    (gameObject.getSize().getWidth() + gameObject.getSize().getHeight());

            double deltaX = gameObject.getPosition().getX() - pgo.getPosition().getX();
            double deltaY = gameObject.getPosition().getY() - pgo.getPosition().getY();

            if (Math.abs(deltaX) > secureRange || Math.abs(deltaY) > secureRange) {
                return false;
            }

            double distance = (deltaX * deltaX) + (deltaY * deltaY);

            return distance <= (secureRange * secureRange);
        }
        return false;
    }

    /**
     * Calculates the Axis-Aligned Bounding Box (AABB) intersection depth between this element and another.
     *
     * @param gameObject The other {@link SpriteGameObject} to check against.
     * @return An {@link Overlap} object containing the X and Y intersection depths, or {@code null} if there is no collision.
     */
    public static Overlap calculateOverlap(GameObject self, PositionableGameObject gameObject) {
        if (self instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return null;
            if (!ir(self, gameObject)) {
                return null;
            }

            // X
            double deltaX = Math.abs(pgo.getPosition().getX() - gameObject.getPosition().getX());
            double limitX = pgo.getSize().getHalfWidth() + gameObject.getSize().getHalfWidth();
            double overlapX = limitX - deltaX;

            // Y
            double deltaY = Math.abs(pgo.getPosition().getY() - gameObject.getPosition().getY());
            double limitY = pgo.getSize().getHalfHeight() + gameObject.getSize().getHalfHeight();
            double overlapY = limitY - deltaY;

            if (overlapX <= 0 || overlapY <= 0) return null;

            return new Overlap(overlapX, overlapY);
        }
        return null;
    }

    /**
     * Internal check to determine if the bounding boxes of this element and another are currently overlapping.
     *
     * @param gameObject The other {@link SpriteGameObject} to check.
     * @return {@code true} if they overlap; {@code false} otherwise.
     */
    public static boolean ov(GameObject self, PositionableGameObject gameObject) {
        if (self instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            Overlap overlap = calculateOverlap(self, gameObject);
            if (overlap == null) return false;
            return overlap.overlapX() > 0 && overlap.overlapY() > 0;
        }
        return false;
    }

}
