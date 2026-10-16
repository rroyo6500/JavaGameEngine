package rroyo.jge.core.assets;

import rroyo.jge.enums.SpriteHorizontalDirection;
import rroyo.jge.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Named collection of {@link Asset} instances for convenient lookup and bulk asset operations.
 * Keys are unique within this group; adding an existing key replaces its previous value. The group
 * does not automatically close assets when they are removed or when the group becomes unreachable.
 */
public class AssetGroup {

    /**
     * A map storing the assets, where the key is a unique string identifier and the value is the {@link Asset}.
     */
    private final Map<String, Asset> assets = new HashMap<>();

    /**
     * Default constructor for creating an empty {@code AssetGroup}.
     */
    public AssetGroup() {}

    /**
     * Sets the facing direction of every {@link Sprite} in this group. Other asset types are ignored.
     *
     * @param direction The new {@link SpriteHorizontalDirection} to apply to all sprites in the group.
     */
    public void setHorizontalDirection(SpriteHorizontalDirection direction) {
        for (Asset asset : assets.values()) {
            if (asset instanceof Sprite sprite)
                sprite.setHorizontalDirection(direction);
        }
    }

    /**
     * Adds or replaces an asset under the specified identifier.
     *
     * @param id     The unique string identifier for the asset.
     * @param asset The {@link Asset} (e.g., a Sprite or Audio) to add.
     * @return This {@code AssetGroup} instance, allowing for method chaining.
     */
    public AssetGroup add(String id, Asset asset) {
        assets.put(id, asset);
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
        if (!assets.containsKey(id))
            Log.warn("Asset ID '" + id + "' does not exist in the AssetGroup");
        return assets.get(id);
    }

    /**
     * Retrieves an immutable snapshot of all assets currently contained in this group.
     * The order follows the backing map's iteration order and is not guaranteed to be stable.
     *
     * @return An unmodifiable {@link List} of all {@link Asset}s in the group.
     */
    public List<Asset> getMembers() {
        return List.copyOf(assets.values());
    }

    /**
     * Removes an asset from the group by its identifier.
     * Logs a warning message if the identifier does not exist before attempting removal.
     *
     * @param id The unique string identifier of the asset to remove.
     */
    public void remove(String id) {
        if (!assets.containsKey(id)) Log.warn("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        assets.remove(id);
    }

}
