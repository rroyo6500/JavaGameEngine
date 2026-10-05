package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.Group;
import rroyo.jgameengine.core.gameutils.Overlap;

import java.lang.instrument.IllegalClassFormatException;

public interface Colision {

    default boolean collide(GameElement gameElement) throws IllegalClassFormatException {
        if (this instanceof GameElement self) {
            if (self.overlap(gameElement)) {
                Overlap overlap = self.getOverlap(gameElement);
                if (overlap == null) return false;
                if (overlap.overlapX() < overlap.overlapY()) {
                    if (self.getPoint().getX() < gameElement.getPoint().getX()) {
                        self.move(-overlap.overlapX(), 0);
                    } else {
                        self.move(overlap.overlapX(), 0);
                    }
                    if (self instanceof Portble selfP)
                        selfP.setVelocityX(0);
                } else {
                    if (self.getPoint().getY() < gameElement.getPoint().getY()) {
                        self.move(0, -overlap.overlapY());
                    } else {
                        self.move(0, overlap.overlapY());
                    }
                    if (self instanceof Portble selfP)
                        selfP.setVelocityY(0);
                }
                return true;
            }
        } else
            throw new IllegalClassFormatException("Required an instance of 'GameObject'");
        return false;
    }

    default boolean collide(Group group) throws IllegalClassFormatException {
        for (GameElement go : group.getMembers()) {
            if (this.collide(go)) {
                return true;
            }
        }
        return false;
    }

}
