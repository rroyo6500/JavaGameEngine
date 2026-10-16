package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameutils.*;
import rroyo.jge.core.gameutils.Dimension;

import java.awt.*;
import java.util.List;

/**
 * Draws diagnostic geometry for visible entities in a scene.
 * <p>
 * Debug mode uses the same viewport transform and visibility culling as normal rendering,
 * outlines each entity with a stable per-instance color, and draws lines to nearby entities.
 * This helper does not mutate the entity collection or the supplied graphics context state
 * beyond the drawing operations it performs.
 * </p>
 */
public final class DebugRender {

    /** Creates a helper instance; rendering is exposed through the static {@link #render} method. */
    public DebugRender() {}

    /**
     * Paints the debug view into the supplied graphics context.
     * Deleted, hidden, and off-screen objects are skipped. The caller owns the graphics
     * context and is responsible for disposing or restoring it as appropriate.
     *
     * @param g graphics context receiving the debug drawing
     * @param gameObjectsToDraw snapshot of entities eligible for rendering
     * @param vp camera transform and visible world bounds for this frame
     * @param windowDimension dimensions of the screen-space viewport
     */
    public static void render(Graphics2D g, List<PositionableGameObject> gameObjectsToDraw, ViewportParams vp, Dimension windowDimension) {
        g.setColor(Color.black);
        g.fillRect(0, 0, (int) windowDimension.getWidth(), (int) windowDimension.getHeight());

        for (PositionableGameObject pgo : gameObjectsToDraw) {
            if (pgo.isDeleted() || !pgo.isVisible()) continue;

            double objX = pgo.getPosition().getX() - pgo.getSize().getHalfWidth();
            double objY = pgo.getPosition().getY() - pgo.getSize().getHalfHeight();
            double objW = pgo.getSize().getWidth();
            double objH = pgo.getSize().getHeight();

            if (objX + objW < vp.viewWorldX() || objX > vp.viewWorldX() + vp.viewWorldWidth() ||
                    objY + objH < vp.viewWorldY() || objY > vp.viewWorldY() + vp.viewWorldHeight()
            ) continue;

            DrawParams dp = DrawParams.calculateDraw(pgo, vp);

            g.setColor(new Color(Math.abs(pgo.hashCode()) % 0xFFFFFF));
            g.drawRect(dp.drawX(), dp.drawY(), dp.drawW(), dp.drawH());
            g.drawLine(dp.drawX(), dp.drawY(), dp.drawX()+dp.drawW(), dp.drawY()+dp.drawH());
            g.drawLine(dp.drawX(), dp.drawY()+dp.drawH(), dp.drawX()+dp.drawW(), dp.drawY());

            for (PositionableGameObject tmp : gameObjectsToDraw) {
                if (Overlap.ir(pgo, tmp)) {
                    DrawParams tmpdp = DrawParams.calculateDraw(tmp, vp);

                    g.drawLine(
                            (dp.drawX() + (dp.drawW()/2)), (dp.drawY() + (dp.drawH()/2)),
                            (tmpdp.drawX() + (tmpdp.drawW()/2)), tmpdp.drawY() + (tmpdp.drawH()/2)
                    );
                }
            }

        }
    }

}
