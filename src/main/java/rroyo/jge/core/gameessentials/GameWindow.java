package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.Dimension;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Keyboard;
import rroyo.jge.core.gameutils.Timer;

import javax.swing.*;

/**
 * The {@code GameWindow} class manages the main application window for the game.
 * It uses a {@link JFrame} to display the game's contents and initializes the rendering system.
 * It also handles switching between different scenes and starting the game loop.
 */
public class GameWindow {

    /**
     * A timer used for scheduling actions, typically used when changing scenes.
     */
    private Timer timer;

    /**
     * The target frames per second (FPS) for the game loop running in this window.
     */
    private final int targetFPS;

    /**
     * The renderer component responsible for drawing the 2D elements onto the window.
     */
    private final Render2D renderer;

    /**
     * The game loop responsible for updating and rendering the active scene.
     */
    private GameLoop gameLoop;

    /**
     * Constructs a new {@code GameWindow} with the specified target FPS, dimensions, and initial scene.
     * This initializes the underlying JFrame, sets up keyboard listening, creates the renderer,
     * and sets the initial scene to be displayed.
     *
     * @param targetFPS The desired frames per second for the game.
     * @param dimension The width and height of the game window.
     * @param scene     The initial {@link Scene} to be displayed and executed.
     */
    public GameWindow(int targetFPS, Dimension dimension, Scene scene) {
        this.targetFPS = targetFPS;

        Camera.setWindowDimensions(dimension);

        JFrame frame = new JFrame();
        frame.addKeyListener(Keyboard.listener);

        renderer = new Render2D(dimension);
        frame.add(renderer);

        setScene(scene);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Sets a new active scene for the game window.
     * If a transition timer is currently running, the scene change is ignored.
     * It links the new scene with the renderer and starts the game loop if it hasn't been started yet.
     * If the loop is already running, it updates the loop to process frames for the new scene.
     *
     * @param scene The new {@link Scene} to be set as active.
     */
    public final void setScene(Scene scene) {
        if (timer != null && !timer.isFinished()) return;
        scene.setRenderer(renderer);

        if (gameLoop == null) {
            gameLoop = new GameLoop(targetFPS, scene::processFrame, null);
            gameLoop.start();
        } else {
            gameLoop.setOnUpdate(scene::processFrame);
        }

        timer = Timer.schedule(1.0f, null);
    }

}
