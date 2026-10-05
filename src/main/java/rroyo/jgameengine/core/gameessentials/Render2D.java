package rroyo.jgameengine.core.gameessentials;

import rroyo.jgameengine.core.gameobjects.Dimension;
import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.Text;
import rroyo.jgameengine.core.gameutils.Camera;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Render2D extends JPanel {

    private final Dimension windowDimension;
    private Color backgroundColor = Color.BLACK;

    private List<GameElement> gameElementsToDraw = new ArrayList<>();
    private List<Text> textsToDraw = new ArrayList<>();

    public Render2D(Dimension windowDimension) {
        this.windowDimension = windowDimension;

        this.setPreferredSize(new java.awt.Dimension(
                (int) windowDimension.getWidth(),
                (int) windowDimension.getHeight()
        ));

        this.setDoubleBuffered(true);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void prepareFrame(List<GameElement> gameElements, List<Text> texts) {
        this.gameElementsToDraw = gameElements;
        this.textsToDraw = texts;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, getWidth(), getHeight());

        double centerX = windowDimension.getHalfWidth();
        double centerY = windowDimension.getHalfHeight();
        double zoom = Camera.getZoom();
        double camX = Camera.getCameraPosition().getX();
        double camY = Camera.getCameraPosition().getY();

        double viewWorldX = centerX - (centerX / zoom) - camX;
        double viewWorldY = centerY - (centerY / zoom) - camY;
        double viewWorldWidth = windowDimension.getWidth() / zoom;
        double viewWorldHeight = windowDimension.getHeight() / zoom;

        for (GameElement go : gameElementsToDraw) {
            if (go.isDeleted()) continue;

            double objX = go.getPoint().getX() - go.getDimension().getHalfWidth();
            double objY = go.getPoint().getY() - go.getDimension().getHalfHeight();
            double objW = go.getDimension().getWidth();
            double objH = go.getDimension().getHeight();

            if (objX + objW < viewWorldX || objX > viewWorldX + viewWorldWidth ||
                    objY + objH < viewWorldY || objY > viewWorldY + viewWorldHeight
            ) continue;

            double worldX, worldY, width, height;

            go.getSprite().next();
            if (go.getSprite().hasSpriteImage()) {
                worldX = switch (go.getSprite().getHorizontalDirection()) {
                    case RIGHT -> go.getPoint().getX() - go.getDimension().getHalfWidth();
                    case LEFT -> (go.getPoint().getX() - go.getDimension().getHalfWidth()) + go.getDimension().getWidth();
                };
                worldY = go.getPoint().getY() - go.getDimension().getHalfHeight();

                width = switch (go.getSprite().getHorizontalDirection()) {
                    case RIGHT -> go.getDimension().getWidth();
                    case LEFT -> -go.getDimension().getWidth();
                };
                height = go.getDimension().getHeight();

                int drawX = (int) (((worldX + camX) - centerX) * zoom + centerX);
                int drawY = (int) (((worldY + camY) - centerY) * zoom + centerY);
                int drawW = (int) (width * zoom);
                int drawH = (int) (height * zoom);

                g.drawImage(go.getSprite().getSpriteImages(), drawX, drawY, drawW, drawH, null);
            }
            else {
                worldX = go.getPoint().getX() - go.getDimension().getHalfWidth();
                worldY = go.getPoint().getY() - go.getDimension().getHalfHeight();
                width = go.getDimension().getWidth();
                height = go.getDimension().getHeight();

                int drawX = (int) (((worldX + camX) - centerX) * zoom + centerX);
                int drawY = (int) (((worldY + camY) - centerY) * zoom + centerY);
                int drawW = (int) (width * zoom);
                int drawH = (int) (height * zoom);

                g.setColor(go.getSprite().getSpriteColor());
                g.fillRect(drawX, drawY, drawW, drawH);
            }
        }
        for (Text text : textsToDraw) {
            if (text.getText() != null) g.setColor(text.getForeground());
            g.drawString(text.getText(), (int) text.getPoint().getX()+5, (int) text.getPoint().getY()+15);
        }
    }
}
