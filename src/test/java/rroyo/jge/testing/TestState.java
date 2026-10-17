package rroyo.jge.testing;

import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameutils.Time;

import java.lang.reflect.Field;
import java.util.Collection;

/** Helpers for resetting static engine state between otherwise isolated tests. */
public final class TestState {
    private TestState() {}

    /**
     * Removes all registered entities for the supplied scene when it is non-null.
     *
     * @param scene scene whose registrations should be removed
     */
    public static void removeScene(Scene scene) {
        if (scene != null) {
            PhysicsEngine.remove(scene);
        }
    }

    /** Restores global time scale and frame delta to their default test values. */
    public static void resetTime() {
        Time.setTimeScale(1.0);
        Time.setDeltaTime(0f);
    }

    /**
     * Timer currently has a global registry but no public reset/removal API for tests.
     * Reflection is confined to this test helper so that each timer test starts clean.
     */
    public static void resetTimers() {
        clearStaticCollection("timers");
        clearStaticCollection("pendingAdd");
    }

    /**
     * Clears a named static collection in {@code Timer} for test isolation.
     *
     * @param fieldName name of the timer registry field to clear
     */
    private static void clearStaticCollection(String fieldName) {
        try {
            Field field = Class.forName("rroyo.jge.core.gameutils.Timer").getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(null);
            if (value instanceof Collection<?> collection) {
                collection.clear();
            } else {
                throw new IllegalStateException("Timer field is not a Collection: " + fieldName);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to reset Timer test state: " + fieldName, e);
        }
    }
}
