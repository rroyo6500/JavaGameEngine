package rroyo.jgameengine.objects.gameobjects;

import rroyo.jgameengine.interfaces.Colision;
import rroyo.jgameengine.interfaces.Portble;
import rroyo.jgameengine.objects.gameutils.*;
import rroyo.jgameengine.objects.gameutils.Point;

import java.awt.*;


public class GameObject implements Portble, Colision {

    protected final Velocity velocity = new Velocity();
    private final Hitbox hitbox;

    protected final Point point;
    protected final Sprite sprite;

    public GameObject(double x, double y, Sprite sprite) {
        this(new Point(x, y), sprite);
    }

    public GameObject(Point point, Sprite sprite) {
        this.point = point;
        this.sprite = sprite;
        this.hitbox = new Hitbox(point.getX(), point.getY(),
                sprite.getDimension().getWidth(), sprite.getDimension().getHeight());
    }

    public Overlap getOverlap(GameObject go) {
        if (!this.inRange(go)) {
            return null;
        }

        // X
        double deltaX = Math.abs((int) this.hitbox.getX() - (int) go.hitbox.getX());
        double limitX = this.hitbox.getHalfWidth() + go.hitbox.getHalfWidth();
        double overlapX = limitX - deltaX;

        // Y
        double deltaY = Math.abs((int) this.hitbox.getY() - (int) go.hitbox.getY());
        double limitY = this.hitbox.getHalfHeight() + go.hitbox.getHalfHeight();
        double overlapY = limitY - deltaY;

        return new Overlap(overlapX, overlapY);
    }

    public boolean overlap(GameObject go) {
        Overlap overlap = getOverlap(go);
        if (overlap == null) return false;
        return overlap.overlapX() > 0 && overlap.overlapY() > 0;
    }

    public boolean inRange(GameObject go) {
        double secureRange = (this.getSprite().getDimension().getWidth() + this.getSprite().getDimension().getHeight()) +
                (go.getSprite().getDimension().getWidth() + go.getSprite().getDimension().getHeight());

        double deltaX = go.getPoint().getX() - this.getPoint().getX();
        double deltaY = go.getPoint().getY() - this.getPoint().getY();

        if (Math.abs(deltaX) > secureRange || Math.abs(deltaY) > secureRange) {
            return false;
        }

        double distance = (deltaX * deltaX) + (deltaY * deltaY);
        return distance <= (secureRange * secureRange);
    }

    public void move() {
        point.setPoint(
                point.getX() + velocity.getVelocityX(),
                point.getY() + velocity.getVelocityY()
        );
        hitbox.move(velocity.getVelocityX(), velocity.getVelocityY());
    }

    public void move(double dx, double dy) {
        point.setPoint(
                point.getX() + dx,
                point.getY() + dy
        );
        hitbox.move(dx, dy);
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
                (int) (point.getX() - sprite.getDimension().getHalfWidth()),
                (int) (point.getY() - sprite.getDimension().getHalfHeight()),
                (int) sprite.getDimension().getWidth(), (int) sprite.getDimension().getHeight()
        );
    }
}
