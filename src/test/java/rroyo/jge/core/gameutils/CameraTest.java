package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Verifies camera positioning, viewport centering, and zoom constraints. */
class CameraTest {

    /** Confirms relative movement and absolute position setters update the expected axes. */
    @Test
    void movementAndPositionUpdatesAreAdditiveAndAbsoluteAsNamed() {
        Camera camera = new Camera();

        camera.move(12, -7);
        camera.moveX(-2);
        camera.setY(9);

        assertEquals(10, camera.getCameraPosition().getX());
        assertEquals(9, camera.getCameraPosition().getY());
    }

    /** Confirms window dimensions center the camera on their half-width and half-height. */
    @Test
    void settingWindowDimensionsCentersCameraOnHalfDimensions() {
        Camera camera = new Camera();
        camera.setWindowDimensions(new Dimension(801, 601));

        assertEquals(400, camera.getCameraPosition().getX());
        assertEquals(300, camera.getCameraPosition().getY());
    }

    /** Confirms invalid zoom values are clamped to the supported positive minimum. */
    @Test
    void zoomIsClampedToAPositiveMinimum() {
        Camera camera = new Camera();
        camera.setZoom(-3);

        assertEquals(0.01, camera.getZoom(), 0.000001);
    }
}
