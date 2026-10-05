package rroyo.jgameengine;

import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.GameObject;
import rroyo.jgameengine.core.gameobjects.Group;
import rroyo.jgameengine.core.gameobjects.Dimension;
import rroyo.jgameengine.core.gameobjects.Text;
import rroyo.jgameengine.core.gameutils.Camera;
import rroyo.jgameengine.core.gameutils.Time;

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
    protected final List<Text> texts = new ArrayList<>();
    protected final Dimension dimension;

    protected final JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());

            double centerX = dimension.getHalfWidth();
            double centerY = dimension.getHalfHeight();
            double zoom = Camera.getZoom();
            double camX = Camera.getCameraPosition().getX();
            double camY = Camera.getCameraPosition().getY();

            double viewWorldX = centerX - (centerX / zoom) - camX;
            double viewWorldY = centerY - (centerY / zoom) - camY;
            double viewWorldWidth = dimension.getWidth() / zoom;
            double viewWorldHeight = dimension.getHeight() / zoom;

            for (GameElement go : gameElements) {
                if (go.isDeleted()) continue;

                double objX = go.getPoint().getX() - go.getDimension().getHalfWidth();
                double objY = go.getPoint().getY() - go.getDimension().getHalfHeight();
                double objW = go.getDimension().getWidth();
                double objH = go.getDimension().getHeight();

                if (objX + objW < viewWorldX || objX > viewWorldX + viewWorldWidth ||
                        objY + objH < viewWorldY || objY > viewWorldY + viewWorldHeight
                ) continue;

                double worldX, worldY, width, height;

                go.getSprite().next();
                if (go.getSprite().hasSpriteImage()) {
                    worldX = switch (go.getSprite().getHorizontalDirection()) {
                        case RIGHT -> go.getPoint().getX() - go.getDimension().getHalfWidth();
                        case LEFT -> (go.getPoint().getX() - go.getDimension().getHalfWidth()) + go.getDimension().getWidth();
                    };
                    worldY = go.getPoint().getY() - go.getDimension().getHalfHeight();

                    width = switch (go.getSprite().getHorizontalDirection()) {
                        case RIGHT -> go.getDimension().getWidth();
                        case LEFT -> -go.getDimension().getWidth();
                    };
                    height = go.getDimension().getHeight();

                    int drawX = (int) (((worldX + camX) - centerX) * zoom + centerX);
                    int drawY = (int) (((worldY + camY) - centerY) * zoom + centerY);
                    int drawW = (int) (width * zoom);
                    int drawH = (int) (height * zoom);

                    g.drawImage(go.getSprite().getSpriteImages(), drawX, drawY, drawW, drawH, null);
                }
                else {
                    worldX = go.getPoint().getX() - go.getDimension().getHalfWidth();
                    worldY = go.getPoint().getY() - go.getDimension().getHalfHeight();
                    width = go.getDimension().getWidth();
                    height = go.getDimension().getHeight();

                    int drawX = (int) (((worldX + camX) - centerX) * zoom + centerX);
                    int drawY = (int) (((worldY + camY) - centerY) * zoom + centerY);
                    int drawW = (int) (width * zoom);
                    int drawH = (int) (height * zoom);

                    g.setColor(go.getSprite().getSpriteColor());
                    g.fillRect(drawX, drawY, drawW, drawH);
                }
            }
            for (Text text : texts) {
                if (text.getText() != null) g.setColor(text.getForeground());
                g.drawString(text.getText(), (int) text.getPoint().getX()+5, (int) text.getPoint().getY()+15);
            }
            canvas(g);
        }
    };

    public GameFrame(int fps, Dimension dimensions) {
        this.fps = fps;
        this.dimension = dimensions;
        Camera.setWindowDimensions(dimensions);

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
                if (group.getMembers() == null) continue;
                for (GameElement ge : group.getMembers()) {
                    if (!gameElements.contains(ge)) {
                        gameElements.add(ge);
                    }
                }
            }
        }

    }

    protected final void drawText(Text... text) {
        for (Text t : text) {
            if (!texts.contains(t)) {
                texts.add(t);
            }
        }
    }

    public void start() {
        timer.schedule(new TimerTask() {

            long lastTime = System.nanoTime();

            @Override
            public void run() {

                long currentTime = System.nanoTime();

                float deltaTime = (currentTime - lastTime) / 1_000_000_000f;

                lastTime = currentTime;

                if (deltaTime > 0.1f) deltaTime = 0.1f;

                try {
                    Time.setDeltaTime(deltaTime);
                    rroyo.jgameengine.core.gameutils.
                            Timer.update();
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

    public List<String> getKeyCodes() {
        return keyCodes;
    }

    public List<String> getKeyCharacters() {
        return keyCharacters;
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

