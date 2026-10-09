package rroyo.jgameengine.core.gameessentials;

import rroyo.jgameengine.core.gameobjects.Dimension;
import rroyo.jgameengine.core.gameutils.Camera;
import rroyo.jgameengine.core.gameutils.Keyboard;

import javax.swing.*;

public class GameWindow {

    public GameWindow(int targetFPS, Dimension dimension, Scene scene) {
        Camera.setWindowDimensions(dimension);

        JFrame frame = new JFrame();
        frame.addKeyListener(Keyboard.listener);

        Render2D renderer = new Render2D(dimension);
        frame.add(renderer);

        scene.setRenderer(renderer);

        GameLoop loop = new GameLoop(targetFPS, scene::processFrame, null);
        loop.start();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
