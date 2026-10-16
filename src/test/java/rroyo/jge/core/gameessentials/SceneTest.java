package rroyo.jge.core.gameessentials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;
import rroyo.jge.interfaces.Script;
import rroyo.jge.testing.TestScene;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SceneTest {
    private final TestScene scene = new TestScene();

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    @Test
    void processFrameStartsScriptOnceAndUpdatesEveryFrame() {
        AtomicInteger starts = new AtomicInteger();
        AtomicInteger updates = new AtomicInteger();
        Script script = new Script() {
            @Override public void start(GameObject self) { starts.incrementAndGet(); }
            @Override public void update(GameObject self) { updates.incrementAndGet(); }
        };
        new ScriptableGameObject(scene, 0, 0, 10, 10, script);

        scene.processFrame();
        scene.processFrame();

        assertEquals(1, starts.get());
        assertEquals(2, updates.get());
        assertEquals(2, scene.getUpdateCalls());
    }

    @Test
    void deletedEntityIsRemovedAfterFrameProcessing() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        object.delete();

        scene.processFrame();

        assertFalse(PhysicsEngine.getGameObjects(scene).contains(object));
    }

    @Test
    void clearRemovesEntitiesFromThisScene() {
        TestScene otherScene = new TestScene();
        try {
            PositionableGameObject first = new PositionableGameObject(scene, 0, 0, 10, 10);
            PositionableGameObject second = new PositionableGameObject(otherScene, 0, 0, 10, 10);

            scene.clear();

            assertTrue(PhysicsEngine.getGameObjects(scene).isEmpty());
            assertTrue(PhysicsEngine.getGameObjects(otherScene).contains(second));
            assertFalse(PhysicsEngine.getGameObjects(scene).contains(first));
        } finally {
            PhysicsEngine.remove(otherScene);
        }
    }
}
