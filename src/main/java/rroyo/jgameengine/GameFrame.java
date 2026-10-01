package rroyo.jgameengine;

import rroyo.jgameengine.GameObjects.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public abstract class GameFrame {

    protected final Graphics2D g;
    protected Timer timer = new Timer();
    protected final int fps;

    public GameFrame(JPanel frame, int fps) {
        this((Graphics2D) frame.getGraphics(), fps);
    }

    public GameFrame(Graphics2D g, int fps) {
        this.g = g;
        this.fps = fps;
    }

    protected abstract void code();

    protected void draw(GameObject... gameObject) {
        for (GameObject go : gameObject) {
            if (go.getSprite().hasSpiteImage())
                g.drawImage(
                        go.getSprite().getSpriteImage(),
                        go.getPoint().x,
                        go.getPoint().y,
                        go.getSprite().getDimension().width,
                        go.getSprite().getDimension().height,
                        null
                );
            else {
                g.setColor(go.getSprite().getSpriteColor());
                g.fillRect(
                        go.getPoint().x,
                        go.getPoint().y,
                        go.getSprite().getDimension().width,
                        go.getSprite().getDimension().height
                );
            }
        }

    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 0, (1000 / fps));
    }

}

