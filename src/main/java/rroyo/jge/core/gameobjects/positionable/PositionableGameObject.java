package rroyo.jge.core.gameobjects.positionable;

import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.core.gameutils.Time;
import rroyo.jge.core.gameutils.Velocity;

import java.awt.*;

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
     */
    public PositionableGameObject(Scene scene, double x, double y, double width, double height) {
        this(scene, new Point(x, y), new Dimension(width, height));
    }

    /**
     * Constructs a new {@code PositionableGameObject} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the element's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the element's size.
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
     * Calculates and returns the geometric boundaries of the element based on its current position and dimensions.
     *
     * @return A {@link Rectangle} containing the absolute boundaries of the element.
     */
    public Rectangle getBounds() {
        bounds.setBounds(
                (int) (position.getX() - this.getSize().getHalfWidth()),
                (int) (position.getY() - this.getSize().getHalfHeight()),
                (int) this.getSize().getWidth(), (int) this.getSize().getHeight()
        );
        return bounds;
    }



}
