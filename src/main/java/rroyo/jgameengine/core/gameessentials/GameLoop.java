package rroyo.jgameengine.core.gameessentials;

import rroyo.jgameengine.core.gameutils.Time;
import rroyo.jgameengine.core.gameutils.Timer;

public class GameLoop implements Runnable {

    private Thread thread;
    private boolean running = false;

    private final int targetFPS;
    private final Runnable onUpdate;
    private final Runnable onRender;

    public GameLoop(int targetFPS, Runnable onUpdate, Runnable onRender) {
        this.targetFPS = targetFPS;
        this.onUpdate = onUpdate;
        this.onRender = onRender;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this, "JGameEngine-MainLoop");
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();

        final double timePerFrame = 1_000_000_000.0 / targetFPS;

        while (running) {
            long now = System.nanoTime();
            float deltaTime = (now - lastTime) / 1_000_000_000f;

            if (now - lastTime >= timePerFrame) {
                lastTime = now;

                if (deltaTime > 0.1f) deltaTime = 0.1f;

                Time.setDeltaTime(deltaTime);
                Timer.update();

                if (onUpdate != null) onUpdate.run();
                if (onRender != null) onRender.run();
            } else {
                long timeToSleep = (long) ((timePerFrame - (now - lastTime)) / 1_000_000f);
                if (timeToSleep > 0) {
                    try {
                        Thread.sleep(timeToSleep);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

        }

    }

}
