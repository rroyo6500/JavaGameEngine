package rroyo.jgameengine.objects.gameobjects;

import rroyo.jgameengine.enums.Direction;
import rroyo.jgameengine.interfaces.Portble;
import rroyo.jgameengine.objects.gameutils.Collider;
import rroyo.jgameengine.objects.gameutils.Point;
import rroyo.jgameengine.objects.gameutils.Sprite;
import rroyo.jgameengine.objects.gameutils.Velocity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GameObject implements Portble {

    protected final Velocity velocity = new Velocity();
    protected final List<Collider> colliders = new ArrayList<>();

    protected final Point point;
    protected final Sprite sprite;

    public GameObject(Point point, Sprite sprite) {
        this.point = point;
        this.sprite = sprite;
        for (Direction d : Direction.values()) {
            colliders.add(new Collider(point, sprite.getDimension(), d));
        }
    }

    public boolean overlap(GameObject go) {
        Rectangle rThis = new Rectangle(
                (int) point.getX(), (int) point.getY(),
                sprite.getDimension().width, sprite.getDimension().height);
        Rectangle rOther = new Rectangle(
                (int) go.getPoint().getX(), (int) go.getPoint().getY(),
                go.getSprite().getDimension().width, go.getSprite().getDimension().height);
        return rThis.intersects(rOther);
    }

    public Direction getDirection(GameObject go) {



        return null;
    }

    public void move() {
        point.setPoint(
                point.getX() + velocity.getVelocityX(),
                point.getY() + velocity.getVelocityY()
        );
        for (Collider collider : colliders) {
            collider.getPolygon().translate(
                    (int) velocity.getVelocityX(),
                    (int) velocity.getVelocityY());
        }
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

    public List<Collider> getColliders() {
        return colliders;
    }
}
