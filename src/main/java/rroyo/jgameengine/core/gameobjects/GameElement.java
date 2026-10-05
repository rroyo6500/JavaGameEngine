package rroyo.jgameengine.core.gameobjects;

import rroyo.jgameengine.core.assets.Sprite;
import rroyo.jgameengine.core.gameutils.*;

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

    public final Overlap getOverlap(GameElement go) {
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

    public final boolean overlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
        Overlap overlap = getOverlap(go);
        if (overlap == null) return false;
        return overlap.overlapX() > 0 && overlap.overlapY() > 0;
    }

    public final boolean overlap(Group group) {
        if (isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    public final GameElement getOverlapElement(Group group) {
        if (isDeleted()) return null;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return go;
            }
        }
        return null;
    }

    public final boolean inRange(GameElement go) {
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

    public final boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public final GameElement getInRangeElement(Group group) {
        if (isDeleted() || group.isDeleted()) return null;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return go;
            }
        }
        return null;
    }

    protected double transformVelocity(double velocity) {
        return velocity;
    }

    public final void move() {
        move(
                transformVelocity(velocity.getVelocityX()),
                transformVelocity(velocity.getVelocityY())
        );
    }

    public final void move(double dx, double dy) {
        if (isDeleted()) return ;
        point.setPoint(
                point.getX() + dx,
                point.getY() + dy
        );
    }

    public final Point getPoint() {
        return point;
    }

    public final Sprite getSprite() {
        return sprite;
    }

    public final Rectangle getBounds() {
        if (isDeleted()) return null;
        return new Rectangle(
                (int) (point.getX() - this.getDimension().getHalfWidth()),
                (int) (point.getY() - this.getDimension().getHalfHeight()),
                (int) this.getDimension().getWidth(), (int) this.getDimension().getHeight()
        );
    }

    public final Dimension getDimension() {
        return dimension;
    }

    public final void setSprite(Sprite sprite) {
        if (isDeleted()) return ;
        this.sprite = sprite;
    }

    public final Velocity getVelocity() {
        return velocity;
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
