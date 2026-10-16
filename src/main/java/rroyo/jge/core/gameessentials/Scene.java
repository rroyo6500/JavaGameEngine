package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.Text;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.ScriptableGameObject;
import rroyo.jge.core.gameutils.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Scene} class is an abstract base class that represents a distinct level, menu, or state within the game.
 * It manages a collection of game elements and text objects, handling their life cycles, updates, and interactions.
 * Subclasses must implement the {@link #update()} method to define scene-specific logic.
 */
public abstract class Scene {

    /**
     * A list containing all the active {@link Text} elements in this scene.
     */
    private final List<Text> texts = new ArrayList<>();

    /** Background color used by the renderer when this scene is active. */
    private Color backgroundColor = Color.BLACK;

    /** Whether this scene's renderer should display diagnostic geometry. */
    private boolean debugMode = false;

    /**
     * The renderer responsible for drawing the visual representation of this scene.
     */
    private Render2D renderer;

    /**
     * The window that contains this scene
     */
    protected GameWindow window;

    /** Camera attached by the containing window; subclasses may use it to control the view. */
    protected Camera camera;

    /**
     * Default constructor for the {@code Scene}.
     */
    public Scene() {}

    /**
     * Sets the renderer that this scene will use to draw its elements.
     *
     * @param renderer  The {@link Render2D} instance to be used for drawing.
     */
    public void  setRenderer(Render2D renderer) {
        this.renderer = renderer;
        renderer.setBackgroundColor(backgroundColor);
        renderer.setDebugMode(debugMode);
    }

    /**
     * Sets the window that contains this scene
     *
     * @param window    The {@link GameWindow} instance
     */
    public void setWindow(GameWindow window) {
        this.window = window;
    }

    /**
     * Stores the camera supplied by the containing game window for use by scene logic.
     *
     * @param camera camera associated with this scene
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Abstract method that must be implemented by subclasses to provide scene-specific update logic.
     * This method is called once per frame during the processing phase.
     */
    public abstract void update();

    /**
     * Processes a single frame for the scene.
     * This involves moving and updating all active game elements, calling the abstract {@link #update()} method
     * for scene-specific logic, removing any elements marked for deletion, and finally passing the active
     * elements to the renderer to be drawn on screen.
     */
    public final void processFrame() {
        for (PositionableGameObject gameObject : PhysicsEngine.getGameObjects(this)) {
            if (!gameObject.isDeleted()) {
                if (gameObject instanceof ScriptableGameObject sgo) {
                    sgo.start();
                    sgo.move();
                    sgo.update();
                } else gameObject.move();

                if (gameObject instanceof SpriteGameObject spriteGameObject)
                    spriteGameObject.getSprite().next();
            }
        }

        update();

        PhysicsEngine.removeIf(this, GameObject::isDeleted);
        texts.removeIf(Text::isDeleted);

        if (renderer != null) {
            renderer.prepareFrame(List.copyOf(PhysicsEngine.getGameObjects(this)),List.copyOf(texts));
            renderer.repaint();
        }
    }

    /**
     * Adds one or more text objects to the scene to be rendered.
     * It ensures that duplicate text objects are not added.
     *
     * @param newTexts A variable number of {@link Text} objects to add.
     */
    public final void addText(Text... newTexts) {
        for (Text text : newTexts) {
            if (!texts.contains(text)) texts.add(text);
        }
    }

    /**
     * Returns the color currently configured for this scene's background.
     *
     * @return scene background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color of the scene by delegating to the renderer.
     *
     * @param color The new background {@link Color} for the scene.
     */
    public final void setBackgroundColor(Color color) {
        backgroundColor = color;
        if (renderer != null) renderer.setBackgroundColor(backgroundColor);
    }

    /**
     * Reports whether debug rendering is enabled for this scene.
     *
     * @return {@code true} when diagnostic rendering is enabled
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Changes debug rendering and immediately forwards the setting when a renderer is attached.
     *
     * @param debugMode {@code true} to show diagnostic geometry; {@code false} to show normal scene visuals
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        if (renderer != null) renderer.setDebugMode(debugMode);
    }

    /**
     * Clears all game elements and text objects from the scene.
     * Useful for resetting the scene or preparing for a complete change of state.
     */
    public void clear() {
        PhysicsEngine.remove(this);
        texts.clear();
    }

}
