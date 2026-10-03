import rroyo.jgameengine.GameFrame;
import rroyo.jgameengine.core.assets.Audio;
import rroyo.jgameengine.core.assets.Sprite;
import rroyo.jgameengine.core.assets.AssetGroup;
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
import java.lang.instrument.IllegalClassFormatException;

void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

    AssetGroup assets = new AssetGroup()
            .add("stand",
                    new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario.gif"))))
            .add("jump",
                    new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario - Jump.gif"))))
            .add("walk", new Sprite(
                    0.1f,
                    ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk1.gif")),
                    ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk2.gif")),
                    ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk3.gif"))
            ))
            .add("queen-underPresure", new Audio(new File("src/main/resources/audio/Queen - Under Pressure.wav")));

    Entity entity = new Entity(250, 0, 32, 64, (Sprite) assets.get("stand"));
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

    GameFrame gf = new GameFrame(60, new Dimension(500, 500)) {

        boolean salto = false;

        @Override
        protected void code() throws IllegalClassFormatException {

            if (((entity.getPoint().getY() + entity.getDimension().getHalfHeight()) < getDimension().getHeight())) {
                entity.setVelocityY(
                        entity.getVelocityY() + (800 * Time.deltaTime())
                );
            } else {
                entity.getPoint().setY(getDimension().getHeight() - entity.getDimension().getHalfHeight());
                salto = true;
            }

            if ((keyLeft() || key("a")) || (keyRight() || key("d"))) {
                if (keyLeft() || key("a")) {
                    entity.setVelocityX(-125);
                    entity.setSprite((Sprite) assets.get("walk"));
                    assets.setHorizontalDirection(SpriteHorizontalDirection.LEFT);
                }
                if (keyRight() || key("d")) {
                    entity.setVelocityX(125);
                    entity.setSprite((Sprite) assets.get("walk"));
                    assets.setHorizontalDirection(SpriteHorizontalDirection.RIGHT);
                }
            } else {
                entity.setVelocityX(0);
                entity.setSprite((Sprite) assets.get("stand"));
            }

            if (!salto) {
                entity.setSprite((Sprite) assets.get("jump"));
            }

            if (salto && (keyUp() || key("w"))) {
                entity.setVelocityY(-300);
                salto = false;
            }

            if (entity.collide(play)) ((Audio) assets.get("queen-underPresure")).play();
            else if (entity.collide(pause)) ((Audio) assets.get("queen-underPresure")).pause();
            else if (entity.collide(resume)) ((Audio) assets.get("queen-underPresure")).resume();
            else if (entity.collide(stop)) assets.get("queen-underPresure").close();
            else if (entity.collide(loop)) {
                ((Audio) assets.get("queen-underPresure")).loop();
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