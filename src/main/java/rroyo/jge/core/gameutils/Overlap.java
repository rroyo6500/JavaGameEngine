package rroyo.jge.core.gameutils;

import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;

/**
 * The {@code Overlap} record stores the enclosing AABB overlap and the minimum-translation
 * normal and depth for two intersecting oriented rectangles.
 *
 * @param overlapX The enclosing AABB overlap depth along the X-axis.
 * @param overlapY The enclosing AABB overlap depth along the Y-axis.
 * @param normalX  X component of the minimum-penetration normal, pointing away from the other object.
 * @param normalY  Y component of the minimum-penetration normal, pointing away from the other object.
 * @param penetration minimum penetration depth along that normal
 */
public record Overlap(double overlapX, double overlapY, double normalX, double normalY,
                      double penetration) {

    /**
     * Constructs a new {@code Overlap} data object with specified values.
     *
     * @param overlapX The X-axis intersection depth.
     * @param overlapY The Y-axis intersection depth.
     */
    public Overlap {
    }

    /** Keeps source compatibility for callers that construct legacy axis-aligned overlaps. */
    public Overlap(double overlapX, double overlapY) {
        this(overlapX, overlapY, 0, 0, Math.min(overlapX, overlapY));
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
     * @param self object whose distance from the other entity is tested
     * @param gameObject other positionable object used in the proximity check
     * @return {@code true} if the elements are close enough to potentially interact; {@code false} otherwise.
     */
    public static boolean ir(GameObject self, PositionableGameObject gameObject) {
        if (self instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;

            double dx = pgo.getPosition().getX() - gameObject.getPosition().getX();
            double dy = pgo.getPosition().getY() - gameObject.getPosition().getY();
            double distanceSq = dx * dx + dy * dy;

            double radiusX = (pgo.getSize().getWidth() + gameObject.getSize().getWidth());
            double radiusY = (pgo.getSize().getHeight() + gameObject.getSize().getHeight());

            double maxRadius = Math.max(radiusX, radiusY);
            return distanceSq <= (maxRadius * maxRadius * 1.5);
        }
        return false;
    }

    /**
     * Calculates the intersection between two center-pivoted oriented rectangles using SAT.
     *
     * @param self object whose bounds are the first half of the comparison
     * @param gameObject other positionable object whose bounds are compared
     * @return the AABB overlap and SAT separation data, or {@code null} when the rectangles do not intersect
     */
    public static Overlap calculateOverlap(GameObject self, PositionableGameObject gameObject) {
        if (self instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return null;
            double dx = pgo.getPosition().getX() - gameObject.getPosition().getX();
            double dy = pgo.getPosition().getY() - gameObject.getPosition().getY();
            double minPenetration = Double.POSITIVE_INFINITY;
            double normalX = 0;
            double normalY = 0;

            // The two local axes from each rectangle are the four separating axes.
            double[] angles = {pgo.getRotation(), pgo.getRotation() + Math.PI / 2,
                    gameObject.getRotation(), gameObject.getRotation() + Math.PI / 2};
            for (double angle : angles) {
                double axisX = Math.cos(angle);
                double axisY = Math.sin(angle);
                double distance = Math.abs(dx * axisX + dy * axisY);
                double radiusA = projectedRadius(pgo, axisX, axisY);
                double radiusB = projectedRadius(gameObject, axisX, axisY);
                double penetration = radiusA + radiusB - distance;
                if (penetration <= 1e-10) return null;
                if (penetration < minPenetration) {
                    minPenetration = penetration;
                    double direction = dx * axisX + dy * axisY;
                    if (direction < 0) {
                        normalX = -axisX;
                        normalY = -axisY;
                    } else {
                        normalX = axisX;
                        normalY = axisY;
                    }
                }
            }

            // Preserve the legacy X/Y AABB penetration values. The SAT normal and depth
            // carry the exact information needed to resolve an oriented-rectangle collision.
            double extentAX = projectedRadius(pgo, 1, 0);
            double extentAY = projectedRadius(pgo, 0, 1);
            double extentBX = projectedRadius(gameObject, 1, 0);
            double extentBY = projectedRadius(gameObject, 0, 1);
            double overlapX = extentAX + extentBX - Math.abs(dx);
            double overlapY = extentAY + extentBY - Math.abs(dy);
            return new Overlap(overlapX, overlapY, normalX, normalY, minPenetration);
        }
        return null;
    }

    /** Projects the half-extents of an oriented rectangle onto a unit axis. */
    private static double projectedRadius(PositionableGameObject object, double axisX, double axisY) {
        double angle = object.getRotation();
        double localXx = Math.cos(angle);
        double localXy = Math.sin(angle);
        double localYx = -localXy;
        double localYy = localXx;
        return object.getSize().getHalfWidth() * Math.abs(axisX * localXx + axisY * localXy)
                + object.getSize().getHalfHeight() * Math.abs(axisX * localYx + axisY * localYy);
    }

    /**
     * Checks whether this element and another oriented rectangle currently intersect.
     *
     * @param gameObject The other {@link SpriteGameObject} to check.
     * @param self object whose bounds are the first half of the comparison
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
