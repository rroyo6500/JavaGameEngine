package rroyo.jge.core.gameobjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.testing.TestScene;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private final TestScene scene = new TestScene();

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    @Test
    void groupDeduplicatesRepeatedMembers() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);

        Group group = new Group(object, object);

        assertEquals(1, group.getMembers().size());
    }

    @Test
    void creatingGroupDoesNotChangeSceneRegistry() {
        PositionableGameObject first = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject second = new PositionableGameObject(scene, 20, 0, 10, 10);
        int registeredBefore = PhysicsEngine.getGameObjects(scene).size();

        new Group(first, second);

        assertEquals(registeredBefore, PhysicsEngine.getGameObjects(scene).size());
    }

    @Test
    void memberListIsAnImmutableSnapshot() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        Group group = new Group(object);

        assertThrows(UnsupportedOperationException.class, () -> group.getMembers().clear());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    void deletingGroupClearsOnlyItsMembership() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        Group group = new Group(object);

        group.delete();

        assertTrue(group.getMembers().isEmpty());
        assertTrue(PhysicsEngine.contains(object), "Deleting a group must not delete its member entity");
    }
}
