package rroyo.jge.core.assets;

/**
 * Base type for resources owned or used by a game, such as image sequences and audio clips.
 * Subclasses release their underlying resources in {@link #dispose()} when {@link #close()}
 * is called. Closing an asset is idempotent; clients should not expect a closed asset to remain usable.
 */
public abstract class Asset implements AutoCloseable {

    /**
     * Tracks whether disposal has completed, preventing repeated release of the same resource.
     */
    private boolean closed = false;

    /** Creates an open asset whose subclass resources can be disposed with {@link #close()}. */
    protected Asset() {}

    /**
     * Releases this asset's resources once. The closed state is set after subclass disposal
     * completes; if disposal throws, the asset remains open so the failure is not hidden.
     */
    @Override
    public final void close() {
        if (!closed) {
            dispose();
            closed = true;
        }
    }

    /**
     * Releases subclass-specific resources such as image collections or audio lines.
     * Implementations should tolerate being called once and should not expose disposed resources.
     */
    protected abstract void dispose();

    /**
     * Reports whether {@link #close()} has completed successfully.
     *
     * @return {@code true} after disposal; otherwise {@code false}
     */
    public final boolean isClosed() {
        return closed;
    }
}
