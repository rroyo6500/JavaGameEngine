package rroyo.jgameengine.core.gameutils;

import rroyo.jgameengine.enums.SpriteHorizontalDirection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprite {

    private SpriteHorizontalDirection horizontalDirection = SpriteHorizontalDirection.RIGHT;
    private final List<BufferedImage> spriteImages = new ArrayList<>();
    private int selectedSprite = 0;
    private int frameDelay = 0;

    private Color spriteColor;

    public Sprite(BufferedImage... spriteImage) {
        spriteImages.addAll(Arrays.asList(spriteImage));
    }

    public Sprite(Color spriteColor) {
        this.spriteColor = spriteColor;
    }

    public Sprite(int frameDelay, BufferedImage... spriteImage) {
        this(spriteImage);
        this.frameDelay = frameDelay;
    }

    public Sprite(int frameDelay, Color spriteColor) {
        this.spriteColor = spriteColor;
        this.frameDelay = frameDelay;
    }

    public boolean hasSpriteImage() {
        return !spriteImages.isEmpty();
    }

    public BufferedImage getSpriteImages() {
        return spriteImages.get(selectedSprite);
    }

    public Color getSpriteColor() {
        return spriteColor;
    }

    public void setSpriteColor(Color spriteColor) {
        this.spriteColor = spriteColor;
    }

    public SpriteHorizontalDirection getHorizontalDirection() {
        return horizontalDirection;
    }

    public void setHorizontalDirection(SpriteHorizontalDirection horizontalDirection) {
        this.horizontalDirection = horizontalDirection;
    }

    public void next() {
        if (frameDelay == 0) return;
        if (FrameWaiter.wait(String.valueOf(this.hashCode()), frameDelay)) {
            selectedSprite++;
            if (selectedSprite == spriteImages.size())
                selectedSprite = 0;
        }
    }

}
