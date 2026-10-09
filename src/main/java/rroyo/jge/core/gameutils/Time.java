package rroyo.jge.core.gameutils;

public final class Time {

    private static double timeScale = 1.0f;
    private static float deltaTime;
    private static float time;

    private Time() {
    }

    public static float deltaTime() {
        return (float) (deltaTime * timeScale);
    }

    public static float unscaledDeltaTime() {
        return deltaTime;
    }

    public static void setDeltaTime(float dt) {
        deltaTime = dt;
        time += dt;
    }

    public static double getTimeScale() {
        return timeScale;
    }

    public static void setTimeScale(double timeScale) {
        Time.timeScale = timeScale;
    }

    public static float getTime() {
        return time;
    }
}
