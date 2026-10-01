package rroyo.jgameengine;

import rroyo.jgameengine.objects.gameobjects.GameObject;

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

    protected final JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (GameObject go : gameObjects) {
                if (go.getSprite().hasSpiteImage())
                    g.drawImage(
                            go.getSprite().getSpriteImage(),
                            (int) go.getPoint().getX() - (go.getSprite().getDimension().width / 2),
                            (int) go.getPoint().getY() - (go.getSprite().getDimension().height / 2),
                            go.getSprite().getDimension().width, go.getSprite().getDimension().height,
                            null
                    );
                else {
                    g.setColor(go.getSprite().getSpriteColor());
                    g.fillRect(
                            (int) go.getPoint().getX() - (go.getSprite().getDimension().width / 2),
                            (int) go.getPoint().getY() - (go.getSprite().getDimension().height / 2),
                            go.getSprite().getDimension().width, go.getSprite().getDimension().height
                    );
                }
            }
            canvas(g);
        }
    };

    public GameFrame(int fps, Dimension dimensions) {
        this.fps = fps;

        JFrame frame = new JFrame();
        frame.setSize(dimensions);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(panel);

        frame.setVisible(true);
    }

    protected abstract void code(GameFrame self);
    protected void canvas(Graphics g) {
    }

    protected final void draw(GameObject... gameObject) {
        gameObjects.addAll(Arrays.asList(gameObject));
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



}

