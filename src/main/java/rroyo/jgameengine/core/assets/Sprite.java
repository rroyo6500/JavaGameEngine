package rroyo.jgameengine.core.assets;

import rroyo.jgameengine.core.gameutils.FrameWaiter;
import rroyo.jgameengine.core.gameutils.Time;
import rroyo.jgameengine.enums.SpriteHorizontalDirection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprite extends Asset {

    private SpriteHorizontalDirection horizontalDirection = SpriteHorizontalDirection.RIGHT;
    private List<BufferedImage> spriteImages = new ArrayList<>();
    private Color spriteColor;
    private int selectedSprite = 0;

    private float frameDuration = 0.0f;
    private float frameTimer = 0.0f;

    public Sprite(BufferedImage... spriteImage) {
        spriteImages.addAll(Arrays.asList(spriteImage));
    }

    public Sprite(Color spriteColor) {
        this.spriteColor = spriteColor;
    }

    public Sprite(float frameDuration, BufferedImage... spriteImage) {
        this(spriteImage);
        this.frameDuration = frameDuration;
    }

    public Sprite(float frameDuration, Color spriteColor) {
        this.spriteColor = spriteColor;
        this.frameDuration = frameDuration;
    }

    public boolean hasSpriteImage() {
        if (isClosed()) return false;
        return !spriteImages.isEmpty();
    }

    public BufferedImage getSpriteImages() {
        if (isClosed()) return null;
        return spriteImages.get(selectedSprite);
    }

    public Color getSpriteColor() {
        if (isClosed()) return null;
        return spriteColor;
    }

    public void setSpriteColor(Color spriteColor) {
        if (isClosed()) return;
        this.spriteColor = spriteColor;
    }

    public SpriteHorizontalDirection getHorizontalDirection() {
        return horizontalDirection;
    }

    public void setHorizontalDirection(SpriteHorizontalDirection horizontalDirection) {
        if (isClosed()) return;
        this.horizontalDirection = horizontalDirection;
    }

    public void next() {
        if (isClosed()) return;
        if (frameDuration <= 0 || spriteImages.size() <= 1) return;

        frameTimer += Time.deltaTime();

        if (frameTimer >= frameDuration) {
            selectedSprite++;
            if (selectedSprite >= spriteImages.size()) selectedSprite = 0;
            frameTimer -= frameDuration;
        }
    }

}
