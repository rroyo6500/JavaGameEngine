package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.testing.TestScene;

import static org.junit.jupiter.api.Assertions.*;

class OverlapTest {
    private final TestScene scene = new TestScene();

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    @Test
    void calculatesFractionalOverlapWithoutTruncatingCoordinates() {
        PositionableGameObject a = new PositionableGameObject(scene, 0.9, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 10.1, 0, 10, 10);

        Overlap overlap = Overlap.calculateOverlap(a, b);

        assertNotNull(overlap);
        assertEquals(0.8, overlap.overlapX(), 0.0001);
        assertTrue(Overlap.ov(a, b));
    }

    @Test
    void doesNotRoundAwayAValidSubCentUnitOverlap() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 9.999, 0, 10, 10);

        Overlap overlap = Overlap.calculateOverlap(a, b);

        assertNotNull(overlap);
        assertEquals(0.001, overlap.overlapX(), 0.0001,
                "The physics result should preserve precision instead of rounding to two decimals");
        assertTrue(Overlap.ov(a, b));
    }

    @Test
    void touchingAtAnEdgeIsNotAnOverlap() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 10, 0, 10, 10);

        assertNull(Overlap.calculateOverlap(a, b));
        assertFalse(Overlap.ov(a, b));
    }

    @Test
    void deletedObjectsDoNotOverlap() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 1, 0, 10, 10);
        a.delete();

        assertNull(Overlap.calculateOverlap(a, b));
        assertFalse(Overlap.ov(a, b));
    }
}
