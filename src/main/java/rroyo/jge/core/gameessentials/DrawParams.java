package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameutils.Dimension;

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
        return calculateDraw(pgo, pgo.getSize(), vp);
    }

    /**
     * Converts a centered world-space rectangle with an explicit size into screen pixels.
     *
     * @param pgo entity whose center is used for placement
     * @param size world-space dimensions to draw
     * @param vp viewport transform to apply
     * @return the top-left screen coordinate and scaled dimensions
     */
    public static DrawParams calculateDraw(PositionableGameObject pgo, Dimension size, ViewportParams vp) {
        int drawW = (int) Math.round(size.getWidth() * vp.zoom());
        int drawH = (int) Math.round(size.getHeight() * vp.zoom());
        int drawX = (int) Math.round(vp.worldToScreenX(pgo.getPosition().getX()) - drawW / 2.0);
        int drawY = (int) Math.round(vp.worldToScreenY(pgo.getPosition().getY()) - drawH / 2.0);

        return new DrawParams(drawX, drawY, drawW, drawH);
    }

}
