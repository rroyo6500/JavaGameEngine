package rroyo.jge.core.gameobjects;

import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Overlap;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.core.gameutils.Velocity;
import rroyo.jge.interfaces.Script;

public abstract class Trigger extends GameObject {

    private final Script triggerScript = new TriggerScript();

    private final Overlap overlap = new Overlap(0, 0);

    private Velocity velocity = new Velocity();

    private final Point position;

    private final Dimension size;

    public Trigger(Point position, Dimension size) {
        this.position = position;
        this.size = size;
    }

    public abstract void onTrigger(GameElement triggeredElement);

    public final boolean trigger(GameElement triggeredElement) {
        return overlap(triggeredElement);
    }

    public final boolean trigger(Group triggeredGroup) {
        return overlap(triggeredGroup);
    }

    /**
     * Calculates the Axis-Aligned Bounding Box (AABB) intersection depth between this element and another.
     *
     * @param go The other {@link GameElement} to check against.
     * @return An {@link Overlap} object containing the X and Y intersection depths, or {@code null} if there is no collision.
     */
    private Overlap calculateOverlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return null;
        if (!this.inRange(go)) {
            return null;
        }

        // X
        double deltaX = Math.abs((int) this.getPosition().getX() - (int) go.getPosition().getX());
        double limitX = this.getSize().getHalfWidth() + go.getSize().getHalfWidth();
        double overlapX = limitX - deltaX;

        // Y
        double deltaY = Math.abs((int) this.getPosition().getY() - (int) go.getPosition().getY());
        double limitY = this.getSize().getHalfHeight() + go.getSize().getHalfHeight();
        double overlapY = limitY - deltaY;

        overlap.setOverlap(overlapX, overlapY);

        return overlap;
    }

    /**
     * Checks if this element overlaps with another element and triggers the {@code onOverlap} script events if they do.
     * Unlike a collision, an overlap check does not physically displace the elements.
     *
     * @param ge The other {@link GameElement} to check for overlap.
     * @return {@code true} if overlapping; {@code false} otherwise.
     */
    private boolean overlap(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        Overlap overlap = calculateOverlap(ge);
        if (overlap == null) return false;

        boolean isOverlap = overlap.getOverlapX() > 0 && overlap.getOverlapY() > 0;

        if (isOverlap) triggerScript.onOverlap(this, ge);

        return isOverlap;
    }

    /**
     * Checks if this element overlaps with any member of a specified {@link Group}.
     *
     * @param group The {@link Group} of elements to check.
     * @return {@code true} if overlapping with at least one member; {@code false} otherwise.
     */
    private boolean overlap(Group group) {
        if (isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this element is in a proximity range of another and triggers the {@code onInRange} script events.
     *
     * @param ge The other {@link GameElement}.
     * @return {@code true} if in range; {@code false} otherwise.
     */
    private boolean inRange(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        double secureRange = (this.getSize().getWidth() + this.getSize().getHeight()) +
                (ge.getSize().getWidth() + ge.getSize().getHeight());

        double deltaX = ge.getPosition().getX() - this.getPosition().getX();
        double deltaY = ge.getPosition().getY() - this.getPosition().getY();

        if (Math.abs(deltaX) > secureRange || Math.abs(deltaY) > secureRange) {
            return false;
        }

        double distance = (deltaX * deltaX) + (deltaY * deltaY);

        boolean ret =  distance <= (secureRange * secureRange);

        if (ret) triggerScript.onOverlap(this, ge);

        return ret;
    }

    /**
     * Checks if this element is in proximity range to any member of a specified {@link Group}.
     *
     * @param group The {@link Group} to check.
     * @return {@code true} if in range of at least one member; {@code false} otherwise.
     */
    private boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A hook to transform or scale the velocity before it is applied.
     * By default, returns the unmodified velocity value. Subclasses can override this to apply global modifiers (e.g., time scale).
     *
     * @param velocity The original velocity value.
     * @return The processed velocity value.
     */
    protected double transformVelocity(double velocity) {
        return velocity;
    }

    /**
     * Translates the element's position based on its current X and Y velocities.
     * Calls {@link #transformVelocity(double)} to process the values before moving.
     * If the element is marked for deletion, it does not move.
     */
    public final void move() {
        if (isDeleted()) return ;
        move(
                transformVelocity(velocity.getVelocityX()),
                transformVelocity(velocity.getVelocityY())
        );
    }

    /**
     * Manually translates the element's position by specific amounts.
     * If the element is marked for deletion, it does not move.
     *
     * @param dx The amount to shift on the X-axis.
     * @param dy The amount to shift on the Y-axis.
     */
    public final void move(double dx, double dy) {
        if (isDeleted()) return ;
        position.setPoint(
                position.getX() + dx,
                position.getY() + dy
        );
    }

    /**
     * Sets the velocity of the element on both axes.
     *
     * @param velocityX The new velocity on the X-axis.
     * @param velocityY The new velocity on the Y-axis.
     * @throws RuntimeException if an underlying IllegalAccessException occurs in the Velocity object.
     */
    public final void setVelocity(double velocityX, double velocityY) {
        try {
            this.getVelocity().setVelocityX(velocityX);
            this.getVelocity().setVelocityY(velocityY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the horizontal velocity of the element.
     *
     * @param velocityX The new velocity on the X-axis.
     * @throws RuntimeException if an underlying IllegalAccessException occurs in the Velocity object.
     */
    public final void setVelocityX (double velocityX) {
        if (isDeleted()) return;
        try {
            this.getVelocity().setVelocityX(velocityX);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the vertical velocity of the element.
     *
     * @param velocityY The new velocity on the Y-axis.
     * @throws RuntimeException if an underlying IllegalAccessException occurs in the Velocity object.
     */
    public final void setVelocityY (double velocityY) {
        if (isDeleted()) return;
        try {
            this.getVelocity().setVelocityY(velocityY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the current horizontal velocity.
     *
     * @return The velocity on the X-axis.
     */
    public final double getVelocityX() {
        return this.getVelocity().getVelocityX();
    }

    /**
     * Retrieves the current vertical velocity.
     *
     * @return The velocity on the Y-axis.
     */
    public final double getVelocityY() {
        return this.getVelocity().getVelocityY();
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Point getPosition() {
        return position;
    }

    public Dimension getSize() {
        return size;
    }
}

class TriggerScript implements Script {

    @Override
    public void start(GameObject self) {

    }

    @Override
    public void update(GameObject self) {

    }

    @Override
    public void onOverlap(GameObject self, GameObject objective) {
        Trigger trigger = (Trigger) self;

        trigger.onTrigger((GameElement) objective);

    }
}
