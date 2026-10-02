package rroyo.jgameengine.objects.gameobjects;

import rroyo.jgameengine.enums.Direction;
import rroyo.jgameengine.interfaces.Colision;
import rroyo.jgameengine.interfaces.Portble;
import rroyo.jgameengine.objects.gameutils.*;
import rroyo.jgameengine.objects.gameutils.Point;

import java.awt.*;


public class GameObject implements Portble, Colision {

    protected final Velocity velocity = new Velocity();
    protected final Hitbox hitbox;

    protected final Point point;
    protected final Sprite sprite;

    public GameObject(Point point, Sprite sprite) {
        this.point = point;
        this.sprite = sprite;
        hitbox = new Hitbox(point, sprite.getDimension());
    }

    public boolean overlap(GameObject go) {
        return this.getHitbox().intersects(go.getHitbox());
    }

    public Direction getDirection(GameObject go) {



        return null;
    }

    public void move() {
        point.setPoint(
                point.getX() + velocity.getVelocityX(),
                point.getY() + velocity.getVelocityY()
        );
        this.getHitbox().move(
                (int) velocity.getVelocityX(),
                (int) velocity.getVelocityY());
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
                (int) point.getX() - (sprite.getDimension().width / 2),
                (int) point.getY() - (sprite.getDimension().height / 2),
                sprite.getDimension().width, sprite.getDimension().height
        );
    }

    public Hitbox getHitbox() {
        return hitbox;
    }
}
