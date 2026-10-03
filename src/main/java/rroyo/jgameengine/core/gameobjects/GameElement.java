package rroyo.jgameengine.core.gameobjects;

import rroyo.jgameengine.core.assets.Sprite;
import rroyo.jgameengine.core.gameutils.*;
import rroyo.jgameengine.core.gameutils.Dimension;
import rroyo.jgameengine.core.gameutils.Point;

import java.awt.*;


public class GameElement extends GameObject {

    protected Velocity velocity = new Velocity();

    protected Point point;
    protected Dimension dimension;
    protected Sprite sprite;

    public GameElement(double x, double y, double width, double height, Sprite sprite) {
        this(new Point(x, y), new Dimension(width, height), sprite);
    }

    public GameElement(Point point, Dimension dimension, Sprite sprite) {
        this.point = point;
        this.dimension = dimension;
        this.sprite = sprite;
    }

    public Overlap getOverlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return null;
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
        if (isDeleted() || go.isDeleted()) return false;
        Overlap overlap = getOverlap(go);
        if (overlap == null) return false;
        return overlap.overlapX() > 0 && overlap.overlapY() > 0;
    }

    public boolean overlap(Group group) {
        if (isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    public GameElement getOverlapElement(Group group) {
        if (isDeleted()) return null;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return go;
            }
        }
        return null;
    }

    public boolean inRange(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
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
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public GameElement getInRangeElement(Group group) {
        if (isDeleted() || group.isDeleted()) return null;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return go;
            }
        }
        return null;
    }

    public void move() {
        if (isDeleted()) return ;
        point.setPoint(
                point.getX() + (velocity.getVelocityX() * Time.deltaTime()),
                point.getY() + (velocity.getVelocityY() * Time.deltaTime())
        );
    }

    public void move(double dx, double dy) {
        if (isDeleted()) return ;
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
        if (isDeleted()) return null;
        return new Rectangle(
                (int) (point.getX() - this.getDimension().getHalfWidth()),
                (int) (point.getY() - this.getDimension().getHalfHeight()),
                (int) this.getDimension().getWidth(), (int) this.getDimension().getHeight()
        );
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setSprite(Sprite sprite) {
        if (isDeleted()) return ;
        this.sprite = sprite;
    }

    @Override
    public void delete() {
        super.delete();
        velocity = null;
        point = null;
        dimension = null;
        sprite = null;
    }
}
