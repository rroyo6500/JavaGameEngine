package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.testing.TestScene;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies precise overlap calculations for axis-aligned and rotated rectangles. */
class OverlapTest {
    /** Scene fixture that owns the positionable objects used by each test. */
    private final TestScene scene = new TestScene();

    /** Removes the fixture's entities after each test. */
    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    /** Confirms fractional overlap values retain precision for center coordinates. */
    @Test
    void calculatesFractionalOverlapWithoutTruncatingCoordinates() {
        PositionableGameObject a = new PositionableGameObject(scene, 0.9, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 10.1, 0, 10, 10);

        Overlap overlap = Overlap.calculateOverlap(a, b);

        assertNotNull(overlap);
        assertEquals(0.8, overlap.overlapX(), 0.0001);
        assertTrue(Overlap.ov(a, b));
    }

    /** Confirms small valid overlaps are not rounded away. */
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

    /** Confirms edge contact without penetration is not treated as an overlap. */
    @Test
    void touchingAtAnEdgeIsNotAnOverlap() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 10, 0, 10, 10);

        assertNull(Overlap.calculateOverlap(a, b));
        assertFalse(Overlap.ov(a, b));
    }

    /** Confirms deleted entities are excluded from overlap calculations. */
    @Test
    void deletedObjectsDoNotOverlap() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 10);
        PositionableGameObject b = new PositionableGameObject(scene, 1, 0, 10, 10);
        a.delete();

        assertNull(Overlap.calculateOverlap(a, b));
        assertFalse(Overlap.ov(a, b));
    }

    /** Confirms rotated intersection results include a unit separation normal. */
    @Test
    void rotatedRectanglesOverlapAndReturnAUnitSeparationNormal() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 2);
        PositionableGameObject b = new PositionableGameObject(scene, 0, 1, 10, 2);
        a.setRotation(Math.PI / 4);
        b.setRotation(Math.PI / 4);

        Overlap overlap = Overlap.calculateOverlap(a, b);

        assertNotNull(overlap);
        assertTrue(Overlap.ov(a, b));
        assertEquals(1, Math.hypot(overlap.normalX(), overlap.normalY()), 0.000001);
        assertTrue(overlap.penetration() > 0);
        assertEquals(Math.sqrt(0.5), Math.abs(overlap.normalX()), 0.000001);
        assertEquals(Math.sqrt(0.5), Math.abs(overlap.normalY()), 0.000001);
    }

    /** Confirms SAT rejects separated shapes even when their enclosing AABBs intersect. */
    @Test
    void rotatedRectanglesCanHaveOverlappingAabbsWithoutColliding() {
        PositionableGameObject a = new PositionableGameObject(scene, 0, 0, 10, 2);
        PositionableGameObject b = new PositionableGameObject(scene, 0, 5, 10, 2);
        a.setRotation(Math.PI / 4);
        b.setRotation(Math.PI / 4);

        assertTrue(a.getBounds().intersects(b.getBounds()), "The enclosing AABBs overlap");
        assertNull(Overlap.calculateOverlap(a, b), "SAT should detect the gap along the rectangles' short axis");
        assertFalse(Overlap.ov(a, b));
    }
}
