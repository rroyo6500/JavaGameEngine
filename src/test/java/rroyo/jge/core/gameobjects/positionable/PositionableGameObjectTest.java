package rroyo.jge.core.gameobjects.positionable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameessentials.PhysicsEngine;
import rroyo.jge.core.gameutils.Time;
import rroyo.jge.testing.TestScene;
import rroyo.jge.testing.TestState;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies movement, rotation, bounds, and velocity behavior for positionable entities. */
class PositionableGameObjectTest {
    /** Scene fixture that owns the entities created by these tests. */
    private final TestScene scene = new TestScene();

    /** Clears scene registration and global time state after each test. */
    @AfterEach
    void tearDown() {
        PhysicsEngine.remove(scene);
        TestState.resetTime();
    }

    /** Confirms direct movement applies the requested world-space offset. */
    @Test
    void manualMoveAppliesTheRequestedDisplacement() {
        PositionableGameObject object = new PositionableGameObject(scene, 10, 20, 8, 6);

        object.move(-3.5, 4.25);

        assertEquals(6.5, object.getPosition().getX(), 0.0001);
        assertEquals(24.25, object.getPosition().getY(), 0.0001);
    }

    /** Confirms deleted entities ignore direct movement. */
    @Test
    void deletedObjectCannotMove() {
        PositionableGameObject object = new PositionableGameObject(scene, 10, 20, 8, 6);
        object.delete();

        object.move(100, 100);

        assertEquals(10, object.getPosition().getX(), 0.0001);
        assertEquals(20, object.getPosition().getY(), 0.0001);
    }

    /** Confirms linear velocity uses the scaled frame delta. */
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

    /** Confirms orientation can be set and incremented using radians or degrees. */
    @Test
    void rotationCanBeSetAndChangedInRadiansOrDegrees() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 4);

        object.setRotationDegrees(30);
        object.rotateDegrees(15);

        assertEquals(45, object.getRotationDegrees(), 0.000001);
        assertEquals(Math.PI / 4, object.getRotation(), 0.000001);

        object.rotate(Math.PI / 4);

        assertEquals(90, object.getRotationDegrees(), 0.000001);
    }

    /** Confirms equivalent rotations are normalized into the supported angular range. */
    @Test
    void rotationIsNormalizedToAnEquivalentTurn() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 4);

        object.setRotation(3 * Math.PI / 2);

        assertEquals(-Math.PI / 2, object.getRotation(), 0.000001);
        assertEquals(-90, object.getRotationDegrees(), 0.000001);
    }

    /** Confirms angular velocity is integrated using the scaled frame delta. */
    @Test
    void angularVelocityUsesScaledFrameDelta() {
        PositionableGameObject object = new PositionableGameObject(scene, 0, 0, 10, 4);
        object.setAngularVelocity(4);
        Time.setTimeScale(0.5);
        Time.setDeltaTime(0.25f);

        object.move();

        assertEquals(0.5, object.getRotation(), 0.000001,
                "4 radians/second * 0.25 seconds * 0.5 time scale = 0.5 radians");
        assertEquals(4, object.getAngularVelocity(), 0.000001);
    }

    /** Confirms returned bounds enclose the rectangle after a quarter turn. */
    @Test
    void boundsEncloseTheRotatedRectangle() {
        PositionableGameObject object = new PositionableGameObject(scene, 10.25, -4.75, 8, 2);
        object.setRotation(Math.PI / 2);

        assertEquals(new Rectangle(9, -9, 3, 9), object.getBounds());
    }
}
