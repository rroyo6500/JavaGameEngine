package rroyo.jge.core.gameobjects.positionable;

import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.core.gameutils.Time;
import rroyo.jge.core.gameutils.Velocity;

import java.awt.*;

/**
 * Base game entity with a center position, dimensions, velocity, and integer AABB bounds.
 * Instances register themselves with the {@link PhysicsEngine} for their owning scene.
 * Movement through {@link #move()} is velocity-based and scaled by the current frame delta;
 * direct movement through {@link #move(double, double)} applies world-space offsets immediately.
 */
public class PositionableGameObject extends GameObject {

    /**
     * The physical velocity vector of the game element.
     */
    private final Velocity velocity = new Velocity();

    /**
     * The central position coordinate of the element in the game world.
     */
    private final Point position;

    /**
     * The physical dimensions (width and height) of the element.
     */
    private final Dimension size;

    /** Rotation in radians, applied around the object's center. */
    private double rotation;

    /**
     * A reusable {@link Rectangle} object representing the physical boundaries of the element.
     */
    private final Rectangle bounds = new Rectangle();

    /**
     * Constructs a new {@code PositionableGameObject} with the specified parameters
     *
     * @param x      The X-coordinate of the object's center.
     * @param y      The Y-coordinate of the object's center.
     * @param width  The total width of the object.
     * @param height The total height of the object.
     * @param scene scene that owns and updates this entity
     */
    public PositionableGameObject(Scene scene, double x, double y, double width, double height) {
        this(scene, new Point(x, y), new Dimension(width, height));
    }

    /**
     * Constructs a new {@code PositionableGameObject} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the element's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the element's size.
     * @param scene scene that owns and updates this entity
     */
    public PositionableGameObject(Scene scene, Point position, Dimension size) {
        super(scene);
        this.position = position;
        this.size = size;
        PhysicsEngine.add(this);
    }

    /**
     * A hook to transform or scale the velocity before it is applied.
     * By default, returns the modified velocity value.
     *
     * @param velocity The original velocity value.
     * @return The processed velocity value.
     */
    protected double transformVelocity(double velocity) {
        return velocity * Time.deltaTime();
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
     */
    public final void setVelocity(double velocityX, double velocityY) {
        this.getVelocity().setVelocityX(velocityX);
        this.getVelocity().setVelocityY(velocityY);
    }

    /**
     * Sets the horizontal velocity of the element.
     *
     * @param velocityX The new velocity on the X-axis.
     */
    public final void setVelocityX (double velocityX) {
        if (isDeleted()) return;
        this.getVelocity().setVelocityX(velocityX);
    }

    /**
     * Sets the vertical velocity of the element.
     *
     * @param velocityY The new velocity on the Y-axis.
     */
    public final void setVelocityY (double velocityY) {
        if (isDeleted()) return;
        this.getVelocity().setVelocityY(velocityY);
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
     * Retrieves the velocity data structure for the element.
     *
     * @return The {@link Velocity} vector object.
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Retrieves the positional coordinate object of the element.
     *
     * @return The {@link rroyo.jge.core.gameutils.Point} representing the center of the element.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Retrieves the spatial dimensions of the element.
     *
     * @return The {@link rroyo.jge.core.gameutils.Dimension} object of the element.
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Returns this object's rotation around its center, in radians.
     *
     * @return rotation in radians
     */
    public final double getRotation() {
        return rotation;
    }

    /**
     * Sets this object's rotation around its center, in radians. Positive values rotate
     * clockwise on screen, matching Java2D's coordinate system.
     *
     * @param rotation rotation in radians
     */
    public final void setRotation(double rotation) {
        if (Double.isFinite(rotation)) this.rotation = rotation;
    }

    /** Adds an angle to this object's rotation. The angle is in radians. */
    public final void rotate(double amount) {
        if (Double.isFinite(amount)) setRotation(rotation + amount);
    }

    /**
     * Calculates the axis-aligned bounding box enclosing the element's current rotated shape.
     *
     * @return A {@link Rectangle} containing the absolute boundaries of the element.
     */
    public Rectangle getBounds() {
        double cos = Math.abs(Math.cos(rotation));
        double sin = Math.abs(Math.sin(rotation));
        double halfWidth = cos * size.getHalfWidth() + sin * size.getHalfHeight();
        double halfHeight = sin * size.getHalfWidth() + cos * size.getHalfHeight();
        int minX = (int) Math.floor(position.getX() - halfWidth);
        int minY = (int) Math.floor(position.getY() - halfHeight);
        int maxX = (int) Math.ceil(position.getX() + halfWidth);
        int maxY = (int) Math.ceil(position.getY() + halfHeight);
        bounds.setBounds(
                minX, minY, maxX - minX, maxY - minY
        );
        return bounds;
    }



}
