package rroyo.jgameengine.core.gameutils;

import rroyo.jgameengine.enums.SpriteHorizontalDirection;

import java.util.HashMap;
import java.util.Map;

public class SpriteGroup {

    private final Map<String, Sprite> sprites = new HashMap<>();

    public SpriteGroup() {}

    public void setHorizontalDirection(SpriteHorizontalDirection direction) {
        for (Sprite sprite : sprites.values()) {
            sprite.setHorizontalDirection(direction);
        }
    }

    public SpriteGroup add(String id, Sprite sprite) {
        sprites.put(id, sprite);
        return this;
    }

    public Sprite get(String id) {
        if (!sprites.containsKey(id))
            throw new NullPointerException("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        return sprites.get(id);
    }

    public void remove(String id) {
        if (!sprites.containsKey(id)) throw new NullPointerException("Sprite ID '" + id + "' does not exist in the SpriteGroup");
        sprites.remove(id);
    }

}
