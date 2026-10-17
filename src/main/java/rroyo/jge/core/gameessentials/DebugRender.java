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

        // Draw each proximity link once and in its own visible color. Object outlines use
        // child graphics contexts, so their colors must not be relied on for these links.
        g.setColor(Color.WHITE);
        for (int i = 0; i < gameObjectsToDraw.size(); i++) {
            PositionableGameObject first = gameObjectsToDraw.get(i);
            if (first.isDeleted() || !first.isVisible()) continue;
            for (int j = i + 1; j < gameObjectsToDraw.size(); j++) {
                PositionableGameObject second = gameObjectsToDraw.get(j);
                if (second.isDeleted() || !second.isVisible()) continue;
                if (!isInViewport(first, vp) && !isInViewport(second, vp)) continue;
                if (!Overlap.ir(first, second)) continue;

                g.drawLine(screenX(first, vp), screenY(first, vp),
                        screenX(second, vp), screenY(second, vp));
            }
        }

        for (PositionableGameObject pgo : gameObjectsToDraw) {
            if (pgo.isDeleted() || !pgo.isVisible()) continue;
            if (!isInViewport(pgo, vp)) continue;

            DrawParams dp = DrawParams.calculateDraw(pgo, vp);

            Graphics2D objectGraphics = (Graphics2D) g.create();
            try {
                objectGraphics.rotate(pgo.getRotation(), screenX(pgo, vp), screenY(pgo, vp));
                objectGraphics.setColor(new Color(Math.abs(pgo.hashCode()) % 0xFFFFFF));
                objectGraphics.drawRect(dp.drawX(), dp.drawY(), dp.drawW(), dp.drawH());
                objectGraphics.drawLine(dp.drawX(), dp.drawY(), dp.drawX()+dp.drawW(), dp.drawY()+dp.drawH());
                objectGraphics.drawLine(dp.drawX(), dp.drawY()+dp.drawH(), dp.drawX()+dp.drawW(), dp.drawY());
            } finally {
                objectGraphics.dispose();
            }
        }
    }

    private static boolean isInViewport(PositionableGameObject object, ViewportParams vp) {
        Rectangle bounds = object.getBounds();
        return bounds.getMaxX() >= vp.viewWorldX()
                && bounds.getMinX() <= vp.viewWorldX() + vp.viewWorldWidth()
                && bounds.getMaxY() >= vp.viewWorldY()
                && bounds.getMinY() <= vp.viewWorldY() + vp.viewWorldHeight();
    }

    private static int screenX(PositionableGameObject object, ViewportParams vp) {
        return (int) ((object.getPosition().getX() + vp.camX() - vp.centerX()) * vp.zoom() + vp.centerX());
    }

    private static int screenY(PositionableGameObject object, ViewportParams vp) {
        return (int) ((object.getPosition().getY() + vp.camY() - vp.centerY()) * vp.zoom() + vp.centerY());
    }

}
