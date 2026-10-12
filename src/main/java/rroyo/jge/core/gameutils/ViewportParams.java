package rroyo.jge.core.gameutils;

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

    public static ViewportParams calculateViewport(Dimension windowDimension) {
        double centerX = windowDimension.getHalfWidth();
        double centerY = windowDimension.getHalfHeight();
        double zoom = Camera.getZoom();
        double camX = Camera.getCameraPosition().getX();
        double camY = Camera.getCameraPosition().getY();

        double viewWorldX = centerX - (centerX / zoom) - camX;
        double viewWorldY = centerY - (centerY / zoom) - camY;
        double viewWorldWidth = windowDimension.getWidth() / zoom;
        double viewWorldHeight = windowDimension.getHeight() / zoom;

        return new ViewportParams(centerX, centerY, zoom, camX, camY, viewWorldX, viewWorldY, viewWorldWidth, viewWorldHeight);
    }

}
