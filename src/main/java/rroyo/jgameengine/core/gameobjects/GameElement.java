package rroyo.jgameengine.core.gameobjects;

import rroyo.jgameengine.core.gameutils.*;
import rroyo.jgameengine.core.gameutils.Dimension;
import rroyo.jgameengine.core.gameutils.Point;

import java.awt.*;


public class GameElement extends GameObject {

    protected final Velocity velocity = new Velocity();

    protected final Point point;
    protected final Dimension dimension;
    protected final Sprite sprite;

    public GameElement(double x, double y, double width, double height, Sprite sprite) {
        this(new Point(x, y), new Dimension(width, height), sprite);
    }

    public GameElement(Point point, Dimension dimension, Sprite sprite) {
        this.point = point;
        this.dimension = dimension;
        this.sprite = sprite;
    }

    public Overlap getOverlap(GameElement go) {
        if (!this.inRange(go)) {
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

        return new Overlap(overlapX, overlapY);
    }

    public boolean overlap(GameElement go) {
        Overlap overlap = getOverlap(go);
        if (overlap == null) return false;
        return overlap.overlapX() > 0 && overlap.overlapY() > 0;
    }

    public boolean overlap(Group group) {
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    public boolean inRange(GameElement go) {
        double secureRange = (this.getDimension().getWidth() + this.getDimension().getHeight()) +
                (go.getDimension().getWidth() + go.getDimension().getHeight());

        double deltaX = go.getPoint().getX() - this.getPoint().getX();
        double deltaY = go.getPoint().getY() - this.getPoint().getY();

        if (Math.abs(deltaX) > secureRange || Math.abs(deltaY) > secureRange) {
            return false;
        }

        double distance = (deltaX * deltaX) + (deltaY * deltaY);
        return distance <= (secureRange * secureRange);
    }

    public boolean inRange(Group group) {
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public void move() {
        point.setPoint(
                point.getX() + velocity.getVelocityX(),
                point.getY() + velocity.getVelocityY()
        );
    }

    public void move(double dx, double dy) {
        point.setPoint(
                point.getX() + dx,
                point.getY() + dy
        );
    }

    public Point getPoint() {
        return point;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return new Rectangle(
                (int) (point.getX() - this.getDimension().getHalfWidth()),
                (int) (point.getY() - this.getDimension().getHalfHeight()),
                (int) this.getDimension().getWidth(), (int) this.getDimension().getHeight()
        );
    }

    public Dimension getDimension() {
        return dimension;
    }
}
