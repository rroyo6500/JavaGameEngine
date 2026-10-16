package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CameraTest {

    @Test
    void movementAndPositionUpdatesAreAdditiveAndAbsoluteAsNamed() {
        Camera camera = new Camera();

        camera.move(12, -7);
        camera.moveX(-2);
        camera.setY(9);

        assertEquals(10, camera.getCameraPosition().getX());
        assertEquals(9, camera.getCameraPosition().getY());
    }

    @Test
    void settingWindowDimensionsCentersCameraOnHalfDimensions() {
        Camera camera = new Camera();
        camera.setWindowDimensions(new Dimension(801, 601));

        assertEquals(400, camera.getCameraPosition().getX());
        assertEquals(300, camera.getCameraPosition().getY());
    }

    @Test
    void zoomIsClampedToAPositiveMinimum() {
        Camera camera = new Camera();
        camera.setZoom(-3);

        assertEquals(0.01, camera.getZoom(), 0.000001);
    }
}
