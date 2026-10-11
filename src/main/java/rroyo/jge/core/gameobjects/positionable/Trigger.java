package rroyo.jge.core.gameobjects.positionable;

import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Overlap;
import rroyo.jge.core.gameutils.Point;
import rroyo.jge.core.gameutils.Velocity;
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
    public Trigger(double x, double y, double width, double height) {
        super(x, y, width, height);
        addScript(new TriggerScript());
    }

    /**
     * Constructs a new {@code Trigger} using object-based spatial properties.
     *
     * @param position     The {@link rroyo.jge.core.gameutils.Point} representing the object's center.
     * @param size The {@link rroyo.jge.core.gameutils.Dimension} representing the object's size.
     */
    public Trigger(Point position, Dimension size) {
        super(position, size);
        addScript(new TriggerScript());
    }

    public abstract void onTrigger(GameElement triggeredElement);

    public final void trigger(PositionableGameObject triggeredElement) {
        sendOnOverlap(this, triggeredElement);
    }

    public final void trigger(Group triggeredGroup) {
        sendOnOverlap(this, triggeredGroup);
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

    @Override
    public void onCollide(GameObject self, GameObject objective) {
        ((Trigger) self).onTrigger((GameElement) objective);
    }
}
