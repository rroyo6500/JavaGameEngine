package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;

/**
 * Immutable screen-space rectangle produced for a world-space entity.
 * <p>
 * The coordinates and dimensions are integer pixels after applying camera translation
 * and zoom. Instances are calculated per draw operation and do not retain the entity.
 * </p>
 *
 * @param drawX left edge of the rectangle in screen pixels
 * @param drawY top edge of the rectangle in screen pixels
 * @param drawW rendered width in screen pixels
 * @param drawH rendered height in screen pixels
 */
public record DrawParams (
        int drawX,
        int drawY,
        int drawW,
        int drawH
) {

    /**
     * Converts an entity's centered world-space bounds into screen-space pixels.
     * The camera viewport supplies the screen center, camera offset, and zoom factor.
     *
     * @param pgo entity whose current position and size are converted
     * @param vp viewport transform to apply
     * @return the top-left screen coordinate and scaled dimensions
     */
    public static DrawParams calculateDraw(PositionableGameObject pgo, ViewportParams vp) {
        double worldX = pgo.getPosition().getX() - pgo.getSize().getHalfWidth();
        double worldY = pgo.getPosition().getY() - pgo.getSize().getHalfHeight();
        double width = pgo.getSize().getWidth();
        double height = pgo.getSize().getHeight();

        int drawX = (int) (((worldX + vp.camX()) - vp.centerX()) * vp.zoom() + vp.centerX());
        int drawY = (int) (((worldY + vp.camY()) - vp.centerY()) * vp.zoom() + vp.centerY());
        int drawW = (int) (width * vp.zoom());
        int drawH = (int) (height * vp.zoom());

        return new DrawParams(drawX, drawY, drawW, drawH);
    }

}
