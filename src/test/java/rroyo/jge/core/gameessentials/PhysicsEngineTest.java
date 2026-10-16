package rroyo.jge.core.gameessentials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.testing.TestScene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhysicsEngineTest {
    private TestScene scene;

    @BeforeEach
    void setUp() {
        scene = new TestScene();
    }

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    @Test
    void constructionRegistersObjectInItsScene() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);

        assertTrue(PhysicsEngine.contains(object));
        assertEquals(List.of(object), PhysicsEngine.getGameObjects(scene));
    }

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

    @Test
    void returnedEntityListCannotModifyInternalRegistry() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        List<PositionableGameObject> objects = PhysicsEngine.getGameObjects(scene);

        assertThrows(UnsupportedOperationException.class, objects::clear);
        assertTrue(PhysicsEngine.contains(object));
    }

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

    @Test
    void removingAnObjectAfterItsSceneWasRemovedDoesNotThrow() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        PhysicsEngine.remove(scene);

        assertDoesNotThrow(() -> PhysicsEngine.remove(object));
    }
}
