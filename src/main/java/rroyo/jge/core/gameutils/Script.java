package rroyo.jge.core.gameutils;

import rroyo.jge.core.gameobjects.GameElement;
import rroyo.jge.core.gameobjects.GameObject;

public interface Script {

    void update(GameElement self);

    default void onOverlap(GameElement self, GameElement objective) {
    }

    default void onCollide(GameElement self, GameElement objective) {
    }

    default void onInRange(GameElement self, GameElement objective) {
    }

}
