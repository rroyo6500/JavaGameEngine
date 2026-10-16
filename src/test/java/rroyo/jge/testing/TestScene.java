package rroyo.jge.testing;

import rroyo.jge.core.gameessentials.Scene;

/** Minimal scene fixture for tests that do not need a Swing window. */
public final class TestScene extends Scene {
    private int updateCalls;

    @Override
    public void update() {
        updateCalls++;
    }

    public int getUpdateCalls() {
        return updateCalls;
    }
}
