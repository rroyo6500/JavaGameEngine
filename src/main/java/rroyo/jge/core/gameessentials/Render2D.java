package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameobjects.positionable.GameElement;
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

    /**
     * The background color of the rendering panel. Defaults to black.
     */
    private Color backgroundColor = Color.BLACK;

    private boolean debugMode = false;

    /**
     * A list of {@link Text} elements that need to be drawn in the current frame.
     */
    private List<Text> textsToDraw = new ArrayList<>();

    private Scene scene;

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

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Prepares the lists of elements and text to be drawn in the next rendering pass.
     * This method is typically called by a {@link Scene} before asking the renderer to repaint.
     *
     * @param texts        The list of {@link Text} elements to render.
     */
    public void prepareFrame(Scene scene, List<Text> texts) {
        this.scene = scene;
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

        ViewportParams vp = ViewportParams.calculateViewport(windowDimension);

        if (debugMode) {
            DebugRender.render(g2, scene, vp, windowDimension);
            return;
        }

        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, (int) windowDimension.getWidth(), (int) windowDimension.getHeight());

        for (PositionableGameObject pgo : PhysicsEngine.getGameObjects(scene)) {
            if (pgo.isDeleted() || !pgo.isVisible()) continue;

            double objX = pgo.getPosition().getX() - pgo.getSize().getHalfWidth();
            double objY = pgo.getPosition().getY() - pgo.getSize().getHalfHeight();
            double objW = pgo.getSize().getWidth();
            double objH = pgo.getSize().getHeight();

            if (objX + objW < vp.viewWorldX() || objX > vp.viewWorldX() + vp.viewWorldWidth() ||
                    objY + objH < vp.viewWorldY() || objY > vp.viewWorldY() + vp.viewWorldHeight()
            ) continue;

            if (pgo instanceof GameElement gameElement) {
                gameElement.getSprite().next();
                if (gameElement.getSprite().hasSpriteImage()) {
                    double worldX, worldY, width, height;
                    int drawX, drawY, drawW, drawH;

                    worldX = switch (gameElement.getSprite().getHorizontalDirection()) {
                        case RIGHT -> gameElement.getPosition().getX() - gameElement.getSize().getHalfWidth();
                        case LEFT -> (gameElement.getPosition().getX() - gameElement.getSize().getHalfWidth()) + gameElement.getSize().getWidth();
                    };
                    worldY = gameElement.getPosition().getY() - gameElement.getSize().getHalfHeight();

                    width = switch (gameElement.getSprite().getHorizontalDirection()) {
                        case RIGHT -> gameElement.getSize().getWidth();
                        case LEFT -> -gameElement.getSize().getWidth();
                    };
                    height = gameElement.getSize().getHeight();

                    drawX = (int) (((worldX + vp.camX()) - vp.centerX()) * vp.zoom() + vp.centerX());
                    drawY = (int) (((worldY + vp.camY()) - vp.centerY()) * vp.zoom() + vp.centerY());
                    drawW = (int) (width * vp.zoom());
                    drawH = (int) (height * vp.zoom());

                    g.drawImage(gameElement.getSprite().getSpriteImages(), drawX, drawY, drawW, drawH, null);
                }
                else {
                    DrawParams dp = DrawParams.calculateDraw(gameElement, vp);

                    g.setColor(gameElement.getSprite().getSpriteColor());
                    g.fillRect(dp.drawX(), dp.drawY(), dp.drawW(), dp.drawH());
                }
            }
        }
        for (Text text : textsToDraw) {
            if (text.getText() != null) g.setColor(text.getForeground());
            g.drawString(text.getText(), (int) text.getPosition().getX()+5, (int) text.getPosition().getY()+15);
        }
    }
}
