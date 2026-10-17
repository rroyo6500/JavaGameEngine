package rroyo.jge.core.gameobjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.testing.TestScene;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies group membership, immutability, and interaction with scene registration. */
class GroupTest {
    /** Scene fixture used to create the member entities. */
    private final TestScene scene = new TestScene();

    /** Removes the fixture's registered entities after each test. */
    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    /** Confirms adding the same entity more than once produces one member. */
    @Test
    void groupDeduplicatesRepeatedMembers() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);

        Group group = new Group(object, object);

        assertEquals(1, group.getMembers().size());
    }

    /** Confirms creating a group does not register or unregister its members. */
    @Test
    void creatingGroupDoesNotChangeSceneRegistry() {
        PositionableGameObject first = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject second = new PositionableGameObject(scene, 20, 0, 10, 10);
        int registeredBefore = PhysicsEngine.getGameObjects(scene).size();

        new Group(first, second);

        assertEquals(registeredBefore, PhysicsEngine.getGameObjects(scene).size());
    }

    /** Confirms the members accessor returns a snapshot that cannot be modified. */
    @Test
    void memberListIsAnImmutableSnapshot() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        Group group = new Group(object);

        assertThrows(UnsupportedOperationException.class, () -> group.getMembers().clear());
        assertEquals(1, group.getMembers().size());
    }

    /** Confirms deleting a group clears membership without deleting its entities. */
    @Test
    void deletingGroupClearsOnlyItsMembership() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        Group group = new Group(object);

        group.delete();

        assertTrue(group.getMembers().isEmpty());
        assertTrue(PhysicsEngine.contains(object), "Deleting a group must not delete its member entity");
    }
}
