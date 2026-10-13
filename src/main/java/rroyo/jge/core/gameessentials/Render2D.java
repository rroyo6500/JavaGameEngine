package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.Text;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Render2D} class extends {@link JPanel} and handles the rendering of 2D game elements.
 * It is responsible for drawing game objects (sprites or colored rectangles) and text onto the screen,
 * taking into account camera position and zoom levels to create a view of the game world.
 */
public final class Render2D extends JPanel {

    /**
     * The dimensions of the game window, used for calculating view boundaries and scaling.
     */
    private final Dimension windowDimension;

    private final Camera camera;

    /**
     * The background color of the rendering panel. Defaults to black.
     */
    private Color backgroundColor = Color.BLACK;

    private boolean debugMode = false;

    /**
     * A list of {@link PositionableGameObject} elements that need to be drawn in the current frame.
     */
    private List<PositionableGameObject> gameObjectsToDraw = new ArrayList<>();

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
    public Render2D(Camera camera, Dimension windowDimension) {
        this.camera = camera;
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

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Prepares the lists of elements and text to be drawn in the next rendering pass.
     * This method is typically called by a {@link Scene} before asking the renderer to repaint.
     *
     * @param texts        The list of {@link Text} elements to render.
     */
    public void prepareFrame(List<PositionableGameObject> gameObjects, List<Text> texts) {
        this.gameObjectsToDraw = gameObjects;
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

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            ViewportParams vp = ViewportParams.calculateViewport(camera, windowDimension);

            if (debugMode) {
                DebugRender.render(g2, gameObjectsToDraw, vp, windowDimension);
                return;
            }

            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, (int) windowDimension.getWidth(), (int) windowDimension.getHeight());

            for (PositionableGameObject pgo : gameObjectsToDraw) {
                if (pgo.isDeleted() || !pgo.isVisible()) continue;

                double objX = pgo.getPosition().getX() - pgo.getSize().getHalfWidth();
                double objY = pgo.getPosition().getY() - pgo.getSize().getHalfHeight();
                double objW = pgo.getSize().getWidth();
                double objH = pgo.getSize().getHeight();

                if (objX + objW < vp.viewWorldX() || objX > vp.viewWorldX() + vp.viewWorldWidth() ||
                        objY + objH < vp.viewWorldY() || objY > vp.viewWorldY() + vp.viewWorldHeight()
                ) continue;

                if (pgo instanceof SpriteGameObject spriteGameObject) {
                    if (spriteGameObject.getSprite().hasSpriteImage()) {
                        double worldX, worldY, width, height;
                        int drawX, drawY, drawW, drawH;

                        worldX = switch (spriteGameObject.getSprite().getHorizontalDirection()) {
                            case RIGHT -> spriteGameObject.getPosition().getX() - spriteGameObject.getSize().getHalfWidth();
                            case LEFT -> (spriteGameObject.getPosition().getX() - spriteGameObject.getSize().getHalfWidth()) + spriteGameObject.getSize().getWidth();
                        };
                        worldY = spriteGameObject.getPosition().getY() - spriteGameObject.getSize().getHalfHeight();

                        width = switch (spriteGameObject.getSprite().getHorizontalDirection()) {
                            case RIGHT -> spriteGameObject.getSize().getWidth();
                            case LEFT -> -spriteGameObject.getSize().getWidth();
                        };
                        height = spriteGameObject.getSize().getHeight();

                        drawX = (int) (((worldX + vp.camX()) - vp.centerX()) * vp.zoom() + vp.centerX());
                        drawY = (int) (((worldY + vp.camY()) - vp.centerY()) * vp.zoom() + vp.centerY());
                        drawW = (int) (width * vp.zoom());
                        drawH = (int) (height * vp.zoom());

                        g.drawImage(spriteGameObject.getSprite().getSpriteImages(), drawX, drawY, drawW, drawH, null);
                    }
                    else {
                        DrawParams dp = DrawParams.calculateDraw(spriteGameObject, vp);

                        g.setColor(spriteGameObject.getSprite().getSpriteColor());
                        g.fillRect(dp.drawX(), dp.drawY(), dp.drawW(), dp.drawH());
                    }
                }
            }
            for (Text text : textsToDraw) {
                if (text.getText() != null) {
                    g.setColor(text.getForeground());
                    g.drawString(text.getText(), (int) text.getPosition().getX()+5, (int) text.getPosition().getY()+15);
                }
            }
        } finally {
            g2.dispose();
        }
    }
}
