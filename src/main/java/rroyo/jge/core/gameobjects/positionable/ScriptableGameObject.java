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

public class ScriptableGameObject extends PositionableGameObject {

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
     * Sends an {@code onInRange} event fot this {@link  GameObject}
     *
     * @param self      This {@link  GameObject}
     * @param objective The other {@link  GameObject} that participates in the interaction
     */
    public final void sendOnInRange(GameObject self, GameObject objective) {
        for (Script s : scripts) {
            s.onInRange(self, objective);
        }
    }

    /**
     * Sends an {@code onOvelap} event fot this {@link  GameObject}
     *
     * @param self      This {@link  GameObject}
     * @param objective The other {@link  GameObject} that participates in the interaction
     */
    public final void sendOnOverlap(GameObject self, GameObject objective) {
        for (Script s : scripts) {
            s.onOverlap(self, objective);
        }
    }

    /**
     * Sends an {@code onCollide} event fot this {@link  GameObject}
     *
     * @param self      This {@link  GameObject}
     * @param objective The other {@link  GameObject} that participates in the interaction
     */
    public final void sendOnCollide(GameObject self, GameObject objective) {
        for (Script s : scripts) {
            s.onCollide(self, objective);
        }
    }

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

    public List<Script> getScripts() {
        return List.copyOf(scripts);
    }
}
