package rroyo.jge.core.gameobjects.positionable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameutils.Time;
import rroyo.jge.testing.TestScene;
import rroyo.jge.testing.TestState;

import static org.junit.jupiter.api.Assertions.*;

class PositionableGameObjectTest {
    private final TestScene scene = new TestScene();

    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
        TestState.resetTime();
    }

    @Test
    void manualMoveAppliesTheRequestedDisplacement() {
        PositionableGameObject object = new PositionableGameObject(scene, 10, 20, 8, 6);

        object.move(-3.5, 4.25);

        assertEquals(6.5, object.getPosition().getX(), 0.0001);
        assertEquals(24.25, object.getPosition().getY(), 0.0001);
    }

    @Test
    void deletedObjectCannotMove() {
        PositionableGameObject object = new PositionableGameObject(scene, 10, 20, 8, 6);
        object.delete();

        object.move(100, 100);

        assertEquals(10, object.getPosition().getX(), 0.0001);
        assertEquals(20, object.getPosition().getY(), 0.0001);
    }

    @Test
    void velocityMovementUsesScaledDeltaTime() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 10);
        object.setVelocity(8, -4);
        Time.setTimeScale(0.5);
        Time.setDeltaTime(0.25f);

        object.move();

        assertEquals(1.0, object.getPosition().getX(), 0.0001,
                "8 units/second * 0.25 seconds * 0.5 time scale = 1 unit");
        assertEquals(-0.5, object.getPosition().getY(), 0.0001);
    }
}
