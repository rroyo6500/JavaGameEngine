package rroyo.jgameengine.objects.gameutils;

import rroyo.jgameengine.enums.Direction;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Hitbox {

    private final Map<Direction, Collider> hitbox = new HashMap<>();
    private final Rectangle hitboxBounds;

    public Hitbox(Point point, Dimension dimension) {
        this.hitboxBounds = new Rectangle(
                (int) point.getX() - (dimension.width / 2),
                (int) point.getY() - (dimension.height / 2),
                dimension.width, dimension.height);
        for (Direction d : Direction.values()) {
            hitbox.put(d, new Collider(point, dimension, d));
        }
    }

    public boolean intersects(Hitbox hitbox) {
        return this.getHitboxBounds().intersects(hitbox.getHitboxBounds());
    }

    public void move(int deltaX, int deltaY) {
        for (Collider collider : hitbox.values()) {
            collider.move(
                    deltaX,
                    deltaY);
        }
        hitboxBounds.setLocation(
                hitboxBounds.x + deltaX,
                hitboxBounds.y + deltaY);
    }

    public Collider getCollider(Direction direction) {
        return hitbox.get(direction);
    }

    public Map<Direction, Collider> getHitbox() {
        return hitbox;
    }

    public Rectangle getHitboxBounds() {
        return hitboxBounds;
    }
}
