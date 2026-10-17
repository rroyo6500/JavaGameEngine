package rroyo.jge.interfaces;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.assets.Sprite;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.testing.TestScene;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies overlap, collision, proximity, and script callback behavior. */
class Event2dTest {
    /** Scene fixture that owns all positionable entities created by the tests. */
    private final TestScene scene = new TestScene();

    /** Removes the test scene's entity registrations after each test. */
    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

    /** Confirms overlap events are dispatched once to both scriptable entities. */
    @Test
    void overlapNotifiesBothScriptableObjectsOnce() {
        AtomicInteger firstEvents = new AtomicInteger();
        AtomicInteger secondEvents = new AtomicInteger();
        Script firstScript = scriptWithOverlapCounter(firstEvents);
        Script secondScript = scriptWithOverlapCounter(secondEvents);
        SpriteGameObject first = new SpriteGameObject(scene, 0, 0, 20, 20, new Sprite(java.awt.Color.RED), firstScript);
        SpriteGameObject second = new SpriteGameObject(scene, 5, 0, 20, 20, new Sprite(java.awt.Color.BLUE), secondScript);

        assertTrue(((Event2d) first).overlap(second));
        assertEquals(1, firstEvents.get());
        assertEquals(1, secondEvents.get());
    }

    /** Confirms axis-aligned collision resolution separates the mover and stops its normal velocity. */
    @Test
    void collisionSeparatesObjectsAlongMinimumPenetrationAxisAndStopsThatVelocity() {
        SpriteGameObject moving = new SpriteGameObject(scene, 0, 0, 10, 20, new Sprite(java.awt.Color.RED));
        SpriteGameObject obstacle = new SpriteGameObject(scene, 8, 0, 10, 20, new Sprite(java.awt.Color.BLUE));
        moving.setVelocity(4, 6);

        assertTrue(((Event2d) moving).collide(obstacle));

        assertEquals(-2, moving.getPosition().getX(), 0.000001);
        assertEquals(0, moving.getVelocityX());
        assertEquals(6, moving.getVelocityY());
        assertFalse(((Event2d) moving).overlap(obstacle));
    }

    /** Confirms rotated collision resolution uses the contact normal for position and velocity. */
    @Test
    void rotatedCollisionSeparatesAlongItsNormalAndRemovesNormalVelocity() {
        SpriteGameObject moving = new SpriteGameObject(scene, 0, 0, 10, 2,
                new Sprite(java.awt.Color.RED));
        SpriteGameObject obstacle = new SpriteGameObject(scene, 0, 0, 10, 2,
                new Sprite(java.awt.Color.BLUE));
        moving.setRotation(Math.PI / 4);
        obstacle.setRotation(Math.PI / 4);
        moving.setVelocity(0, 4);

        assertTrue(((Event2d) moving).collide(obstacle));

        assertNotEquals(0, moving.getPosition().getX(), 0.000001);
        assertNotEquals(0, moving.getPosition().getY(), 0.000001);
        assertFalse(((Event2d) moving).overlap(obstacle));

        double normalVelocity = moving.getVelocityX() * -Math.sqrt(0.5)
                + moving.getVelocityY() * Math.sqrt(0.5);
        assertEquals(0, normalVelocity, 0.000001);
    }

    /** Confirms proximity callbacks can run when entities are near but do not overlap. */
    @Test
    void proximityCallbackRunsForBothObjectsWithoutRequiringOverlap() {
        AtomicInteger firstEvents = new AtomicInteger();
        AtomicInteger secondEvents = new AtomicInteger();
        SpriteGameObject first = new SpriteGameObject(scene, 0, 0, 4, 4,
                new Sprite(java.awt.Color.RED), scriptWithRangeCounter(firstEvents));
        SpriteGameObject second = new SpriteGameObject(scene, 6, 0, 4, 4,
                new Sprite(java.awt.Color.BLUE), scriptWithRangeCounter(secondEvents));

        assertTrue(((Event2d) first).inRange(second));
        assertFalse(((Event2d) first).overlap(second));
        assertEquals(1, firstEvents.get());
        assertEquals(1, secondEvents.get());
    }

    /**
     * Creates a script that increments a counter whenever an overlap callback is received.
     *
     * @param counter counter to increment from the overlap callback
     * @return script that counts overlap callbacks
     */
    private static Script scriptWithOverlapCounter(AtomicInteger counter) {
        return new Script() {
            @Override public void start(GameObject self) { }
            @Override public void update(GameObject self) { }
            @Override public void onOverlap(GameObject self, GameObject objective) { counter.incrementAndGet(); }
        };
    }

    /**
     * Creates a script that increments a counter whenever an in-range callback is received.
     *
     * @param counter counter to increment from the in-range callback
     * @return script that counts in-range callbacks
     */
    private static Script scriptWithRangeCounter(AtomicInteger counter) {
        return new Script() {
            @Override public void start(GameObject self) { }
            @Override public void update(GameObject self) { }
            @Override public void onInRange(GameObject self, GameObject objective) { counter.incrementAndGet(); }
        };
    }
}
