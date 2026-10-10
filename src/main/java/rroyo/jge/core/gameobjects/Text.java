package rroyo.jge.core.gameobjects;

import java.awt.*;

/**
 * The {@code Text} class represents a renderable text string in the game world.
 * It extends {@link GameObject} and holds the character string to display, its position,
 * and the specific color to draw it with.
 */
public class Text extends GameObject {

    /**
     * The positional coordinate of where the text will be drawn.
     */
    private final Point point;

    /**
     * The actual text string content to be displayed.
     */
    private String text;

    /**
     * The color applied to the text.
     */
    private Color foreground;

    /**
     * Constructs a new {@code Text} element using explicit x and y coordinates.
     *
     * @param text The string content to render.
     * @param x    The horizontal X position to start rendering the text.
     * @param y    The vertical Y position to render the text.
     */
    public Text(String text, double x, double y) {
        this(text, new Point(x, y));
    }

    /**
     * Constructs a new {@code Text} element using a coordinate object.
     *
     * @param text  The string content to render.
     * @param point The {@link Point} object defining the location.
     */
    public Text(String text, Point point) {
        this.point = point;
        this.text = text;
    }

    /**
     * Retrieves the current textual content of this object.
     *
     * @return The text string.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets a new string value to be rendered.
     *
     * @param text The new string to display.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Retrieves the spatial coordinate point of the text.
     *
     * @return The {@link Point} indicating position.
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Retrieves the color applied to the font.
     *
     * @return The {@link Color} of the text.
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     * Sets the font color for the rendered text. Returns the instance itself to allow for method chaining.
     *
     * @param foreground The new {@link Color} to apply.
     * @return This {@code Text} instance.
     */
    public Text setForeground(Color foreground) {
        this.foreground = foreground;
        return this;
    }
}
