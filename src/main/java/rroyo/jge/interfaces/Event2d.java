package rroyo.jge.interfaces;

import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;
import rroyo.jge.core.gameutils.Overlap;

/**
 * Defines the standard two-dimensional proximity, overlap, and collision operations for entities.
 * Implementations are expected to be positionable objects; each default method performs the
 * relevant geometry check and dispatches script callbacks when supported by the participants.
 */
public interface Event2d {

    /**
     * Checks if this element is in a proximity range of another and triggers the {@code onInRange} script events.
     *
     * @param gameObject The other {@link SpriteGameObject}.
     * @return {@code true} if in range; {@code false} otherwise.
     */
    default boolean inRange(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            boolean ret = Overlap.ir(pgo, gameObject);
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
        boolean ret = false;
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || group.isDeleted()) return false;
            for (PositionableGameObject go : group.getMembers()) {
                if (inRange(go)) ret = true;
            }
        }
        return ret;
    }

    /**
     * Checks if this element overlaps with another element and triggers the {@code onOverlap} script events if they do.
     * Unlike a collision, an overlap check does not physically displace the elements.
     *
     * @param gameObject The other {@link SpriteGameObject} to check for overlap.
     * @return {@code true} if overlapping; {@code false} otherwise.
     */
    default boolean overlap(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            boolean isOverlap = Overlap.ov(pgo, gameObject);
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
        boolean ret = false;
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || group.isDeleted()) return false;
            for (PositionableGameObject go : group.getMembers()) {
                if (overlap(go)) ret = true;
            }
        }
        return ret;
    }

    /**
     * Evaluates a physical collision between this element and another. If they intersect,
     * this element is displaced along the minimum-penetration normal to separate them,
     * its velocity component along that normal is halted, and the {@code onCollide} script events are triggered.
     *
     * @param gameObject The other {@link SpriteGameObject} to collide with.
     * @return {@code true} if a collision occurred and was resolved; {@code false} otherwise.
     */
    default boolean collide(PositionableGameObject gameObject) {
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || gameObject.isDeleted()) return false;
            if (Overlap.ov(pgo, gameObject)) {
                Overlap overlap = Overlap.calculateOverlap(pgo, gameObject);
                if (overlap == null) return false;
                pgo.move(overlap.normalX() * overlap.penetration(),
                        overlap.normalY() * overlap.penetration());
                // Remove the component of velocity directed along the collision normal.
                double normalVelocity = pgo.getVelocityX() * overlap.normalX()
                        + pgo.getVelocityY() * overlap.normalY();
                pgo.setVelocity(pgo.getVelocityX() - normalVelocity * overlap.normalX(),
                        pgo.getVelocityY() - normalVelocity * overlap.normalY());

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
        boolean ret = false;
        if (this instanceof PositionableGameObject pgo) {
            if (pgo.isDeleted() || group.isDeleted()) return false;
            for (PositionableGameObject go : group.getMembers()) {
                if (collide(go)) ret = true;
            }
        }
        return ret;
    }

}
