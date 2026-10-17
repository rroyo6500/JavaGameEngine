package rroyo.jge.core.gameobjects.positionable;

import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Overlap;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.interfaces.Script;

/**
 * A non-solid positionable sensor that invokes {@link #onTrigger(PositionableGameObject)}
 * when explicitly checked against an overlapping entity or group. The trigger check does not
 * run automatically; game logic must call one of the {@code trigger} methods.
 */
public abstract class Trigger extends ScriptableGameObject {

    /**
     * Constructs a new {@code Trigger} using primitive coordinates and dimensions.
     *
     * @param x      The X-coordinate of the object's center.
     * @param y      The Y-coordinate of the object's center.
     * @param width  The total width of the object.
     * @param height The total height of the object.
     * @param scene scene that owns and updates this trigger
     */
    public Trigger(Scene scene, double x, double y, double width, double height) {
        super(scene, x, y, width, height);
        addScript(new TriggerScript());
    }

    /**
     * Constructs a new {@code Trigger} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the object's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the object's size.
     * @param scene scene that owns and updates this trigger
     */
    public Trigger(Scene scene, Point position, Dimension size) {
        super(scene, position, size);
        addScript(new TriggerScript());
    }

    /**
     * Action to execute when an explicit trigger check finds an overlapping entity.
     *
     * @param triggeredObject overlapping entity that activated this trigger
     */
    public abstract void onTrigger(PositionableGameObject triggeredObject);

    /**
     * Checks whether one entity overlaps this trigger and dispatches {@code onTrigger} when it does.
     * Deleted entities and deleted triggers are ignored.
     *
     * @param triggeredObject entity to test against this trigger
     * @return {@code true} when their oriented rectangles overlap; otherwise {@code false}
     */
    public final boolean trigger(PositionableGameObject triggeredObject) {
        if (isDeleted() || triggeredObject.isDeleted()) return false;
        boolean isOverlap = Overlap.ov(this, triggeredObject);
        if (isOverlap)
            sendOnOverlap(this, triggeredObject);
        return isOverlap;
    }

    /**
     * Checks every current group member against this trigger and dispatches an action for each
     * overlapping member. The group itself is not modified by the check.
     *
     * @param triggeredGroup group of entities to test
     * @return {@code true} if at least one member overlaps; otherwise {@code false}
     */
    public final boolean trigger(Group triggeredGroup) {
        boolean ret = false;
        if (isDeleted() || triggeredGroup.isDeleted()) return false;
        for (PositionableGameObject go : triggeredGroup.getMembers()) {
            if (trigger(go)) ret = true;
        }
        return ret;
    }

}

/** Bridges the generic overlap event into the trigger's typed callback. */
class TriggerScript implements Script {

    /** No initialization is required for the built-in trigger event adapter. */
    @Override
    public void start(GameObject self) {

    }

    /** Trigger behavior is dispatched by explicit calls to the trigger methods. */
    @Override
    public void update(GameObject self) {

    }

    /** Invokes the trigger's user-defined action when an overlap event is dispatched. */
    @Override
    public void onOverlap(GameObject self, GameObject objective) {
        if (objective instanceof PositionableGameObject)
            ((Trigger) self).onTrigger((PositionableGameObject) objective);
    }
}
