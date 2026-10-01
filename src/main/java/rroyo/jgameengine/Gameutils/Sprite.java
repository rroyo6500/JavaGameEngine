package rroyo.jgameengine.Gameutils;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
public class Sprite {

    private final Dimension dimension;

    @Setter
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

    public boolean hasSpiteImage() {
        return spriteImage != null;
    }

}
