package rroyo.jge.core.gameobjects.positionable;

import org.jetbrains.annotations.NotNull;
import rroyo.jge.core.assets.Sprite;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.interfaces.Event2d;
import rroyo.jge.interfaces.Script;

import java.util.Objects;

/**
 * The {@code GameElement} class is the core representation of an interactive entity within the game world.
 * It extends {@link GameObject} and encapsulates position, dimensions, physics (velocity and collisions),
 * visual representation (sprites), and behavior (scripts).
 */
public class SpriteGameObject extends ScriptableGameObject implements Event2d {

    /**
     * The visual asset (image or color) used to render the element.
     */
    private Sprite sprite;

    /**
     * The width of the sprite in world units, clamped to the total width of the game object.
     */
    private double spriteWidth;

    /**
     * The effective height of the sprite in world units, clamped to the object's current height.
     */
    private double spriteHeight;

    /**
     * Constructs a new {@code GameElement} using primitive coordinates and dimensions.
     *
     * @param x      The X-coordinate of the element's center.
     * @param y      The Y-coordinate of the element's center.
     * @param width  The total width of the element.
     * @param height The total height of the element.
     * @param sprite The visual {@link Sprite} for the element.
     * @param scripts A variable number of {@link Script}s to attach for customized behavior.
     * @param scene scene that owns and updates this entity
     */
    public SpriteGameObject(Scene scene, double x, double y, double width, double height, @NotNull Sprite sprite, Script... scripts) {
        super(scene, x, y, width, height, scripts);
        Objects.requireNonNull(sprite);
        this.sprite = sprite;
        initializeSpriteSize();
    }

    /**
     * Constructs a new {@code GameElement} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the element's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the element's size.
     * @param sprite    The visual {@link Sprite} for the element.
     * @param scripts    A variable number of {@link Script}s to attach for customized behavior.
     * @param scene scene that owns and updates this entity
     */
    public SpriteGameObject(Scene scene, Point position, Dimension size, @NotNull Sprite sprite, Script... scripts) {
        super(scene, position, size, scripts);
        Objects.requireNonNull(sprite);
        this.sprite = sprite;
        initializeSpriteSize();
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
     * Sets a new visual sprite for the element.
     * Will ignore null, closed sprites, or if the element is marked for deletion.
     *
     * @param sprite The new {@link Sprite} to apply.
     */
    public final void setSprite(@NotNull Sprite sprite) {
        Objects.requireNonNull(sprite);
        if (sprite.isClosed()) return;
        if (isDeleted()) return ;
        this.sprite = sprite;
    }

    /**
     * Returns the image dimensions in world units. The returned value is a copy, and each
     * dimension is limited to the current size of this object.
     *
     * @return the effective sprite size
     */
    public final Dimension getSpriteSize() {
        return new Dimension(getSpriteWidth(), getSpriteHeight());
    }

    /**
     * Sets the image dimensions in world units. Each dimension is clamped to the corresponding
     * object dimension; the image is rendered centered on the object.
     *
     * @param width requested sprite width
     * @param height requested sprite height
     */
    public final void setSpriteSize(double width, double height) {
        if (isDeleted()) return;
        spriteWidth = clampToObject(width, getSize().getWidth());
        spriteHeight = clampToObject(height, getSize().getHeight());
    }

    /**
     * Sets the image dimensions in world units using a {@link Dimension} object.
     * Each dimension is clamped to the corresponding object dimension, and
     * the image is rendered centered on the object.
     *
     * @param size The {@link Dimension} representing the requested sprite width and height.
     */
    public final void setSpriteSize(@NotNull Dimension size) {
        Objects.requireNonNull(size);
        setSpriteSize(size.getWidth(), size.getHeight());
    }

    /**
     * Returns the effective sprite width, clamped to this object's current width.
     *
     * @return The effective width of the sprite.
     */
    public final double getSpriteWidth() {
        return Math.min(spriteWidth, getSize().getWidth());
    }

    /**
     * Returns the effective sprite height, clamped to this object's current height.
     *
     * @return The effective height of the sprite.
     */
    public final double getSpriteHeight() {
        return Math.min(spriteHeight, getSize().getHeight());
    }

    /**
     * Initializes the sprite dimensions based on the current size of the game object.
     * Sets both the sprite width and height to match the object's total width and height.
     */
    private void initializeSpriteSize() {
        spriteWidth = getSize().getWidth();
        spriteHeight = getSize().getHeight();
    }

    /**
     * Clamps the requested size to be between zero and the specified object size.
     * Returns 0 if the requested size is NaN.
     *
     * @param requestedSize the size to be clamped
     * @param objectSize the maximum allowable size
     * @return the clamped size, or 0 if the requested size was NaN
     */
    private static double clampToObject(double requestedSize, double objectSize) {
        if (Double.isNaN(requestedSize)) return 0;
        return Math.min(Math.max(0, requestedSize), objectSize);
    }

}
