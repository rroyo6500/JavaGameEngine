package rroyo.jge.core.gameessentials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.testing.TestScene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies per-scene entity registration and snapshot behavior. */
class PhysicsEngineTest {
    /** Scene fixture shared by each test in this class. */
    private TestScene scene;

    /** Creates a fresh scene registry fixture for a test. */
    @BeforeEach
    void setUp() {
        scene = new TestScene();
    }

    /** Removes the test scene's entities after each test. */
    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    /** Confirms construction automatically registers an entity with its owner scene. */
    @Test
    void constructionRegistersObjectInItsScene() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);

        assertTrue(PhysicsEngine.contains(object));
        assertEquals(List.of(object), PhysicsEngine.getGameObjects(scene));
    }

    /** Confirms duplicate registration preserves the rest of the scene's entities. */
    @Test
    void registeringAnExistingObjectDoesNotDiscardOtherSceneObjects() {
        PositionableGameObject first = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject second = new PositionableGameObject(scene, 20, 0, 10, 10);

        PhysicsEngine.add(first); // A repeat registration must be harmless.

        assertEquals(2, PhysicsEngine.getGameObjects(scene).size(),
                "Re-registering one entity must not replace the scene's entire entity list");
        assertTrue(PhysicsEngine.getGameObjects(scene).contains(first));
        assertTrue(PhysicsEngine.getGameObjects(scene).contains(second));
    }

    /** Confirms callers cannot mutate the engine's internal entity list through a snapshot. */
    @Test
    void returnedEntityListCannotModifyInternalRegistry() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        List<PositionableGameObject> objects = PhysicsEngine.getGameObjects(scene);

        assertThrows(UnsupportedOperationException.class, objects::clear);
        assertTrue(PhysicsEngine.contains(object));
    }

    /** Confirms entities registered to different scenes remain isolated. */
    @Test
    void scenesHaveIndependentRegistries() {
        TestScene otherScene = new TestScene();
        try {
            PositionableGameObject first = new PositionableGameObject(scene, 0, 0, 10, 10);
            PositionableGameObject second = new PositionableGameObject(otherScene, 0, 0, 10, 10);

            assertEquals(List.of(first), PhysicsEngine.getGameObjects(scene));
            assertEquals(List.of(second), PhysicsEngine.getGameObjects(otherScene));
        } finally {
            PhysicsEngine.remove(otherScene);
        }
    }

    /** Confirms removing an object after clearing its scene is harmless. */
    @Test
    void removingAnObjectAfterItsSceneWasRemovedDoesNotThrow() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        PhysicsEngine.remove(scene);

        assertDoesNotThrow(() -> PhysicsEngine.remove(object));
    }
}
