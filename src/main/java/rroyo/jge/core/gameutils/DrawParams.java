package rroyo.jge.core.gameutils;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;

public record DrawParams (
        int drawX,
        int drawY,
        int drawW,
        int drawH
) {

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
