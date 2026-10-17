package rroyo.jge.core.assets;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies image-frame initialization and asset cleanup for sprites. */
class SpriteTest {
    /** Confirms that an image sprite selects its first supplied frame initially. */
    @Test
    void imageSpriteStartsWithItsFirstFrame() {
        BufferedImage first = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        BufferedImage second = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        Sprite sprite = new Sprite(first, second);

        assertTrue(sprite.hasSpriteImage());
        assertSame(first, sprite.getSpriteImages());
    }

    /** Confirms that closing a sprite repeatedly is safe and disables asset access. */
    @Test
    void closingSpriteIsIdempotentAndDisablesAssetAccess() {
        Sprite sprite = new Sprite(Color.RED);

        assertDoesNotThrow(() -> {
            sprite.close();
            sprite.close();
        });
        assertTrue(sprite.isClosed());
        assertFalse(sprite.hasSpriteImage());
        assertNull(sprite.getSpriteColor());
    }
}
