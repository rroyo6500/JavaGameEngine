package rroyo.jge.core.assets;

/**
 * The {@code Asset} class serves as a base class for all resources used within the game,
 * such as sprites, audio, or other media files. It provides a common mechanism to manage
 * the lifecycle of an asset, specifically allowing it to be marked as closed or disposed.
 */
public class Asset {

    /**
     * A flag indicating whether this asset has been closed.
     * When true, the asset should no longer be used or accessed.
     */
    private boolean closed = false;

    /**
     * Marks this asset as closed. Subclasses should check the {@link #isClosed()} status
     * before performing operations to prevent errors if the asset's resources have been released.
     */
    public void close() {
        this.closed = true;
    }

    /**
     * Checks if this asset has been closed.
     *
     * @return {@code true} if the asset is closed; {@code false} otherwise.
     */
    public boolean isClosed() {
        return closed;
    }

}
