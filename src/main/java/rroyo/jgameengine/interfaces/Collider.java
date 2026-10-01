package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.objects.gameobjects.GameObject;

public interface Collider {

    default boolean collide(GameObject go) {
        if (this instanceof GameObject) {



        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
        return false;
    }

}
