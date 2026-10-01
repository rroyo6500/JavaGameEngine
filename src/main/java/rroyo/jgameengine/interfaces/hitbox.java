package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Collider;

public interface hitbox {

    default boolean collide(GameObject go) {
        if (this instanceof GameObject self) {
            if (self.overlap(go)) {

                for (Collider cOther : go.getColliders()) {

                    if (cOther.getPolygon().intersects(self.getBounds())) {
                        switch (cOther.getDirection()) {
                            case UP, DOWN -> self.getVelocity().setVelocityY(0);
                            case LEFT, RIGHT -> self.getVelocity().setVelocityX(0);
                        }
                    }

                }

            }
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
        return false;
    }

}
