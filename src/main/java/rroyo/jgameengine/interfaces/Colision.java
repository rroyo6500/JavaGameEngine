package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.enums.Direction;
import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Collider;

public interface Colision {

    default boolean collide(GameObject go) {
        if (this instanceof GameObject self) {
            if (self.overlap(go)) {

                for (Direction d : Direction.values()) {
                    Collider cSelf = self.getHitbox().getCollider(d);
                    Collider cOther = go.getHitbox().getCollider(d.getOposite());

                    if (cSelf.getPolygon().intersects(cOther.getPolygon().getBounds2D()) ||
                            cOther.getPolygon().intersects(cSelf.getPolygon().getBounds2D())
                    ) {
                        switch (d) {
                            case UP -> self.setVelocityY(1);
                            case DOWN -> self.setVelocityY(-1);
                            case LEFT -> self.setVelocityX(1);
                            case RIGHT -> self.setVelocityX(-1);
                        }
                    }

                }

            }
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
        return false;
    }

}
