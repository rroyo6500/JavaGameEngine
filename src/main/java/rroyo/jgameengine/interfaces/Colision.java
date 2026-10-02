package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Overlap;

public interface Colision {

    default boolean collide(GameObject gameObject) {
        if (this instanceof GameObject self) {
            if (self.overlap(gameObject)) {
                Overlap overlap = self.getOverlap(gameObject);

                if (overlap.overlapX() < overlap.overlapY()) {
                    if (self.getPoint().getX() < gameObject.getPoint().getX()) {
                        self.move(-overlap.overlapX(), 0);
                    } else {
                        self.move(overlap.overlapX(), 0);
                    }
                    self.setVelocityX(0);
                } else {
                    if (self.getPoint().getY() < gameObject.getPoint().getY()) {
                        self.move(0, -overlap.overlapY());
                    } else {
                        self.move(0, overlap.overlapY());
                    }
                    self.setVelocityY(0);
                }
                return true;
            }
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
        return false;
    }

}
