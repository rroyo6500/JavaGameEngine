package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.Text;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class PhisicsEngine {

    private static final Map<Integer, List<PositionableGameObject>> gameObjects = new HashMap<>();

    public static void add(PositionableGameObject... gameObject) {
        for (PositionableGameObject go : gameObject) {
            if (!gameObjects.containsKey(go.getSceneHash())) {
                gameObjects.get(go.getSceneHash()).add(go);
                if (go instanceof ScriptableGameObject sgo) {
                    sgo.start();
                }
            }
        }
    }

    public static List<PositionableGameObject> getGameObjects(Scene scene) {
        return List.copyOf(gameObjects.get(scene.hashCode()));
    }

    public static boolean contains(PositionableGameObject go) {
        if (gameObjects.containsKey(go.getSceneHash()))
            return gameObjects.get(go.getSceneHash()).contains(go);
        return false;
    }

    public static void remove(PositionableGameObject go) {
        gameObjects.get(go.getSceneHash()).remove(go);
    }

    public static void removeIf(Predicate<? super PositionableGameObject> filter) {
        for (List<PositionableGameObject> pgo : gameObjects.values()){
            pgo.removeIf(filter);
        }
    }

}
