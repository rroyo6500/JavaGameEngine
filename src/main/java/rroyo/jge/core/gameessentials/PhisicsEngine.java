package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;

import java.util.ArrayList;
import java.util.List;

public final class PhisicsEngine {

    private static final List<PositionableGameObject> gameObjects = new ArrayList<>();

    public static void add(PositionableGameObject... gameObject) {
        for (PositionableGameObject go : gameObject) {
            if (!gameObjects.contains(go)) {
                gameObjects.add(go);
            }
        }
    }

    public static List<PositionableGameObject> getGameObjects() {
        return List.copyOf(gameObjects);
    }

    public static boolean contains(PositionableGameObject go) {
        return gameObjects.contains(go);
    }

}
