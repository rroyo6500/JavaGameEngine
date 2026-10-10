package rroyo.jge.core.gameobjects;

import rroyo.jge.core.assets.Sprite;
import rroyo.jge.interfaces.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GameElement} class is the core representation of an interactive entity within the game world.
 * It extends {@link GameObject} and encapsulates position, dimensions, physics (velocity and collisions),
 * visual representation (sprites), and behavior (scripts).
 */
public class GameElement extends GameObject {

    /**
     * A list of {@link Script} behaviors attached to this element.
     */
    private final List<Script> scripts = new ArrayList<>();

    /**
     * A reusable {@link Rectangle} object representing the physical boundaries of the element.
     */
    private final Rectangle bounds = new Rectangle();

    /**
     * A reusable {@link Overlap} object used to store intersection penetration depths.
     */
    private final Overlap overlap = new Overlap(0, 0);

    /**
     * The physical velocity vector of the game element.
     */
    protected Velocity velocity = new Velocity();

    /**
     * The central position coordinate of the element in the game world.
     */
    protected Point point;

    /**
     * The physical dimensions (width and height) of the element.
     */
    protected Dimension dimension;

    /**
     * The visual asset (image or color) used to render the element.
     */
    protected Sprite sprite;

    /**
     * Constructs a new {@code GameElement} using primitive coordinates and dimensions.
     *
     * @param x      The X-coordinate of the element's center.
     * @param y      The Y-coordinate of the element's center.
     * @param width  The total width of the element.
     * @param height The total height of the element.
     * @param sprite The visual {@link Sprite} for the element.
     * @param script A variable number of {@link Script}s to attach for customized behavior.
     */
    public GameElement(double x, double y, double width, double height, Sprite sprite,
                       Script... script) {
        this(new Point(x, y), new Dimension(width, height), sprite, script);
    }

    /**
     * Constructs a new {@code GameElement} using object-based spatial properties.
     *
     * @param point     The {@link Point} representing the element's center.
     * @param dimension The {@link Dimension} representing the element's size.
     * @param sprite    The visual {@link Sprite} for the element.
     * @param script    A variable number of {@link Script}s to attach for customized behavior.
     */
    public GameElement(Point point, Dimension dimension, Sprite sprite,
                       Script... script) {
        this.point = point;
        this.dimension = dimension;
        this.sprite = sprite;
        for (Script s : script) {
            if (!scripts.contains(s))
                scripts.add(s);
        }
    }

    /**
     * Initializes the element by calling the {@code start} method on all attached scripts.
     * This should typically be called right after the element is instantiated or added to a scene.
     */
    public final void start() {
        for (Script s : scripts) {
            s.start(this);
        }
    }

    /**
     * Updates the element's state for the current frame by delegating to its attached scripts.
     */
    public final void update() {
        for (Script script : scripts) {
            script.update(this);
        }
    }

    /**
     * Calculates the Axis-Aligned Bounding Box (AABB) intersection depth between this element and another.
     *
     * @param go The other {@link GameElement} to check against.
     * @return An {@link Overlap} object containing the X and Y intersection depths, or {@code null} if there is no collision.
     */
    protected final Overlap calculateOverlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return null;
        if (!this.ir(go)) {
            return null;
        }

        // X
        double deltaX = Math.abs((int) this.getPoint().getX() - (int) go.getPoint().getX());
        double limitX = this.getDimension().getHalfWidth() + go.getDimension().getHalfWidth();
        double overlapX = limitX - deltaX;

        // Y
        double deltaY = Math.abs((int) this.getPoint().getY() - (int) go.getPoint().getY());
        double limitY = this.getDimension().getHalfHeight() + go.getDimension().getHalfHeight();
        double overlapY = limitY - deltaY;

        overlap.setOverlap(overlapX, overlapY);

        return overlap;
    }

    /**
     * Internal check to determine if the bounding boxes of this element and another are currently overlapping.
     *
     * @param ge The other {@link GameElement} to check.
     * @return {@code true} if they overlap; {@code false} otherwise.
     */
    protected final boolean ov(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        Overlap overlap = calculateOverlap(ge);
        if (overlap == null) return false;
        return overlap.getOverlapX() > 0 && overlap.getOverlapY() > 0;
    }

    /**
     * Checks if this element overlaps with another element and triggers the {@code onOverlap} script events if they do.
     * Unlike a collision, an overlap check does not physically displace the elements.
     *
     * @param ge The other {@link GameElement} to check for overlap.
     * @return {@code true} if overlapping; {@code false} otherwise.
     */
    public final boolean overlap(GameElement ge) {
        boolean isOverlap = ov(ge);
        if (isOverlap) {
            for (Script script : scripts) {
                script.onOverlap(this, ge);
            }
        }
        return isOverlap;
    }

    /**
     * Checks if this element overlaps with any member of a specified {@link Group}.
     *
     * @param group The {@link Group} of elements to check.
     * @return {@code true} if overlapping with at least one member; {@code false} otherwise.
     */
    public final boolean overlap(Group group) {
        if (isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the last calculated overlap data between this element and another.
     * Note: This returns the cached instance, which only reflects the most recent {@link #calculateOverlap(GameElement)} call.
     *
     * @param go The other {@link GameElement}.
     * @return The reusable {@link Overlap} object.
     */
    public final Overlap getOverlap(GameElement go) {
        return overlap;
    }

    /**
     * Internal optimized broad-phase check to see if another element is within a general secure distance,
     * quickly eliminating the need for complex AABB checks on distant objects.
     *
     * @param ge The other {@link GameElement}.
     * @return {@code true} if the elements are close enough to potentially interact; {@code false} otherwise.
     */
    protected final boolean ir(GameElement ge) {
        if (isDeleted() || ge.isDeleted()) return false;
        double secureRange = (this.getDimension().getWidth() + this.getDimension().getHeight()) +
                (ge.getDimension().getWidth() + ge.getDimension().getHeight());

        double deltaX = ge.getPoint().getX() - this.getPoint().getX();
        double deltaY = ge.getPoint().getY() - this.getPoint().getY();

        if (Math.abs(deltaX) > secureRange || Math.abs(deltaY) > secureRange) {
            return false;
        }

        double distance = (deltaX * deltaX) + (deltaY * deltaY);

        return distance <= (secureRange * secureRange);
    }

    /**
     * Checks if this element is in a proximity range of another and triggers the {@code onInRange} script events.
     *
     * @param ge The other {@link GameElement}.
     * @return {@code true} if in range; {@code false} otherwise.
     */
    public final boolean inRange(GameElement ge) {
        boolean ret = ir(ge);
        if (ret) {
            for (Script script : scripts) {
                script.onInRange(this, ge);
            }
        }
        return ret;
    }

    /**
     * Checks if this element is in proximity range to any member of a specified {@link Group}.
     *
     * @param group The {@link Group} to check.
     * @return {@code true} if in range of at least one member; {@code false} otherwise.
     */
    public final boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates a physical collision between this element and another. If they intersect,
     * this element is physically displaced along the axis of minimum penetration to separate them,
     * its velocity on that axis is halted, and the {@code onCollide} script events are triggered.
     *
     * @param gameElement The other {@link GameElement} to collide with.
     * @return {@code true} if a collision occurred and was resolved; {@code false} otherwise.
     */
    public final boolean collide(GameElement gameElement) {
        if (isDeleted() || gameElement.isDeleted()) return false;
        if (this.ov(gameElement)) {
            Overlap overlap = calculateOverlap(gameElement);
            if (overlap == null) return false;
            if (overlap.getOverlapX() < overlap.getOverlapY()) {
                if (this.getPoint().getX() < gameElement.getPoint().getX())
                    this.move(-overlap.getOverlapX(), 0);
                else
                    this.move(overlap.getOverlapX(), 0);
                this.setVelocityX(0);
            } else {
                if (this.getPoint().getY() < gameElement.getPoint().getY())
                    this.move(0, -overlap.getOverlapY());
                else
                    this.move(0, overlap.getOverlapY());
                this.setVelocityY(0);
            }

            for (Script script : scripts) {
                script.onCollide(this, gameElement);
            }

            return true;
        }
        return false;
    }

    /**
     * Performs a physical collision check and resolution against all members of a specified group.
     *
     * @param group The {@link Group} to collide against.
     * @return {@code true} if a collision occurred with any member; {@code false} otherwise.
     */
    public final boolean collide(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.collide(go)) {
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
        point.setPoint(
                point.getX() + dx,
                point.getY() + dy
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

    /**
     * Retrieves the positional coordinate object of the element.
     *
     * @return The {@link Point} representing the center of the element.
     */
    public final Point getPoint() {
        return point;
    }

    /**
     * Retrieves the visual sprite associated with this element.
     *
     * @return The {@link Sprite} used for rendering.
     */
    public final Sprite getSprite() {
        return sprite;
    }

    /**
     * Calculates and returns the geometric boundaries of the element based on its current position and dimensions.
     *
     * @return A {@link Rectangle} containing the absolute boundaries of the element.
     */
    public final Rectangle getBounds() {
        bounds.setBounds(
                (int) (point.getX() - this.getDimension().getHalfWidth()),
                (int) (point.getY() - this.getDimension().getHalfHeight()),
                (int) this.getDimension().getWidth(), (int) this.getDimension().getHeight()
        );
        return bounds;
    }

    /**
     * Retrieves the spatial dimensions of the element.
     *
     * @return The {@link Dimension} object of the element.
     */
    public final Dimension getDimension() {
        return dimension;
    }

    /**
     * Sets a new visual sprite for the element.
     * Will ignore null, closed sprites, or if the element is marked for deletion.
     *
     * @param sprite The new {@link Sprite} to apply.
     */
    public final void setSprite(Sprite sprite) {
        if (sprite == null || sprite.isClosed()) return;
        if (isDeleted()) return ;
        this.sprite = sprite;
    }

    /**
     * Retrieves the velocity data structure for the element.
     *
     * @return The {@link Velocity} vector object.
     */
    public final Velocity getVelocity() {
        return velocity;
    }

}
