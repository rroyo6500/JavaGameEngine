package rroyo.jge.interfaces;

import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameobjects.positionable.GameElement;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;
import rroyo.jge.core.gameutils.Overlap;

public interface Event2d {

    /**
     * Internal optimized broad-phase check to see if another element is within a general secure distance,
     * quickly eliminating the need for complex AABB checks on distant objects.
     *
     * @param gameObject The other {@link GameElement}.
     * @return {@code true} if the elements are close enough to potentially interact; {@code false} otherwise.
     */
    private boolean ir(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
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
     * Checks if this element is in a proximity range of another and triggers the {@code onInRange} script events.
     *
     * @param gameObject The other {@link GameElement}.
     * @return {@code true} if in range; {@code false} otherwise.
     */
    default boolean inRange(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            boolean ret = ir(gameObject);
            if (ret && pgo instanceof ScriptableGameObject sgo) {
                sgo.sendOnInRange(sgo, gameObject);
                if (gameObject instanceof ScriptableGameObject sge) {
                    sge.sendOnInRange(sge, sgo);
                }
            }
            return ret;
        }
        return false;
    }

    /**
     * Checks if this element is in proximity range to any member of a specified {@link Group}.
     *
     * @param group The {@link Group} to check.
     * @return {@code true} if in range of at least one member; {@code false} otherwise.
     */
    default boolean inRange(Group group) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || group.isDeleted()) return false;
            for (PositionableGameObject go : group.getMembers()) {
                if (inRange(go)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates the Axis-Aligned Bounding Box (AABB) intersection depth between this element and another.
     *
     * @param gameObject The other {@link GameElement} to check against.
     * @return An {@link Overlap} object containing the X and Y intersection depths, or {@code null} if there is no collision.
     */
    default Overlap calculateOverlap(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return null;
            if (!this.inRange(gameObject)) {
                return null;
            }

            // X
            double deltaX = Math.abs((int) pgo.getPosition().getX() - (int) gameObject.getPosition().getX());
            double limitX = pgo.getSize().getHalfWidth() + gameObject.getSize().getHalfWidth();
            double overlapX = limitX - deltaX;

            // Y
            double deltaY = Math.abs((int) pgo.getPosition().getY() - (int) gameObject.getPosition().getY());
            double limitY = pgo.getSize().getHalfHeight() + gameObject.getSize().getHalfHeight();
            double overlapY = limitY - deltaY;

            return new Overlap(overlapX, overlapY);
        }
        return null;
    }

    /**
     * Internal check to determine if the bounding boxes of this element and another are currently overlapping.
     *
     * @param gameObject The other {@link GameElement} to check.
     * @return {@code true} if they overlap; {@code false} otherwise.
     */
    private boolean ov(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            Overlap overlap = calculateOverlap(gameObject);
            if (overlap == null) return false;
            return overlap.overlapX() > 0 && overlap.overlapY() > 0;
        }
        return false;
    }

    /**
     * Checks if this element overlaps with another element and triggers the {@code onOverlap} script events if they do.
     * Unlike a collision, an overlap check does not physically displace the elements.
     *
     * @param gameObject The other {@link GameElement} to check for overlap.
     * @return {@code true} if overlapping; {@code false} otherwise.
     */
    default boolean overlap(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            boolean isOverlap = ov(gameObject);
            if (isOverlap && pgo instanceof ScriptableGameObject sgo) {
                sgo.sendOnOverlap(sgo, gameObject);
                if (gameObject instanceof ScriptableGameObject sge) {
                    sge.sendOnOverlap(sge, sgo);
                }
            }
            return isOverlap;
        }
        return false;
    }

    /**
     * Checks if this element overlaps with any member of a specified {@link Group}.
     *
     * @param group The {@link Group} of elements to check.
     * @return {@code true} if overlapping with at least one member; {@code false} otherwise.
     */
    default boolean overlap(Group group) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted()) return false;
            for (PositionableGameObject go : group.getMembers()) {
                if (overlap(go)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Evaluates a physical collision between this element and another. If they intersect,
     * this element is physically displaced along the axis of minimum penetration to separate them,
     * its velocity on that axis is halted, and the {@code onCollide} script events are triggered.
     *
     * @param gameObject The other {@link GameElement} to collide with.
     * @return {@code true} if a collision occurred and was resolved; {@code false} otherwise.
     */
    default boolean collide(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            if (ov(gameObject)) {
                Overlap overlap = calculateOverlap(gameObject);
                if (overlap == null) return false;
                if (overlap.overlapX() < overlap.overlapY()) {
                    if (pgo.getPosition().getX() < gameObject.getPosition().getX())
                        pgo.move(-overlap.overlapX(), 0);
                    else
                        pgo.move(overlap.overlapX(), 0);
                    pgo.setVelocityX(0);
                } else {
                    if (pgo.getPosition().getY() < gameObject.getPosition().getY())
                        pgo.move(0, -overlap.overlapY());
                    else
                        pgo.move(0, overlap.overlapY());
                    pgo.setVelocityY(0);
                }

                if (pgo instanceof ScriptableGameObject sgo) {
                    sgo.sendOnCollide(sgo, gameObject);
                    if (gameObject instanceof ScriptableGameObject sge) {
                        sge.sendOnCollide(sge, sgo);
                    }
                }

                return true;
            }
        }
        return false;
    }

    /**
     * Performs a physical collision check and resolution against all members of a specified group.
     *
     * @param group The {@link Group} to collide against.
     * @return {@code true} if a collision occurred with any member; {@code false} otherwise.
     */
    default boolean collide(Group group) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || group.isDeleted()) return false;
            for (PositionableGameObject go : group.getMembers()) {
                if (collide(go)) {
                    return true;
                }
            }
        }
        return false;
    }

}
