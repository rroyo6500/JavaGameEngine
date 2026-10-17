package rroyo.jge.core.gameessentials;

import org.junit.jupiter.api.Test;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Dimension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Verifies viewport calculations use the supplied camera and view dimensions. */
class ViewportParamsTest {
    /** Confirms camera position and zoom determine the viewport's world-space bounds. */
    @Test
    void viewportUsesTheCameraInstancePassedByTheWindow() {
        Camera camera = new Camera();
        camera.setPosition(100, 50);
        camera.setZoom(2.0);

        ViewportParams viewport = ViewportParams.calculateViewport(camera, new Dimension(800, 600));

        assertEquals(2.0, viewport.zoom(), 0.0001);
        assertEquals(100.0, viewport.camX(), 0.0001);
        assertEquals(50.0, viewport.camY(), 0.0001);
        assertEquals(100.0, viewport.viewWorldX(), 0.0001);
        assertEquals(100.0, viewport.viewWorldY(), 0.0001);
        assertEquals(400.0, viewport.viewWorldWidth(), 0.0001);
        assertEquals(300.0, viewport.viewWorldHeight(), 0.0001);
    }
}
