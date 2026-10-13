package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Dimension;

public record ViewportParams(
        double centerX,
        double centerY,
        double zoom,
        double camX,
        double camY,

        double viewWorldX,
        double viewWorldY,
        double viewWorldWidth,
        double viewWorldHeight
) {

    public static ViewportParams calculateViewport(Camera camera, Dimension windowDimension) {
        double centerX = windowDimension.getHalfWidth();
        double centerY = windowDimension.getHalfHeight();
        double zoom = camera.getZoom();
        double camX = camera.getCameraPosition().getX();
        double camY = camera.getCameraPosition().getY();

        double viewWorldX = centerX - (centerX / zoom) - camX;
        double viewWorldY = centerY - (centerY / zoom) - camY;
        double viewWorldWidth = windowDimension.getWidth() / zoom;
        double viewWorldHeight = windowDimension.getHeight() / zoom;

        return new ViewportParams(centerX, centerY, zoom, camX, camY, viewWorldX, viewWorldY, viewWorldWidth, viewWorldHeight);
    }

}
