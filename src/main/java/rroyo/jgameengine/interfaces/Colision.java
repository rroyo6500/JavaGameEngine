package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.enums.Direction;
import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Collider;

public interface Colision {

    default boolean collide(GameObject gameObject) {
        if (this instanceof GameObject self) {
            if (self.overlap(gameObject)) {

                for (Direction d : Direction.values()) {

                    Collider cSelf = self.getHitbox().getCollider(d);
                    Collider cOther = gameObject.getHitbox().getCollider(d.getOposite());

                    if (cSelf.intersects(cOther)) {
                        switch (d) {
                            case UP -> self.setVelocityY(-1);
                            case DOWN -> self.setVelocityY(1);
                            case LEFT -> self.setVelocityX(1);
                            case RIGHT -> self.setVelocityX(-1);
                        }
                    }

                }
                return true;
            }
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
        return false;
    }

}
