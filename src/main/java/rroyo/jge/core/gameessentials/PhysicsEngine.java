package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;

import java.util.*;
import java.util.function.Predicate;

/**
 * Global registry of positionable entities grouped by their owning scene.
 * <p>
 * Registration operations are synchronized and reads return immutable snapshots, so callers
 * can iterate without holding the registry lock. Entity identity is used for scene keys.
 * </p>
 */
public final class PhysicsEngine {

    /** Creates an instance; registry operations themselves are static and shared globally. */
    private PhysicsEngine() {}

    /** Guards all access to the scene-to-entity registry. */
    private static final Object LOCK = new Object();

    /** Stores each scene's active entities; identity semantics avoid custom scene equality. */
    private static final Map<Scene, List<PositionableGameObject>> gameObjects =
            new IdentityHashMap<>();

    /**
     * Registers one or more entities with their owning scenes. Re-registering an entity is
     * harmless and does not change its ordering. Every entity and owning scene must be non-null.
     *
     * @param gameObject entities to add to the registry
     * @throws NullPointerException if the array, an entity, or an entity's scene is null
     */
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

    /**
     * Returns an immutable snapshot of the entities currently registered to a scene.
     * An unregistered scene produces an empty list.
     *
     * @param scene scene whose entities are requested
     * @return an immutable point-in-time list, never {@code null}
     */
    public static List<PositionableGameObject> getGameObjects(Scene scene) {
        synchronized (LOCK) {
            List<PositionableGameObject> sceneObjects = gameObjects.get(scene);

            if (sceneObjects == null) return List.of();

            return List.copyOf(sceneObjects);
        }
    }

    /**
     * Tests whether an entity is currently present in its owning scene's registry.
     *
     * @param go entity to look up
     * @return {@code true} when registered; otherwise {@code false}
     * @throws NullPointerException if {@code go} is null
     */
    public static boolean contains(PositionableGameObject go) {
        Objects.requireNonNull(go, "gameObject");

        synchronized (LOCK) {
            List<PositionableGameObject> sceneObjects = gameObjects.get(go.getScene());

            if (sceneObjects == null) return false;

            return sceneObjects.contains(go);
        }
    }

    /**
     * Removes one entity from its owning scene and releases the scene entry when it becomes empty.
     * Removing an entity that is not registered has no effect.
     *
     * @param go entity to remove
     * @throws NullPointerException if {@code go} is null
     */
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

    /**
     * Removes all registered entities for a scene. This only clears registry membership; it does
     * not mark the entities as deleted or alter references held elsewhere.
     *
     * @param scene scene whose registry entry should be removed
     * @throws NullPointerException if {@code scene} is null
     */
    public static void remove(Scene scene) {
        Objects.requireNonNull(scene, "scene");
        synchronized (LOCK) {
            gameObjects.remove(scene);
        }
    }

    /**
     * Removes every entity in a scene for which the supplied predicate returns {@code true}.
     * The scene entry is discarded if no entities remain after filtering.
     *
     * @param scene scene whose entities are filtered
     * @param filter predicate identifying entities to remove
     * @throws NullPointerException if either argument is null
     */
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
