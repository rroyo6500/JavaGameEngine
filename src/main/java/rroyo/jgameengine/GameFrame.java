package rroyo.jgameengine;

import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.GameObject;
import rroyo.jgameengine.core.gameobjects.Group;
import rroyo.jgameengine.core.gameutils.Dimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.instrument.IllegalClassFormatException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public abstract class GameFrame {

    protected final Timer timer = new Timer();
    protected final int fps;
    protected Color backgroundColor = Color.BLACK;

    protected final List<String> keyCodes = new ArrayList<>();
    protected final List<String> keyCharacters = new ArrayList<>();

    protected final List<GameElement> gameElements = new ArrayList<>();
    protected final Dimension dimension;

    protected final JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (GameElement go : gameElements) {
                go.getSprite().next();
                if (go.getSprite().hasSpriteImage())
                    g.drawImage(
                            go.getSprite().getSpriteImages(),
                            (int) switch (go.getSprite().getHorizontalDirection()) {
                                case RIGHT -> (go.getPoint().getX() - go.getDimension().getHalfWidth());
                                case LEFT -> (go.getPoint().getX() - go.getDimension().getHalfWidth()) + go.getDimension().getWidth();
                            },
                            (int) (go.getPoint().getY() - go.getDimension().getHalfHeight()),
                            (int) switch (go.getSprite().getHorizontalDirection()) {
                                case RIGHT -> go.getDimension().getWidth();
                                case LEFT -> -go.getDimension().getWidth();
                            },
                            (int) go.getDimension().getHeight(),
                            null
                    );
                else {
                    g.setColor(go.getSprite().getSpriteColor());
                    g.fillRect(
                            (int) (go.getPoint().getX() - go.getDimension().getHalfWidth()),
                            (int) (go.getPoint().getY() - go.getDimension().getHalfHeight()),
                            (int) go.getDimension().getWidth(), (int) go.getDimension().getHeight()
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

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!keyCodes.contains(String.valueOf(e.getKeyCode()))) {
                    keyCodes.add(String.valueOf(e.getKeyCode()));
                    keyCharacters.add(String.valueOf(e.getKeyChar()));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyCodes.remove(String.valueOf(e.getKeyCode()));
                keyCharacters.remove(String.valueOf(e.getKeyChar()));
            }
        });

        frame.setVisible(true);
    }

    protected abstract void code() throws IllegalClassFormatException;
    protected void canvas(Graphics g) {
    }

    protected final void draw(GameObject... gameElement) {
        for (GameObject go : gameElement) {
            if (go instanceof GameElement ge) {
                if (!gameElements.contains(ge)) {
                    gameElements.add(ge);
                }
            } else if (go instanceof Group group) {
                for (GameElement ge : group.getMembers()) {
                    if (!gameElements.contains(ge)) {
                        gameElements.add(ge);
                    }
                }
            }
        }

    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    code();
                    panel.repaint();
                } catch (IllegalClassFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, (1000 / fps));
    }

    public void stop() {
        timer.cancel();
    }

    public boolean key(String key) {
        return keyCharacters.contains(key);
    }

    public boolean key(int keyCode) {
        return keyCodes.contains(String.valueOf(keyCode));
    }

    public boolean key(KeyEvent key) {
        return keyCodes.contains(String.valueOf(key.getKeyCode()));
    }

    public boolean keyUp() {
        return keyCodes.contains(String.valueOf(KeyEvent.VK_UP));
    }

    public boolean keyDown() {
        return keyCodes.contains(String.valueOf(KeyEvent.VK_DOWN));
    }

    public boolean keyLeft() {
        return keyCodes.contains(String.valueOf(KeyEvent.VK_LEFT));
    }

    public boolean keyRight() {
        return keyCodes.contains(String.valueOf(KeyEvent.VK_RIGHT));
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

