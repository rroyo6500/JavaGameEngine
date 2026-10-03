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

    SpriteGroup sprites = new SpriteGroup()
            .add("stand",
                    new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario.gif"))))
            .add("jump",
                    new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario - Jump.gif"))))
            .add("walk", new Sprite(
                    8,
                    ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk1.gif")),
                    ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk2.gif")),
                    ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk3.gif"))
            ));

    Entity entity = new Entity(250, 0, 32, 64, sprites.get("stand"));
    GameElement play = new GameElement(50, 300, 50, 50, new Sprite(
            ImageIO.read(new File("src/main/resources/sprites/play.png"))
    ));
    GameElement pause = new GameElement(375, 300, 50, 50, new Sprite(
            ImageIO.read(new File("src/main/resources/sprites/pause.png"))
    ));
    GameElement resume = new GameElement(125, 300, 50, 50, new Sprite(
            ImageIO.read(new File("src/main/resources/sprites/resume.png"))
    ));
    GameElement stop = new GameElement(450, 300, 50, 50, new Sprite(
            ImageIO.read(new File("src/main/resources/sprites/stop.png"))
    ));
    GameElement loop = new GameElement(200, 300, 50, 50, new Sprite(
            ImageIO.read(new File("src/main/resources/sprites/loop.png"))
    ));

    Group group = new Group(
            play,
            pause,
            resume,
            stop,
            loop,
            new GameElement(250, 490, 500, 100, new Sprite(Color.gray))
    );

    Group collectables = new Group(
            new GameElement(50, 400, 10, 10, new Sprite(Color.YELLOW)),
            new GameElement(100, 400, 10, 10, new Sprite(Color.YELLOW)),
            new GameElement(150, 400, 10, 10, new Sprite(Color.YELLOW)),
            new GameElement(200, 300, 10, 10, new Sprite(Color.YELLOW))
    );

    Text songName = new Text("Queen - Under Pressure", 0, 0).setForeground(Color.white);

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
                entity.setSprite(sprites.get("walk"));
                sprites.setHorizontalDirection(SpriteHorizontalDirection.LEFT);
            } else if (keyRight()) {
                entity.setVelocityX(2);
                entity.setSprite(sprites.get("walk"));
                sprites.setHorizontalDirection(SpriteHorizontalDirection.RIGHT);
            } else {
                entity.setVelocityX(0);
                entity.setSprite(sprites.get("stand"));
            }

            if (!salto) {
                entity.setSprite(sprites.get("jump"));
            }

            if (salto && keyUp()) {
                entity.setVelocityY(-4);
                salto = false;
            }

            if (entity.collide(play)) audio.play();
            else if (entity.collide(pause)) audio.pause();
            else if (entity.collide(resume)) audio.resume();
            else if (entity.collide(stop)) audio.stop();
            else if (entity.collide(loop)) {
                audio.loop();
                loop.delete();
            }

            if (entity.collide(group)) {
                salto = true;
            }

            if (entity.overlap(collectables)) {
                GameElement collectable = entity.getOverlapElement(collectables);
                if (collectable != null) collectable.delete();
            }

            draw(entity, collectables, group);
            drawText(songName);
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