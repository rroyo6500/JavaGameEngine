package rroyo.jge.core.gameutils;

/**
 * The {@code Time} class is a global utility that manages and provides access to time-related data
 * within the game engine, such as delta time (the time elapsed between frames), total elapsed time,
 * and a time scale factor for slowing down or speeding up the game.
 */
public final class Time {

    /**
     * A multiplier applied to the delta time.
     * A value of 1.0 represents normal speed. Less than 1.0 creates a slow-motion effect,
     * and greater than 1.0 fast-forwards the game.
     */
    private static double timeScale = 1.0f;

    /**
     * The unscaled time elapsed since the last frame, in seconds.
     */
    private static float deltaTime;

    /**
     * The total accumulated, unscaled time since the game started running, in seconds.
     */
    private static float time;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Time() {}

    /**
     * Gets the delta time scaled by the current time scale.
     * This is the value most game logic should use for movement and calculations to respect slow-motion or pauses.
     *
     * @return The scaled delta time in seconds.
     */
    public static float deltaTime() {
        return (float) (deltaTime * timeScale);
    }

    /**
     * Gets the actual, real-world delta time since the last frame, ignoring the time scale.
     * Useful for UI animations or systems that should not freeze when the game is paused.
     *
     * @return The unscaled delta time in seconds.
     */
    public static float unscaledDeltaTime() {
        return deltaTime;
    }

    /**
     * Updates the global delta time and accumulates the total time.
     * This should typically only be called by the core game loop.
     *
     * @param dt The new delta time in seconds.
     */
    public static void setDeltaTime(float dt) {
        deltaTime = dt;
        time += dt;
    }

    /**
     * Retrieves the current time scale multiplier.
     *
     * @return The current time scale.
     */
    public static double getTimeScale() {
        return timeScale;
    }

    /**
     * Sets a new time scale multiplier to speed up, slow down, or pause (by setting to 0) the scaled game time.
     *
     * @param timeScale The new time scale value.
     */
    public static void setTimeScale(double timeScale) {
        Time.timeScale = timeScale;
    }

    /**
     * Retrieves the total unscaled time elapsed since the application began updating time.
     *
     * @return The total elapsed time in seconds.
     */
    public static float getTime() {
        return time;
    }
}
