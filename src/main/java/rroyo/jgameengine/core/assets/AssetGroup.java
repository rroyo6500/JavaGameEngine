package rroyo.jgameengine.core.assets;

import rroyo.jgameengine.enums.SpriteHorizontalDirection;
import rroyo.jgameengine.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AssetGroup {

    private final Map<String, Asset> sprites = new HashMap<>();

    public AssetGroup() {}

    public void setHorizontalDirection(SpriteHorizontalDirection direction) {
        for (Asset asset : sprites.values()) {
            if (asset instanceof Sprite sprite)
                sprite.setHorizontalDirection(direction);
        }
    }

    public AssetGroup add(String id, Asset sprite) {
        sprites.put(id, sprite);
        return this;
    }

    public Asset get(String id) {
        if (!sprites.containsKey(id))
            Log.warning("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        return sprites.get(id);
    }

    public void remove(String id) {
        if (!sprites.containsKey(id)) Log.warning("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        sprites.remove(id);
    }

}
