package rroyo.jgameengine.core.gameutils;

import java.util.HashMap;
import java.util.Map;

public final class Time {

    private static final Map<String, Float> waiters = new HashMap<>();

    private static float deltaTime;

    public static boolean wait(String id, float seconds) {
        if (!waiters.containsKey(id)) {
            waiters.put(id, seconds);
        }

        float remaining = waiters.get(id) - deltaTime();

        if (remaining <= 0) {
            waiters.remove(id);
            return true;
        } else {
            waiters.put(id, remaining);
            return false;
        }
    }

    public static float deltaTime() {
        return deltaTime;
    }

    public static void setDeltaTime(float dt) {
        deltaTime = dt;
    }
}
