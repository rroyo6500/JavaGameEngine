package rroyo.jge.core.gameobjects.positionable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.interfaces.Script;
import rroyo.jge.testing.TestScene;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies script lifecycle callbacks and immutable script snapshots. */
class ScriptableGameObjectTest {
    /** Scene fixture that owns the scriptable test entities. */
    private final TestScene scene = new TestScene();

    /** Removes registered entities after each test. */
    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    /** Confirms start runs once and each explicit update call is dispatched. */
    @Test
    void startRunsOnlyOnceAndUpdateRunsOnEveryCall() {
        AtomicInteger starts = new AtomicInteger();
        AtomicInteger updates = new AtomicInteger();
        Script script = new Script() {
            @Override public void start(rroyo.jge.core.gameobjects.GameObject self) { starts.incrementAndGet(); }
            @Override public void update(rroyo.jge.core.gameobjects.GameObject self) { updates.incrementAndGet(); }
        };
        ScriptableGameObject object = new ScriptableGameObject(scene, 0, 0, 10, 10, script);

        object.start();
        object.start();
        object.update();
        object.update();

        assertEquals(1, starts.get(), "Each object's start hook should be called exactly once");
        assertEquals(2, updates.get(), "Update should run once for every explicit update call");
    }

    /** Confirms scripts added after startup receive one start callback. */
    @Test
    void scriptsAddedAfterStartAreInitializedExactlyOnce() {
        AtomicInteger starts = new AtomicInteger();
        Script lateScript = new Script() {
            @Override public void start(rroyo.jge.core.gameobjects.GameObject self) { starts.incrementAndGet(); }
            @Override public void update(rroyo.jge.core.gameobjects.GameObject self) { }
        };
        ScriptableGameObject object = new ScriptableGameObject(scene, 0, 0, 10, 10);

        object.start();
        object.addScript(lateScript);
        object.start();

        assertEquals(1, starts.get(),
                "A script attached after its object has started should receive its own start callback");
    }

    /** Confirms the scripts accessor cannot mutate the object's internal list. */
    @Test
    void scriptsAccessorReturnsAnImmutableSnapshot() {
        Script script = new Script() {
            @Override public void start(rroyo.jge.core.gameobjects.GameObject self) { }
            @Override public void update(rroyo.jge.core.gameobjects.GameObject self) { }
        };
        ScriptableGameObject object = new ScriptableGameObject(scene, 0, 0, 10, 10, script);

        assertThrows(UnsupportedOperationException.class, () -> object.getScripts().clear());
        assertEquals(1, object.getScripts().size());
    }
}
