package rroyo.jgameengine.objects.gameobjects;

import rroyo.jgameengine.interfaces.Portble;
import rroyo.jgameengine.objects.gameutils.Point;
import rroyo.jgameengine.objects.gameutils.Sprite;
import rroyo.jgameengine.objects.gameutils.Velocity;

import java.awt.*;


public class GameObject implements Portble {

    protected final Velocity velocity = new Velocity();

    protected final Point point;
    protected final Sprite sprite;

    public GameObject(Point point, Sprite sprite) {
        this.point = point;
        this.sprite = sprite;
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

    public void move() {
        point.setPoint(
                point.getX() + velocity.getVelocityX(),
                point.getY() + velocity.getVelocityY()
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
}
