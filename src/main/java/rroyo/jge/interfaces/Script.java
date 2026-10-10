package rroyo.jge.interfaces;

import rroyo.jge.core.gameobjects.GameElement;

/**
 * The {@code Script} interface defines a set of lifecycle and interaction hooks
 * that can be attached to a {@link GameElement}. Implementing this interface allows
 * developers to inject custom logic for initialization, frame-by-frame updates,
 * and collision/overlap events.
 */
public interface Script {

    /**
     * Called once when the script is initialized or when the game element it is attached to starts.
     * Useful for setting up initial values or states.
     *
     * @param self The {@link GameElement} this script is attached to.
     */
    void start(GameElement self);

    /**
     * Called every frame during the game loop.
     * This is where the core logic, movement, or decision-making for the element should reside.
     *
     * @param self The {@link GameElement} this script is attached to.
     */
    void update(GameElement self);

    /**
     * Triggered when the bounding area of this element overlaps with another element,
     * often used for triggers that do not have solid physical responses (like sensors).
     *
     * @param self      The {@link GameElement} this script is attached to.
     * @param objective The other {@link GameElement} that was overlapped.
     */
    default void onOverlap(GameElement self, GameElement objective) {
    }

    /**
     * Triggered when this element physically collides with another solid element.
     *
     * @param self      The {@link GameElement} this script is attached to.
     * @param objective The other {@link GameElement} involved in the collision.
     */
    default void onCollide(GameElement self, GameElement objective) {
    }

    /**
     * Triggered when another element enters a defined range or line of sight of this element.
     *
     * @param self      The {@link GameElement} this script is attached to.
     * @param objective The other {@link GameElement} that is within range.
     */
    default void onInRange(GameElement self, GameElement objective) {
    }

}
