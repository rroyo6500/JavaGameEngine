package rroyo.jge.core.gameobjects;

/**
 * The {@code GameObject} class is the fundamental base class for all entity types
 * in the game engine, such as elements, groups, and text. It provides a common
 * mechanism to manage the life cycle state, allowing objects to be flagged for deletion.
 */
public class GameObject {

    /**
     * A flag indicating if this object has been marked for deletion.
     */
    private boolean deleted = false;

    /**
     * Marks this object for deletion. The game engine's loop will typically
     * check this flag and remove the object from active processing arrays.
     */
    public void delete() {
        this.deleted = true;
    }

    /**
     * Unmarks the object, effectively reviving it so it won't be purged by the engine.
     */
    public final void revive() {
        this.deleted = false;
    }

    /**
     * Checks if this object is scheduled to be removed from the game.
     *
     * @return {@code true} if marked for deletion; {@code false} otherwise.
     */
    public final boolean isDeleted() {
        return deleted;
    }
}
