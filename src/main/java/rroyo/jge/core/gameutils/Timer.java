package rroyo.jge.core.gameutils;

import java.util.*;

public final class Timer {

    private static final List<Timer> timers = new ArrayList<>();
    private static final List<Timer> pendingAdd = new ArrayList<>();

    private float remaining;
    private final Runnable callback;
    private final boolean loop;
    private boolean cancelled;
    private final float initialDuration;

    public Timer(float seconds, Runnable callback, boolean loop) {
        this.initialDuration = seconds;
        this.remaining = seconds;
        this.callback = callback;
        this.loop = loop;
    }

    public void update(float dt) {
        if (cancelled) return;

        remaining -= dt;
        if (remaining <= 0) {
            if (callback != null) callback.run();
            if (loop) remaining = initialDuration;
        }
    }

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

    public static Timer schedule(float seconds, Runnable callback) {
        return schedule(seconds, callback, false);
    }

    public static Timer schedule(float seconds, Runnable callback, boolean loop) {
        Timer timer = new Timer(seconds, callback, loop);
        pendingAdd.add(timer);
        return timer;
    }

    public boolean isFinished() {
        return remaining <= 0 && !loop;
    }

    public void cancel() {
        this.cancelled = true;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(remaining, callback, loop, cancelled, initialDuration);
    }
}
