package rroyo.jgameengine.objects.gameutils;

import rroyo.jgameengine.objects.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {

    private final Dimension dimension;

    private BufferedImage spriteImage;
    private Color spriteColor;

    public Sprite(Dimension dimension, BufferedImage spriteImage) {
        this.spriteImage = spriteImage;
        this.dimension = dimension;
    }

    public Sprite(Dimension dimension, Color spriteColor) {
        this.spriteColor = spriteColor;
        this.dimension = dimension;
    }

    public boolean hasSpriteImage() {
        return spriteImage != null;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public BufferedImage getSpriteImage() {
        return spriteImage;
    }

    public Color getSpriteColor() {
        return spriteColor;
    }
}
