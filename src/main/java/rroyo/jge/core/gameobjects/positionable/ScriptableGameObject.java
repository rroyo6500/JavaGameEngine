package rroyo.jge.core.gameobjects.positionable;

import org.jetbrains.annotations.NotNull;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.interfaces.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A positionable entity that delegates lifecycle and interaction behavior to attached scripts.
 * Each script is initialized when attached, while the object's normal start phase is guarded
 * so it runs only once. Updates and interaction notifications are forwarded in attachment order.
 */
public class ScriptableGameObject extends PositionableGameObject {

    /** Prevents the regular start phase from invoking scripts more than once. */
    private boolean started = false;

    /**
     * A list of {@link Script} behaviors attached to this object.
     */
    private final List<Script> scripts = new ArrayList<>();

    /**
     * Constructs a new {@code ScriptableGameObject} using primitive coordinates and dimensions.
     *
     * @param x      The X-coordinate of the object's center.
     * @param y      The Y-coordinate of the object's center.
     * @param width  The total width of the object.
     * @param height The total height of the object.
     * @param scripts A variable number of {@link Script}s to attach for customized behavior.
     * @param scene scene that owns and updates this entity
     */
    public ScriptableGameObject(Scene scene, double x, double y, double width, double height, Script... scripts) {
        super(scene, x, y, width, height);
        if (scripts != null && scripts.length > 0) addScript(scripts);
    }

    /**
     * Constructs a new {@code ScriptableGameObject} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the object's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the object's size.
     * @param scripts    A variable number of {@link Script}s to attach for customized behavior.
     * @param scene scene that owns and updates this entity
     */
    public ScriptableGameObject(Scene scene, Point position, Dimension size, Script... scripts) {
        super(scene, position, size);
        if (scripts != null && scripts.length > 0) addScript(scripts);
    }

    /**
     * Initializes the {@link GameObject} by calling the {@code start} method on all attached scripts.
     * This should typically be called right after the {@link  GameObject} is instantiated or added to a scene.
     */
    public final void start() {
        if (started) return;
        started = true;
        for (Script s : scripts) {
            s.start(this);
        }
    }

    /**
     * Updates the {@link GameObject} state for the current frame by delegating to its attached scripts.
     */
    public final void update() {
        for (Script s : scripts) {
            s.update(this);
        }
    }

    /**
     * Dispatches an in-range notification to each attached script.
     *
     * @param self object reported as the source of the event
     * @param objective other object reported as the event target
     */
    public final void sendOnInRange(GameObject self, GameObject objective) {
        for (Script s : scripts) {
            s.onInRange(self, objective);
        }
    }

    /**
     * Dispatches an overlap notification to each attached script.
     *
     * @param self object reported as the source of the event
     * @param objective other object reported as the event target
     */
    public final void sendOnOverlap(GameObject self, GameObject objective) {
        for (Script s : scripts) {
            s.onOverlap(self, objective);
        }
    }

    /**
     * Dispatches a collision notification to each attached script.
     *
     * @param self object reported as the source of the event
     * @param objective other object reported as the event target
     */
    public final void sendOnCollide(GameObject self, GameObject objective) {
        for (Script s : scripts) {
            s.onCollide(self, objective);
        }
    }

    /**
     * Attaches each non-null script that is not already present. Newly attached scripts receive
     * their {@link Script#start(GameObject)} callback immediately, and the object is marked started
     * so its regular frame-start phase will not initialize them a second time.
     *
     * @param script scripts to attach in the supplied order
     * @throws NullPointerException if the array or any script in it is null
     */
    public void addScript(@NotNull Script... script) {
        Objects.requireNonNull(script);
        for (Script s : script) {
            Objects.requireNonNull(s);
            if (!scripts.contains(s)) {
                scripts.add(s);
                s.start(this);
                started = true;
            }
        }
    }

    /**
     * Returns an immutable snapshot of scripts currently attached to this entity.
     *
     * @return scripts in attachment order
     */
    public List<Script> getScripts() {
        return List.copyOf(scripts);
    }
}
