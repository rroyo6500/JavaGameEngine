package rroyo.jgameengine;

import rroyo.jgameengine.objects.gameobjects.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public abstract class GameFrame {

    private final JPanel panel;
    protected final Graphics2D g;
    protected Timer timer = new Timer();
    protected final int fps;

    public GameFrame(JPanel frame, int fps) {
        this.panel = frame;
        this.g = (Graphics2D) panel.getGraphics().create();
        this.fps = fps;
    }

    protected abstract void code();

    protected void draw(GameObject... gameObject) {
        for (GameObject go : gameObject) {
            Point p = new Point(
                    (int) go.getPoint().getX(),
                    (int) go.getPoint().getX());
            Dimension d = new Dimension(
                    go.getSprite().getDimension().width,
                    go.getSprite().getDimension().height);

            if (go.getSprite().hasSpiteImage())
                g.drawImage(
                        go.getSprite().getSpriteImage(),
                        p.x - (d.width / 2), p.y - (d.height / 2),
                        d.width, d.height,
                        null
                );
            else {
                g.setColor(go.getSprite().getSpriteColor());
                g.fillRect(
                        p.x - (d.width / 2), p.y - (d.height / 2),
                        d.width, d.height
                );
            }
        }

    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                code();
                panel.repaint();
            }
        }, 0, (1000 / fps));
    }

}

