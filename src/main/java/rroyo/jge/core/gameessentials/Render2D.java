package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.Dimension;
import rroyo.jge.core.gameobjects.GameElement;
import rroyo.jge.core.gameobjects.Text;
import rroyo.jge.core.gameutils.Camera;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Render2D} class extends {@link JPanel} and handles the rendering of 2D game elements.
 * It is responsible for drawing game objects (sprites or colored rectangles) and text onto the screen,
 * taking into account camera position and zoom levels to create a view of the game world.
 */
public class Render2D extends JPanel {

    /**
     * The dimensions of the game window, used for calculating view boundaries and scaling.
     */
    private final Dimension windowDimension;

    /**
     * The background color of the rendering panel. Defaults to black.
     */
    private Color backgroundColor = Color.BLACK;

    /**
     * A list of {@link GameElement}s that need to be drawn in the current frame.
     */
    private List<GameElement> gameElementsToDraw = new ArrayList<>();

    /**
     * A list of {@link Text} elements that need to be drawn in the current frame.
     */
    private List<Text> textsToDraw = new ArrayList<>();

    /**
     * Constructs a new {@code Render2D} component with the specified window dimensions.
     * It configures the preferred size of the panel and enables double buffering for smoother rendering.
     *
     * @param windowDimension The dimension (width and height) of the render view.
     */
    public Render2D(Dimension windowDimension) {
        this.windowDimension = windowDimension;

        this.setPreferredSize(new java.awt.Dimension(
                (int) windowDimension.getWidth(),
                (int) windowDimension.getHeight()
        ));

        this.setDoubleBuffered(true);
    }

    /**
     * Sets the background color of the render view.
     *
     * @param backgroundColor The new background {@link Color}.
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Prepares the lists of elements and text to be drawn in the next rendering pass.
     * This method is typically called by a {@link Scene} before asking the renderer to repaint.
     *
     * @param gameElements The list of {@link GameElement}s to render.
     * @param texts        The list of {@link Text} elements to render.
     */
    public void prepareFrame(List<GameElement> gameElements, List<Text> texts) {
        this.gameElementsToDraw = gameElements;
        this.textsToDraw = texts;
    }

    /**
     * Overrides the {@code paintComponent} method of {@link JPanel} to perform custom rendering.
     * It clears the screen with the background color, calculates the visible area based on the camera,
     * culls elements outside the viewport for optimization, and draws the visible elements and texts.
     * Support is provided for drawing either image-based sprites or colored rectangles, depending on the element's sprite configuration.
     *
     * @param g The {@link Graphics} context used for drawing.
     */
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
