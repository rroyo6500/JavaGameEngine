package rroyo.jge.core.assets;

import rroyo.jge.enums.SpriteHorizontalDirection;
import rroyo.jge.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code AssetGroup} class is a container for managing a collection of {@link Asset} objects,
 * identified by unique string keys. It provides methods to add, retrieve, remove, and perform
 * bulk operations on its contained assets, such as updating the direction of all sprites within the group.
 */
public class AssetGroup {

    /**
     * A map storing the assets, where the key is a unique string identifier and the value is the {@link Asset}.
     */
    private final Map<String, Asset> sprites = new HashMap<>();

    /**
     * Default constructor for creating an empty {@code AssetGroup}.
     */
    public AssetGroup() {}

    /**
     * Sets the horizontal direction for all {@link Sprite} assets contained within this group.
     * Assets that are not of type {@code Sprite} are ignored.
     *
     * @param direction The new {@link SpriteHorizontalDirection} to apply to all sprites in the group.
     */
    public void setHorizontalDirection(SpriteHorizontalDirection direction) {
        for (Asset asset : sprites.values()) {
            if (asset instanceof Sprite sprite)
                sprite.setHorizontalDirection(direction);
        }
    }

    /**
     * Adds a new asset to the group with the specified identifier.
     *
     * @param id     The unique string identifier for the asset.
     * @param asset The {@link Asset} (e.g., a Sprite or Audio) to add.
     * @return This {@code AssetGroup} instance, allowing for method chaining.
     */
    public AssetGroup add(String id, Asset asset) {
        sprites.put(id, asset);
        return this;
    }

    /**
     * Retrieves an asset from the group by its identifier.
     * Logs a warning message if the identifier does not exist in the group.
     *
     * @param id The unique string identifier of the asset to retrieve.
     * @return The {@link Asset} associated with the ID, or {@code null} if it does not exist.
     */
    public Asset get(String id) {
        if (!sprites.containsKey(id))
            Log.warn("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        return sprites.get(id);
    }

    /**
     * Retrieves a list of all assets currently contained in this group.
     * The returned list is a copy, so modifying it will not affect the group's internal map.
     *
     * @return An unmodifiable {@link List} of all {@link Asset}s in the group.
     */
    public List<Asset> getMembers() {
        return List.copyOf(sprites.values());
    }

    /**
     * Removes an asset from the group by its identifier.
     * Logs a warning message if the identifier does not exist before attempting removal.
     *
     * @param id The unique string identifier of the asset to remove.
     */
    public void remove(String id) {
        if (!sprites.containsKey(id)) Log.warn("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        sprites.remove(id);
    }

}
