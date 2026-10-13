package rroyo.jge.core.assets;

/**
 * The {@code Asset} class serves as a base class for all resources used within the game,
 * such as sprites, audio, or other media files. It provides a common mechanism to manage
 * the lifecycle of an asset, specifically allowing it to be marked as closed or disposed.
 */
public abstract class Asset implements AutoCloseable {

    /**
     * A flag indicating whether this asset has been closed.
     * When true, the asset should no longer be used or accessed.
     */
    private boolean closed = false;

    @Override
    public final void close() {
        if (!closed) {
            dispose();
            closed = true;
        }
    }

    protected abstract void dispose();

    public final boolean isClosed() {
        return closed;
    }
}
