package rroyo.jgameengine.core.gameutils;

import java.awt.*;

public class Text {

    private final Point point;
    private String text;

    private Color foreground;

    public Text(String text, double x, double y) {
        this(text, new Point(x, y));
    }

    public Text(String text, Point point) {
        this.point = point;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Point getPoint() {
        return point;
    }

    public Color getForeground() {
        return foreground;
    }

    public Text setForeground(Color foreground) {
        this.foreground = foreground;
        return this;
    }
}
