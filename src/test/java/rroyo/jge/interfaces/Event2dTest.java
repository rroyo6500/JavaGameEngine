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

class Event2dTest {
    private final TestScene scene = new TestScene();

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
    }

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

    private static Script scriptWithOverlapCounter(AtomicInteger counter) {
        return new Script() {
            @Override public void start(GameObject self) { }
            @Override public void update(GameObject self) { }
            @Override public void onOverlap(GameObject self, GameObject objective) { counter.incrementAndGet(); }
        };
    }

    private static Script scriptWithRangeCounter(AtomicInteger counter) {
        return new Script() {
            @Override public void start(GameObject self) { }
            @Override public void update(GameObject self) { }
            @Override public void onInRange(GameObject self, GameObject objective) { counter.incrementAndGet(); }
        };
    }
}
