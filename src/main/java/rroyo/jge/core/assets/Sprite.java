package rroyo.jge.core.assets;

import rroyo.jge.core.gameutils.Time;
import rroyo.jge.enums.SpriteHorizontalDirection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Sprite} class represents a visual asset for a game element. It extends {@link Asset}
 * and can be either an animated sequence of images (using {@link BufferedImage}) or a solid color.
 * It manages animation timing, current frame selection, and the sprite's facing direction.
 */
public class Sprite extends Asset {

    /**
     * The current horizontal direction the sprite is facing (e.g., LEFT or RIGHT).
     */
    private SpriteHorizontalDirection horizontalDirection = SpriteHorizontalDirection.RIGHT;

    /**
     * A list of images that make up the animation sequence for this sprite.
     */
    private List<BufferedImage> spriteImages = new ArrayList<>();

    /**
     * The solid color to use if this sprite is not image-based.
     */
    private Color spriteColor;

    /**
     * The index of the currently active image in the animation sequence.
     */
    private int selectedSprite = 0;

    /**
     * The duration in seconds that each frame of the animation should be displayed.
     */
    private float frameDuration = 0.0f;

    /**
     * An internal timer used to track how long the current frame has been displayed.
     */
    private float frameTimer = 0.0f;

    /**
     * Constructs a new image-based {@code Sprite} with one or more images.
     * If multiple images are provided without a frame duration, it will only display the first one
     * unless manually animated.
     *
     * @param spriteImage A variable number of {@link BufferedImage}s representing the sprite's frames.
     */
    public Sprite(BufferedImage... spriteImage) {
        spriteImages.addAll(Arrays.asList(spriteImage));
    }

    /**
     * Constructs a new color-based {@code Sprite} with a solid color.
     *
     * @param spriteColor The {@link Color} to be drawn for this sprite.
     */
    public Sprite(Color spriteColor) {
        this.spriteColor = spriteColor;
    }

    /**
     * Constructs a new animated image-based {@code Sprite}.
     *
     * @param frameDuration The amount of time (in seconds) to show each frame.
     * @param spriteImage   A variable number of {@link BufferedImage}s representing the animation frames.
     */
    public Sprite(float frameDuration, BufferedImage... spriteImage) {
        this(spriteImage);
        this.frameDuration = frameDuration;
    }

    /**
     * Checks if this sprite is backed by images rather than a solid color.
     *
     * @return {@code true} if the sprite contains one or more images and is not closed; {@code false} otherwise.
     */
    public boolean hasSpriteImage() {
        if (isClosed()) return false;
        return !spriteImages.isEmpty();
    }

    /**
     * Retrieves the current image for this sprite based on its animation state.
     *
     * @return The currently active {@link BufferedImage}, or {@code null} if the asset is closed.
     */
    public BufferedImage getSpriteImages() {
        if (isClosed()) return null;
        return spriteImages.get(selectedSprite);
    }

    /**
     * Retrieves the solid color of this sprite.
     *
     * @return The {@link Color} of the sprite, or {@code null} if the asset is closed.
     */
    public Color getSpriteColor() {
        if (isClosed()) return null;
        return spriteColor;
    }

    /**
     * Sets the solid color for this sprite.
     * If the asset is closed, this method does nothing.
     *
     * @param spriteColor The new {@link Color} to apply.
     */
    public void setSpriteColor(Color spriteColor) {
        if (isClosed()) return;
        this.spriteColor = spriteColor;
    }

    /**
     * Gets the current horizontal facing direction of the sprite.
     *
     * @return The current {@link SpriteHorizontalDirection}.
     */
    public SpriteHorizontalDirection getHorizontalDirection() {
        return horizontalDirection;
    }

    /**
     * Sets the horizontal facing direction of the sprite.
     * If the asset is closed, this method does nothing.
     *
     * @param horizontalDirection The new {@link SpriteHorizontalDirection}.
     */
    public void setHorizontalDirection(SpriteHorizontalDirection horizontalDirection) {
        if (isClosed()) return;
        this.horizontalDirection = horizontalDirection;
    }

    /**
     * Advances the sprite's animation to the next frame based on the elapsed time.
     * This method should be called once per game loop iteration. It accumulates delta time
     * and switches to the next image in the sequence when the frame duration is reached.
     * If the asset is closed, has no duration, or has only one frame, it does nothing.
     */
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
