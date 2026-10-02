package rroyo.jgameengine.objects.gameutils;

import rroyo.jgameengine.enums.Direction;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Hitbox {

    private final Map<Direction, Collider> hitbox = new HashMap<>();

    public Hitbox(Point point, Dimension dimension) {
        for (Direction d : Direction.values()) {
            hitbox.put(d, new Collider(point, dimension, d));
        }
    }

    public boolean intersects(Direction d, Collider collider) {

        return  false;

    }

    public Collider getCollider(Direction direction) {
        return hitbox.get(direction);
    }

    public Map<Direction, Collider> getHitbox() {
        return hitbox;
    }
}
