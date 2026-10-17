package rroyo.jge.testing;

import rroyo.jge.core.gameessentials.Scene;

/** Minimal scene fixture for tests that do not need a Swing window. */
public final class TestScene extends Scene {
    /** Number of times the scene's update callback has run. */
    private int updateCalls;

    /** Increments the update-call counter for each processed frame. */
    @Override
    public void update() {
        updateCalls++;
    }

    /**
     * Returns how many times {@link #update()} has run.
     *
     * @return number of update calls recorded by this scene
     */
    public int getUpdateCalls() {
        return updateCalls;
    }
}
