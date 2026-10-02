package rroyo.jgameengine.core.gameutils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {

    private BufferedImage spriteImage;
    private Color spriteColor;

    public Sprite(BufferedImage spriteImage) {
        this.spriteImage = spriteImage;
    }

    public Sprite(Color spriteColor) {
        this.spriteColor = spriteColor;
    }

    public boolean hasSpriteImage() {
        return spriteImage != null;
    }

    public BufferedImage getSpriteImage() {
        return spriteImage;
    }

    public Color getSpriteColor() {
        return spriteColor;
    }
}
