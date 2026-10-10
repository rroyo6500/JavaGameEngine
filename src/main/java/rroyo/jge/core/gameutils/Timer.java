package rroyo.jge.core.gameutils;

import java.util.*;

/**
 * The {@code Timer} class provides a mechanism to execute a callback function after a specified delay.
 * It supports one-shot executions as well as looping timers.
 * The class manages a global static list of all active timers, which are updated every frame.
 */
public final class Timer {

    /**
     * A global list of all currently active timers that are being processed.
     */
    private static final List<Timer> timers = new ArrayList<>();

    /**
     * A temporary list of timers waiting to be added to the active list.
     * This prevents {@link java.util.ConcurrentModificationException} when timers are created during an update loop.
     */
    private static final List<Timer> pendingAdd = new ArrayList<>();

    /**
     * The amount of time remaining in seconds before this timer triggers its callback.
     */
    private float remaining;

    /**
     * The action to be executed when the timer reaches zero.
     */
    private final Runnable callback;

    /**
     * A flag indicating whether the timer should restart automatically after finishing.
     */
    private final boolean loop;

    /**
     * A flag indicating whether this timer has been cancelled and should no longer trigger or update.
     */
    private boolean cancelled;

    /**
     * The original duration of the timer, used to reset the timer if it loops.
     */
    private final float initialDuration;

    /**
     * Constructs a new {@code Timer}.
     *
     * @param seconds  The duration of the timer in seconds.
     * @param callback The {@link Runnable} to execute when the time elapses.
     * @param loop     {@code true} if the timer should restart automatically upon completion; {@code false} otherwise.
     */
    private Timer(float seconds, Runnable callback, boolean loop) {
        this.initialDuration = seconds;
        this.remaining = seconds;
        this.callback = callback;
        this.loop = loop;

        pendingAdd.add(this);
    }

    /**
     * Updates this specific timer's remaining time.
     * If the timer reaches zero, the callback is executed. If it is a looping timer, it resets.
     *
     * @param dt The delta time (elapsed time since the last frame) in seconds.
     */
    public void update(float dt) {
        if (cancelled) return;

        remaining -= dt;
        if (remaining <= 0) {
            if (callback != null) callback.run();
            if (loop) remaining = initialDuration;
        }
    }

    /**
     * Updates all active timers globally.
     * This method adds any pending timers to the active list and decreases their remaining time using the global delta time.
     * Finished non-looping timers are automatically removed from the list.
     */
    public static void update() {
        timers.addAll(pendingAdd);
        pendingAdd.clear();

        float dt = Time.deltaTime();
        for (int i = timers.size() - 1; i >= 0; i--) {
            Timer timer = timers.get(i);
            timer.update(dt);

            if (timer.isFinished()) {
                timers.remove(i);
            }
        }
    }

    /**
     * Schedules a one-shot timer to execute a callback after a given delay.
     *
     * @param seconds  The delay in seconds before the callback is executed.
     * @param callback The {@link Runnable} to execute.
     * @return The created {@code Timer} instance.
     */
    public static Timer schedule(float seconds, Runnable callback) {
        return schedule(seconds, callback, false);
    }

    /**
     * Schedules a timer to execute a callback after a given delay, with an option to loop continuously.
     *
     * @param seconds  The delay in seconds before the callback is executed.
     * @param callback The {@link Runnable} to execute.
     * @param loop     {@code true} to make the timer restart automatically; {@code false} for a one-shot execution.
     * @return The created {@code Timer} instance.
     */
    public static Timer schedule(float seconds, Runnable callback, boolean loop) {
        return new Timer(seconds, callback, loop);
    }

    /**
     * Checks if this timer has finished its countdown.
     * A looping timer is never considered finished.
     *
     * @return {@code true} if the time has elapsed and the timer is not set to loop; {@code false} otherwise.
     */
    public boolean isFinished() {
        return remaining <= 0 && !loop;
    }

    /**
     * Cancels the timer, preventing it from updating or executing its callback in the future.
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * Indicates whether some other object is "equal to" this timer.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Timer timer = (Timer) o;
        return Float.compare(remaining, timer.remaining) == 0 &&
                loop == timer.loop &&
                cancelled == timer.cancelled &&
                Float.compare(initialDuration, timer.initialDuration) == 0 &&
                Objects.equals(callback, timer.callback);
    }

    /**
     * Returns a hash code value for the timer.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(remaining, callback, loop, cancelled, initialDuration);
    }
}
