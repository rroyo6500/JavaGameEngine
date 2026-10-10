package rroyo.jge.core.gameessentials;

import rroyo.jge.core.gameutils.Time;
import rroyo.jge.core.gameutils.Timer;

/**
 * The {@code GameLoop} class is responsible for managing the main execution loop of the game.
 * It implements the {@link Runnable} interface, allowing it to run in a separate thread.
 * This loop ensures that the game's state is updated and rendered at a consistent frame rate.
 */
public class GameLoop implements Runnable {

    /**
     * The thread in which the game loop will run.
     */
    private Thread thread;

    /**
     * A flag indicating whether the game loop is currently running.
     */
    private boolean running = false;

    /**
     * The target frames per second (FPS) at which the game loop should attempt to run.
     */
    private final int targetFPS;

    /**
     * A {@link Runnable} representing the logic to update the game state.
     * This is executed once per frame.
     */
    private Runnable onUpdate;

    /**
     * A {@link Runnable} representing the logic after render the game.
     * This is executed once per frame after the update logic.
     */
    private Runnable onRender;

    /**
     * Constructs a new {@code GameLoop} with the specified target FPS, update logic, and logic after render.
     *
     * @param targetFPS The desired frames per second for the game loop.
     * @param onUpdate  The logic to execute to update the game state during each frame.
     * @param onRender  The logic to execute after render the game.
     */
    public GameLoop(int targetFPS, Runnable onUpdate, Runnable onRender) {
        this.targetFPS = targetFPS;
        this.onUpdate = onUpdate;
        this.onRender = onRender;
    }

    /**
     * Starts the game loop. If the loop is already running, this method does nothing.
     * It creates a new thread named "JGameEngine-MainLoop" and starts it.
     */
    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this, "JGameEngine-MainLoop");
        thread.start();
    }

    /**
     * Stops the game loop gracefully. If the loop is not running, this method does nothing.
     * It sets the running flag to false and waits for the thread to terminate using {@code join()}.
     * If interrupted while waiting, it restores the interrupted status of the thread.
     */
    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The core execution loop of the game.
     * It manages the timing to achieve the target FPS.
     * In each iteration, it calculates the delta time, updates the global {@link Time} and {@link Timer} utilities,
     * and calls the provided update and render logic.
     * If a frame finishes earlier than the target time per frame, the thread sleeps for the remaining time
     * to prevent CPU overuse and maintain the target FPS.
     */
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

    /**
     * Sets or updates the logic to be executed for updating the game state.
     *
     * @param onUpdate A {@link Runnable} containing the new update logic.
     */
    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    /**
     * Sets or updates the logic to be executed after rendering the game.
     *
     * @param onRender A {@link Runnable} containing the new render logic.
     */
    public void setOnRender(Runnable onRender) {
        this.onRender = onRender;
    }

}
