package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;

import java.util.*;
import java.util.function.Predicate;

public final class PhysicsEngine {

    private static final Object LOCK = new Object();

    private static final Map<Scene, List<PositionableGameObject>> gameObjects =
            new IdentityHashMap<>();

    public static void add(PositionableGameObject... gameObject) {
        Objects.requireNonNull(gameObject, "gameObject");

        synchronized (LOCK) {
            for (PositionableGameObject go : gameObject) {
                Objects.requireNonNull(go, "gameObject");

                Scene scene = Objects.requireNonNull(go.getScene(),
                        "The game object must belong to a scene");

                List<PositionableGameObject> sceneObjects =
                        gameObjects.computeIfAbsent(
                                scene,
                                ignored -> new ArrayList<>()
                        );

                if (!sceneObjects.contains(go)) sceneObjects.add(go);
            }
        }
    }

    public static List<PositionableGameObject> getGameObjects(Scene scene) {
        synchronized (LOCK) {
            List<PositionableGameObject> sceneObjects = gameObjects.get(scene);

            if (sceneObjects == null) return List.of();

            return List.copyOf(sceneObjects);
        }
    }

    public static boolean contains(PositionableGameObject go) {
        Objects.requireNonNull(go, "gameObject");

        synchronized (LOCK) {
            List<PositionableGameObject> sceneObjects = gameObjects.get(go.getScene());

            if (sceneObjects == null) return false;

            return sceneObjects.contains(go);
        }
    }

    public static void remove(PositionableGameObject go) {
        Objects.requireNonNull(go, "gameObject");

        synchronized (LOCK) {
            Scene scene = go.getScene();
            List<PositionableGameObject> sceneObjects = gameObjects.get(scene);

            if (sceneObjects == null) return;

            sceneObjects.remove(go);

            if (sceneObjects.isEmpty()) gameObjects.remove(scene);
        }
    }

    public static void remove(Scene scene) {
        Objects.requireNonNull(scene, "scene");
        synchronized (LOCK) {
            gameObjects.remove(scene);
        }
    }

    public static void removeIf(Scene scene, Predicate<? super PositionableGameObject> filter) {
        Objects.requireNonNull(scene, "scene");
        Objects.requireNonNull(filter, "filter");

        synchronized (LOCK) {

            List<PositionableGameObject> sceneObjects = gameObjects.get(scene);

            if (sceneObjects == null) return;

            sceneObjects.removeIf(filter);

            if (sceneObjects.isEmpty()) gameObjects.remove(scene);
        }
    }

}
