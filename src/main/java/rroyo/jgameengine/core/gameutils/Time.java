package rroyo.jgameengine.core.gameutils;

import java.util.HashMap;
import java.util.Map;

public final class Time {

    private static float timeScale = 1.0f;
    private static float deltaTime;
    private static float time;

    private Time() {
    }

    public static float deltaTime() {
        return deltaTime * timeScale;
    }

    public static float unscaledDeltaTime() {
        return deltaTime;
    }

    public static void setDeltaTime(float dt) {
        deltaTime = dt;
        time += dt;
    }

    public static float getTimeScale() {
        return timeScale;
    }

    public static void setTimeScale(float timeScale) {
        Time.timeScale = timeScale;
    }

    public static float getTime() {
        return time;
    }
}
