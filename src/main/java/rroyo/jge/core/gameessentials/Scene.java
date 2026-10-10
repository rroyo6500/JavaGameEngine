package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.GameElement;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameobjects.Text;

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
     * A list containing all the active {@link GameElement}s in this scene.
     */
    private final List<GameElement> gameElements = new ArrayList<>();

    /**
     * A list containing all the active {@link Text} elements in this scene.
     */
    private final List<Text> texts = new ArrayList<>();

    /**
     * The renderer responsible for drawing the visual representation of this scene.
     */
    private Render2D renderer;

    /**
     * Default constructor for the {@code Scene}.
     */
    public Scene() {}

    /**
     * Sets the renderer that this scene will use to draw its elements.
     *
     * @param renderer The {@link Render2D} instance to be used for drawing.
     */
    public void  setRenderer(Render2D renderer) {
        this.renderer = renderer;
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
        for (GameElement ge : gameElements) {
            if (!ge.isDeleted()) {
                ge.move();
                ge.update();
            }
        }

        update();

        gameElements.removeIf(GameObject::isDeleted);
        texts.removeIf(Text::isDeleted);

        if (renderer != null) {
            renderer.prepareFrame(gameElements, texts);
            renderer.repaint();
        }
    }

    /**
     * Adds one or more game objects to the scene.
     * If an object is a single {@link GameElement}, it is added to the scene's list and its {@code start()} method is called.
     * If an object is a {@link Group}, all its members are individually added and started.
     * It ensures that duplicates are not added.
     *
     * @param gameObjects A variable number of {@link GameObject}s (which can be elements or groups) to add to the scene.
     */
    public final void add(GameObject... gameObjects) {
        for (GameObject go : gameObjects) {
            if (go instanceof  GameElement ge) {
                if (!gameElements.contains(ge)) {
                    gameElements.add(ge);
                    ge.start();
                }
            } else if (go instanceof Group group) {
                for (GameElement ge : group.getMembers()) {
                    if (!gameElements.contains(ge)) {
                        gameElements.add(ge);
                        ge.start();
                    }
                }
            }
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
     * Sets the background color of the scene by delegating to the renderer.
     *
     * @param color The new background {@link Color} for the scene.
     */
    public final void setBackgroundColor(Color color) {
        renderer.setBackgroundColor(color);
    }

    /**
     * Clears all game elements and text objects from the scene.
     * Useful for resetting the scene or preparing for a complete change of state.
     */
    public void clear() {
        gameElements.clear();
        texts.clear();
    }

}
