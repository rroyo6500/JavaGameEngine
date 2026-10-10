package rroyo.jge.core.gameutils;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code FrameWaiter} class provides a frame-based delay mechanism.
 * Instead of waiting for real-world time to pass, it counts down a specified number of frames.
 * This is useful for delaying actions in game logic that are dependent on the game loop's cycles.
 */
public final class FrameWaiter {

    /**
     * A map storing active frame wait requests.
     * The key is a unique string identifier, and the value is the number of frames remaining to wait.
     */
    private static final Map<String, Integer> frameWaiters = new HashMap<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FrameWaiter() {}

    /**
     * Checks if a specified delay in frames has elapsed for a given identifier.
     * When called for the first time with a new ID, it registers the delay and returns false.
     * Subsequent calls decrement the delay until it reaches zero, at which point it returns true
     * and clears the identifier.
     *
     * @param id    A unique string identifier for the operation waiting.
     * @param delay The amount of frames to wait.
     * @return {@code true} if the specified number of frames has passed; {@code false} if it is still waiting.
     */
    public static boolean wait(String id, int delay) {
        if (!frameWaiters.containsKey(id)) {
            frameWaiters.put(id, delay);
        } else {
            int remaining = frameWaiters.get(id);
            if (remaining > 0) {
                frameWaiters.put(id, remaining - 1);
            } else {
                frameWaiters.remove(id);
                return true;
            }
        }
        return false;
    }

}
