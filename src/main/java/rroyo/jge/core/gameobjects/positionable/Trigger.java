package rroyo.jge.core.gameobjects.positionable;

import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Overlap;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.interfaces.Script;

public abstract class Trigger extends ScriptableGameObject {

    /**
     * Constructs a new {@code Trigger} using primitive coordinates and dimensions.
     *
     * @param x      The X-coordinate of the object's center.
     * @param y      The Y-coordinate of the object's center.
     * @param width  The total width of the object.
     * @param height The total height of the object.
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
     */
    public Trigger(Scene scene, Point position, Dimension size) {
        super(scene, position, size);
        addScript(new TriggerScript());
    }

    /**
     * Action to execute when the {@code Trigger} is activated
     *
     * @param triggeredObject  The {@link PositionableGameObject} to check
     */
    public abstract void onTrigger(PositionableGameObject triggeredObject);

    /**
     * Check if the {@code triggeredObject} intersects with the {@code Trigger}
     *
     * @param triggeredObject
     */
    public final boolean trigger(PositionableGameObject triggeredObject) {
        if (isDeleted() || triggeredObject.isDeleted()) return false;
        boolean isOverlap = Overlap.ov(this, triggeredObject);
        if (isOverlap)
            sendOnOverlap(this, triggeredObject);
        return isOverlap;
    }

    public final boolean trigger(Group triggeredGroup) {
        boolean ret = false;
        if (isDeleted() || triggeredGroup.isDeleted()) return false;
        for (PositionableGameObject go : triggeredGroup.getMembers()) {
            if (trigger(go)) ret = true;
        }
        return ret;
    }

}

class TriggerScript implements Script {

    @Override
    public void start(GameObject self) {

    }

    @Override
    public void update(GameObject self) {

    }

    @Override
    public void onOverlap(GameObject self, GameObject objective) {
        ((Trigger) self).onTrigger((GameElement) objective);
    }
}
