package rroyo.jge.core.gameobjects.positionable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.interfaces.Script;
import rroyo.jge.testing.TestScene;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ScriptableGameObjectTest {
    private final TestScene scene = new TestScene();

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

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
