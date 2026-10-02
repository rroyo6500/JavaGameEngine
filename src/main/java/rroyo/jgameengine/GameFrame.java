package rroyo.jgameengine;

import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Dimension;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public abstract class GameFrame {

    protected final Timer timer = new Timer();
    protected final int fps;
    protected Color backgroundColor = Color.BLACK;

    protected final List<GameObject> gameObjects = new ArrayList<>();
    protected final Dimension dimension;

    protected final JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (GameObject go : gameObjects) {
                if (go.getSprite().hasSpriteImage())
                    g.drawImage(
                            go.getSprite().getSpriteImage(),
                            (int) (go.getPoint().getX() - go.getSprite().getDimension().getHalfWidth()),
                            (int) (go.getPoint().getY() - go.getSprite().getDimension().getHalfHeight()),
                            (int) go.getSprite().getDimension().getWidth(), (int) go.getSprite().getDimension().getHeight(),
                            null
                    );
                else {
                    g.setColor(go.getSprite().getSpriteColor());
                    g.fillRect(
                            (int) (go.getPoint().getX() - go.getSprite().getDimension().getHalfWidth()),
                            (int) (go.getPoint().getY() - go.getSprite().getDimension().getHalfHeight()),
                            (int) go.getSprite().getDimension().getWidth(), (int) go.getSprite().getDimension().getHeight()
                    );
                }
            }
            canvas(g);
        }
    };

    public GameFrame(int fps, Dimension dimensions) {
        this.fps = fps;
        this.dimension = dimensions;

        JFrame frame = new JFrame();
        frame.setSize(new java.awt.Dimension((int) dimensions.getWidth(), (int) dimensions.getHeight()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        panel.setPreferredSize(new java.awt.Dimension((int) dimensions.getWidth(), (int) dimensions.getHeight()));
        frame.setContentPane(panel);
        frame.pack();

        frame.setVisible(true);
    }

    protected abstract void code(GameFrame self);
    protected void canvas(Graphics g) {
    }

    protected final void draw(GameObject... gameObject) {
        for (GameObject go : gameObject)
            if (!gameObjects.contains(go))
                gameObjects.add(go);
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                code(GameFrame.this);
                panel.repaint();
            }
        }, 0, (1000 / fps));
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Dimension getDimension() {
        return dimension;
    }
}

