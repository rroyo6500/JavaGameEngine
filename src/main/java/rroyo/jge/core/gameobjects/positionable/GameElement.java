package rroyo.jge.core.gameobjects.positionable;

import rroyo.jge.core.assets.Sprite;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.interfaces.Event2d;
import rroyo.jge.interfaces.Script;

import java.awt.*;

/**
 * The {@code GameElement} class is the core representation of an interactive entity within the game world.
 * It extends {@link GameObject} and encapsulates position, dimensions, physics (velocity and collisions),
 * visual representation (sprites), and behavior (scripts).
 */
public class GameElement extends ScriptableGameObject implements Event2d {

    /**
     * The visual asset (image or color) used to render the element.
     */
    private Sprite sprite;

    /**
     * Constructs a new {@code GameElement} using primitive coordinates and dimensions.
     *
     * @param x      The X-coordinate of the element's center.
     * @param y      The Y-coordinate of the element's center.
     * @param width  The total width of the element.
     * @param height The total height of the element.
     * @param sprite The visual {@link Sprite} for the element.
     * @param scripts A variable number of {@link Script}s to attach for customized behavior.
     */
    public GameElement(Scene scene, double x, double y, double width, double height, Sprite sprite, Script... scripts) {
        super(scene, x, y, width, height, scripts);
        this.sprite = sprite;
    }

    /**
     * Constructs a new {@code GameElement} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the element's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the element's size.
     * @param sprite    The visual {@link Sprite} for the element.
     * @param scripts    A variable number of {@link Script}s to attach for customized behavior.
     */
    public GameElement(Scene scene, Point position, Dimension size, Sprite sprite, Script... scripts) {
        super(scene, position, size, scripts);
        this.sprite = sprite;
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
    public final void setSprite(Sprite sprite) {
        if (sprite == null || sprite.isClosed()) return;
        if (isDeleted()) return ;
        this.sprite = sprite;
    }

}
