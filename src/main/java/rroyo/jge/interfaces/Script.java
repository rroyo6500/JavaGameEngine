package rroyo.jge.interfaces;

import rroyo.jge.core.gameobjects.GameElement;

public interface Script {

    void start(GameElement self);

    void update(GameElement self);

    default void onOverlap(GameElement self, GameElement objective) {
    }

    default void onCollide(GameElement self, GameElement objective) {
    }

    default void onInRange(GameElement self, GameElement objective) {
    }

}
