package rroyo.jgameengine.core.gameobjects;

public class GameObject {

    private boolean deleted = false;

    public void delete() {
        this.deleted = true;
    }

    public final void revive() {
        this.deleted = false;
    }

    public final boolean isDeleted() {
        return deleted;
    }
}
