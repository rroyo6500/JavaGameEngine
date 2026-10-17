package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Dimension;

/**
 * Immutable camera transform and world-space bounds for one render viewport.
 * Screen coordinates are centered around {@code centerX}/{@code centerY}; camera
 * coordinates and view bounds are expressed in world units. The camera position
 * contributes a world-space translation to the viewport transform.
 *
 * @param centerX horizontal center of the viewport in screen pixels
 * @param centerY vertical center of the viewport in screen pixels
 * @param zoom positive world-to-screen scale factor
 * @param camX horizontal world-space translation
 * @param camY vertical world-space translation
 * @param viewWorldX left edge of the visible world rectangle
 * @param viewWorldY top edge of the visible world rectangle
 * @param viewWorldWidth visible world width
 * @param viewWorldHeight visible world height
 */
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

    /**
     * Calculates the visible world rectangle and transform from a camera and viewport size.
     * Zoom changes the amount of world space visible while the screen-space center remains fixed.
     *
     * @param camera camera position and zoom to read
     * @param windowDimension viewport dimensions in pixels
     * @return a snapshot of the transform and visible world bounds
     */
    public static ViewportParams calculateViewport(Camera camera, Dimension windowDimension) {
        double centerX = windowDimension.getHalfWidth();
        double centerY = windowDimension.getHalfHeight();
        double zoom = camera.getZoom();
        double camX = camera.getCameraPosition().getX();
        double camY = camera.getCameraPosition().getY();

        double viewWorldX = centerX - centerX / zoom - camX;
        double viewWorldY = centerY - centerY / zoom - camY;
        double viewWorldWidth = windowDimension.getWidth() / zoom;
        double viewWorldHeight = windowDimension.getHeight() / zoom;

        return new ViewportParams(centerX, centerY, zoom, camX, camY, viewWorldX, viewWorldY, viewWorldWidth, viewWorldHeight);
    }

    /**
     * Converts a world-space X coordinate to a screen-space coordinate.
     *
     * @param worldX the X coordinate in world units
     * @return the corresponding X coordinate in screen pixels
     */
    public double worldToScreenX(double worldX) {
        return ((worldX + camX) - centerX) * zoom + centerX;
    }

    /**
     * Converts a world-space Y coordinate to a screen-space coordinate.
     *
     * @param worldY the Y coordinate in world units
     * @return the corresponding Y coordinate in screen pixels
     */
    public double worldToScreenY(double worldY) {
        return ((worldY + camY) - centerY) * zoom + centerY;
    }

}
