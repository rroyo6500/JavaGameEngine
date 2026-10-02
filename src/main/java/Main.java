import rroyo.jgameengine.GameFrame;
import rroyo.jgameengine.core.gameutils.*;
import rroyo.jgameengine.core.gameutils.Dimension;
import rroyo.jgameengine.core.gameutils.Point;
import rroyo.jgameengine.enums.SpriteHorizontalDirection;
import rroyo.jgameengine.interfaces.Colision;
import rroyo.jgameengine.interfaces.Portble;
import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.Group;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.instrument.IllegalClassFormatException;

void main(String[] args) throws IOException {

    Sprite stand = new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario.gif")));
    Sprite jump = new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario - Jump.gif")));
    Sprite walk = new Sprite(
            10,
            ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk1.gif")),
            ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk2.gif")),
            ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk3.gif"))
    );

    Entity entity = new Entity(250, 0, 32, 64, stand);
    GameElement play = new GameElement(50, 300, 50, 50, new Sprite(Color.GREEN));
    GameElement pause = new GameElement(375, 300, 50, 50, new Sprite(Color.ORANGE));
    GameElement resume = new GameElement(125, 300, 50, 50, new Sprite(Color.YELLOW));
    GameElement stop = new GameElement(450, 300, 50, 50, new Sprite(Color.RED));
    GameElement loop = new GameElement(200, 300, 50, 50, new Sprite(Color.MAGENTA));

    Group group = new Group(
            play,
            pause,
            resume,
            stop,
            loop,
            new GameElement(250, 490, 500, 100, new Sprite(Color.gray))
    );

    Audio audio;
    try {
        audio = new Audio(new File("src/main/resources/audio/Queen - Under Pressure.wav"));
    } catch (LineUnavailableException | UnsupportedAudioFileException e) {
        throw new RuntimeException(e);
    }

    GameFrame gf = new GameFrame(60, new Dimension(500, 500)) {

        boolean salto = false;

        @Override
        protected void code() throws IllegalClassFormatException {

            if (((entity.getPoint().getY() + entity.getDimension().getHalfHeight()) < getDimension().getHeight())) {
                entity.setVelocityY(
                        entity.getVelocityY() + 0.1
                );
            } else {
                entity.getPoint().setY(getDimension().getHeight() - entity.getDimension().getHalfHeight());
                salto = true;
            }

            if (keyLeft()) {
                entity.setVelocityX(-2);
                entity.setSprite(walk);
                walk.setHorizontalDirection(SpriteHorizontalDirection.LEFT);
                stand.setHorizontalDirection(SpriteHorizontalDirection.LEFT);
                jump.setHorizontalDirection(SpriteHorizontalDirection.LEFT);
            } else if (keyRight()) {
                entity.setVelocityX(2);
                entity.setSprite(walk);
                walk.setHorizontalDirection(SpriteHorizontalDirection.RIGHT);
                stand.setHorizontalDirection(SpriteHorizontalDirection.RIGHT);
                jump.setHorizontalDirection(SpriteHorizontalDirection.RIGHT);
            } else {
                entity.setVelocityX(0);
                entity.setSprite(stand);
            }

            if (!salto) {
                entity.setSprite(jump);
            }

            if (salto && keyUp()) {
                entity.setVelocityY(-4);
                salto = false;
            }

            if (entity.collide(play)) audio.play();
            else if (entity.collide(pause)) audio.pause();
            else if (entity.collide(resume)) audio.resume();
            else if (entity.collide(stop)) audio.stop();
            else if (entity.collide(loop)) audio.loop();

            if (entity.collide(group)) {
                salto = true;
            }

            draw(entity, group);
        }

        @Override
        protected void canvas(Graphics g) {
            super.canvas(g);



        }
    };

    gf.start();

}

private class Entity extends GameElement implements Colision, Portble {

    public Entity(double x, double y, double width, double height, Sprite sprite) {
        super(x, y, width, height, sprite);
    }

    public Entity(Point point, Dimension dimension, Sprite sprite) {
        super(point, dimension, sprite);
    }

}