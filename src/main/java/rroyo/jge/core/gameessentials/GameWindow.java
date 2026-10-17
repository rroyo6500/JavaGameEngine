package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Keyboard;
import rroyo.jge.core.gameutils.Timer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The {@code GameWindow} class manages the main application window for the game.
 * It uses a {@link JFrame} to display the game's contents and initializes the rendering system.
 * It also handles switching between different scenes and starting the game loop.
 */
public final class GameWindow {

    /**
     * The target frames per second (FPS) for the game loop running in this window.
     */
    private final int targetFPS;

    /**
     * The renderer component responsible for drawing the 2D elements onto the window.
     */
    private final Render2D renderer;

    /** Camera shared by the window's renderer and each scene attached to this window. */
    private final Camera camera;

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
        camera = new Camera();
        renderer = new Render2D(camera, dimension);

        SwingUtilities.invokeLater(() -> {
            camera.setWindowDimensions(dimension);

            JFrame frame = new JFrame();

            frame.addKeyListener(Keyboard.listener);
            frame.addWindowFocusListener(new WindowAdapter() {
                /** Clears held-key state so focus changes cannot leave keys stuck. */
                @Override
                public void windowLostFocus(WindowEvent e) {
                    Keyboard.clear();
                }
            });

            frame.add(renderer);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            setScene(scene);
        });
    }

    /**
     * Attaches a scene to this window's renderer, window reference, and camera, then stores its
     * frame processor as the loop's update callback. The first call creates and starts the loop.
     *
     * @param scene scene to attach and activate
     */
    public synchronized void setScene(Scene scene) {
        scene.setRenderer(renderer);
        scene.setWindow(this);
        scene.setCamera(camera);

        if (gameLoop == null) {
            gameLoop = new GameLoop(targetFPS, scene::processFrame, null);
            gameLoop.start();
        } else {
            gameLoop.setOnUpdate(scene::processFrame);
        }
    }

}
