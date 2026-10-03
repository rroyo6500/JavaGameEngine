package rroyo.jgameengine.core.assets;

public class Asset {

    private boolean closed = false;

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

}
