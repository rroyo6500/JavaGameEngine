package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Overlap;

import java.awt.*;

public final class DebugRender {

    public static void render(Graphics2D g, Scene scene, Dimension windowDimension) {
        g.setBackground(Color.black);

        double centerX = windowDimension.getHalfWidth();
        double centerY = windowDimension.getHalfHeight();
        double zoom = Camera.getZoom();
        double camX = Camera.getCameraPosition().getX();
        double camY = Camera.getCameraPosition().getY();

        double viewWorldX = centerX - (centerX / zoom) - camX;
        double viewWorldY = centerY - (centerY / zoom) - camY;
        double viewWorldWidth = windowDimension.getWidth() / zoom;
        double viewWorldHeight = windowDimension.getHeight() / zoom;

        for (PositionableGameObject pgo : PhisicsEngine.getGameObjects(scene)) {
            if (pgo.isDeleted() || !pgo.isVisible()) continue;

            double objX = pgo.getPosition().getX() - pgo.getSize().getHalfWidth();
            double objY = pgo.getPosition().getY() - pgo.getSize().getHalfHeight();
            double objW = pgo.getSize().getWidth();
            double objH = pgo.getSize().getHeight();

            if (objX + objW < viewWorldX || objX > viewWorldX + viewWorldWidth ||
                    objY + objH < viewWorldY || objY > viewWorldY + viewWorldHeight
            ) continue;

            double worldX, worldY, width, height;
            int drawX, drawY, drawW, drawH;

            worldX = pgo.getPosition().getX() - pgo.getSize().getHalfWidth();
            worldY = pgo.getPosition().getY() - pgo.getSize().getHalfHeight();
            width = pgo.getSize().getWidth();
            height = pgo.getSize().getHeight();

            drawX = (int) (((worldX + camX) - centerX) * zoom + centerX);
            drawY = (int) (((worldY + camY) - centerY) * zoom + centerY);
            drawW = (int) (width * zoom);
            drawH = (int) (height * zoom);

            g.setColor(new Color(pgo.hashCode() % 0xFFFFFF));
            g.drawRect(drawX, drawY, drawW, drawH);
            g.drawLine(drawX, drawY, drawX+drawW, drawY+drawH);
            g.drawLine(drawX, drawY+drawH, drawX+drawW, drawY);

            for (PositionableGameObject tmp : PhisicsEngine.getGameObjects(scene)) {
                if (Overlap.ir(pgo, tmp)) {
                    double tmpX, tmpY, tmpW, tmpH;
                    int tmpDX, tmpDY, tmpDW, tmpDH;

                    tmpX = tmp.getPosition().getX() - tmp.getSize().getHalfWidth();
                    tmpY = tmp.getPosition().getY() - tmp.getSize().getHalfHeight();
                    tmpW = tmp.getSize().getWidth();
                    tmpH = tmp.getSize().getHeight();

                    tmpDX = (int) (((tmpX + camX) - centerX) * zoom + centerX);
                    tmpDY = (int) (((tmpY + camY) - centerY) * zoom + centerY);
                    tmpDW = (int) (tmpW * zoom);
                    tmpDH = (int) (tmpH * zoom);

                    g.drawLine(
                            (drawX + (drawW/2)), (drawY + (drawH/2)),
                            (tmpDX + (tmpDW/2)), tmpDY + (tmpDH/2)
                    );
                }
            }

        }

    }

}
