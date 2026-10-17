package rroyo.jge.core.gameessentials;

import rroyo.jge.core.assets.Sprite;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.Text;
import rroyo.jge.enums.SpriteHorizontalDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing panel that renders a snapshot of a 2D scene using a camera transform.
 * It draws sprite images or solid-color rectangles, overlays screen-space text, culls entities
 * outside the visible world bounds, and can switch to diagnostic geometry through debug mode.
 * Frame data is published as immutable-by-convention list snapshots before Swing repaints the panel.
 */
public final class Render2D extends JPanel {

    /**
     * The dimensions of the game window, used for calculating view boundaries and scaling.
     */
    private final Dimension windowDimension;

    /** Camera transform used to map world coordinates into the panel's screen coordinates. */
    private final Camera camera;

    /**
     * The background color of the rendering panel. Defaults to black.
     */
    private Color backgroundColor = Color.BLACK;

    /** Selects diagnostic geometry rendering instead of normal sprites and text. */
    private boolean debugMode = false;

    /**
     * A list of {@link PositionableGameObject} elements that need to be drawn in the current frame.
     */
    private volatile List<PositionableGameObject> gameObjectsToDraw = new ArrayList<>();

    /**
     * A list of {@link Text} elements that need to be drawn in the current frame.
     */
    private volatile List<Text> textsToDraw = new ArrayList<>();

    /**
     * Constructs a new {@code Render2D} component with the specified window dimensions.
     * It configures the preferred size of the panel and enables double buffering for smoother rendering.
     *
     * @param camera camera that supplies position and zoom for drawing
     * @param windowDimension logical dimensions of the render view in pixels
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

    /**
     * Enables or disables diagnostic rendering for subsequent paint operations.
     *
     * @param debugMode {@code true} to draw debug bounds and proximity links; {@code false} for normal rendering
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Prepares the lists of elements and text to be drawn in the next rendering pass.
     * This method is typically called by a {@link Scene} before asking the renderer to repaint.
     *
     * @param gameObjects positionable entity snapshot for the next paint operation
     * @param texts text snapshot for the next paint operation
     */
    public void prepareFrame(List<PositionableGameObject> gameObjects, List<Text> texts) {
        this.gameObjectsToDraw = new ArrayList<>(gameObjects);
        this.textsToDraw = new ArrayList<>(texts);
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

        List<PositionableGameObject> objects = this.gameObjectsToDraw;
        List<Text> texts = this.textsToDraw;

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            ViewportParams vp = ViewportParams.calculateViewport(camera, windowDimension);

            if (debugMode) {
                DebugRender.render(g2, objects, vp, windowDimension);
                return;
            }

            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, (int) windowDimension.getWidth(), (int) windowDimension.getHeight());

            for (PositionableGameObject pgo : objects) {
                if (pgo.isDeleted() || !pgo.isVisible()) continue;

                Dimension drawSize = pgo instanceof SpriteGameObject spriteGameObject
                        ? getSpriteDrawSize(spriteGameObject)
                        : pgo.getSize();
                Rectangle bounds = getDrawBounds(pgo, drawSize);
                if (bounds.getMaxX() < vp.viewWorldX() || bounds.getMinX() > vp.viewWorldX() + vp.viewWorldWidth() ||
                        bounds.getMaxY() < vp.viewWorldY() || bounds.getMinY() > vp.viewWorldY() + vp.viewWorldHeight()
                ) continue;

                Graphics2D objectGraphics = (Graphics2D) g2.create();
                try {
                    objectGraphics.rotate(pgo.getRotation(),
                            vp.worldToScreenX(pgo.getPosition().getX()),
                            vp.worldToScreenY(pgo.getPosition().getY()));

                    if (pgo instanceof SpriteGameObject spriteGameObject) {
                        if (spriteGameObject.getSprite().hasSpriteImage()) {
                            Sprite sprite = spriteGameObject.getSprite();
                            BufferedImage img = sprite.getSpriteImages();
                            DrawParams dp = DrawParams.calculateDraw(spriteGameObject, drawSize, vp);

                            if (sprite.getHorizontalDirection() == SpriteHorizontalDirection.LEFT)
                                objectGraphics.drawImage(img,
                                        dp.drawX(), dp.drawY(), dp.drawX() + dp.drawW(), dp.drawY() + dp.drawH(),
                                        img.getWidth(), 0, 0, img.getHeight(), null);
                            else
                                objectGraphics.drawImage(img, dp.drawX(), dp.drawY(), dp.drawW(), dp.drawH(), null);
                        }
                        else {
                            DrawParams dp = DrawParams.calculateDraw(spriteGameObject, drawSize, vp);

                            objectGraphics.setColor(spriteGameObject.getSprite().getSpriteColor());
                            objectGraphics.fillRect(dp.drawX(), dp.drawY(), dp.drawW(), dp.drawH());
                        }
                    }
                } finally {
                    objectGraphics.dispose();
                }
            }
            for (Text text : texts) {
                if (text.getText() != null) {
                    g2.setColor(text.getForeground());
                    g2.drawString(text.getText(), (int) text.getPosition().getX()+5, (int) text.getPosition().getY()+15);
                }
            }
        } finally {
            g2.dispose();
        }
    }

    /** Uses explicitly configured sprite dimensions, falling back to the object's visual dimensions. */
    private static Dimension getSpriteDrawSize(SpriteGameObject spriteGameObject) {
        Dimension spriteSize = spriteGameObject.getSprite().getSpriteSize();
        return spriteSize == null ? spriteGameObject.getSpriteSize() : spriteSize;
    }

    /** Calculates the rotated world-space bounds of the dimensions that will actually be drawn. */
    private static Rectangle getDrawBounds(PositionableGameObject gameObject, Dimension drawSize) {
        double cos = Math.abs(Math.cos(gameObject.getRotation()));
        double sin = Math.abs(Math.sin(gameObject.getRotation()));
        double halfWidth = cos * drawSize.getHalfWidth() + sin * drawSize.getHalfHeight();
        double halfHeight = sin * drawSize.getHalfWidth() + cos * drawSize.getHalfHeight();
        int minX = (int) Math.floor(gameObject.getPosition().getX() - halfWidth);
        int minY = (int) Math.floor(gameObject.getPosition().getY() - halfHeight);
        int maxX = (int) Math.ceil(gameObject.getPosition().getX() + halfWidth);
        int maxY = (int) Math.ceil(gameObject.getPosition().getY() + halfHeight);
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}
