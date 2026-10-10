package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.Dimension;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Keyboard;

import javax.swing.*;

public class GameWindow {

    private final int targetFPS;

    private final Render2D renderer;
    private GameLoop gameLoop;

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

    public final void setScene(Scene scene) {
        scene.setRenderer(renderer);

        if (gameLoop == null) {
            gameLoop = new GameLoop(targetFPS, scene::processFrame, null);
            gameLoop.start();
        } else {
            gameLoop.setOnUpdate(scene::processFrame);
        }

    }

}
