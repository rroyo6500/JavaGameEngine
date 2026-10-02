package rroyo.jgameengine.objects.gameutils;

import java.util.HashMap;
import java.util.Map;

public final class FrameWaiter {

    private static final Map<String, Integer> frameWaiters = new HashMap<>();

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
