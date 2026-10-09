package rroyo.jge.core.assets;

import rroyo.jge.enums.SpriteHorizontalDirection;
import rroyo.jge.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Asset> getMembers() {
        return List.copyOf(sprites.values());
    }

    public void remove(String id) {
        if (!sprites.containsKey(id)) Log.warning("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        sprites.remove(id);
    }

}
