package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameobjects.Dimension;
import rroyo.jge.core.gameutils.Camera;
import rroyo.jge.core.gameutils.Keyboard;

import javax.swing.*;

public class GameWindow {

    private final Render2D renderer;
    private GameLoop gameLoop;

    public GameWindow(int targetFPS, Dimension dimension, Scene scene) {
        Camera.setWindowDimensions(dimension);

        JFrame frame = new JFrame();
        frame.addKeyListener(Keyboard.listener);

        renderer = new Render2D(dimension);
        frame.add(renderer);

        setScene(targetFPS, scene);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public final void setScene(int targetFPS, Scene scene) {
        scene.setRenderer(renderer);
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop.setOnRender(scene::processFrame);
            gameLoop.start();
        } else {
            gameLoop = new GameLoop(targetFPS, scene::processFrame, null);
            gameLoop.start();
        }
    }

}
