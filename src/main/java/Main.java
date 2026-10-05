import rroyo.jgameengine.core.assets.AssetGroup;
import rroyo.jgameengine.core.assets.Audio;
import rroyo.jgameengine.core.assets.Sprite;
import rroyo.jgameengine.core.gameessentials.GameWindow;
import rroyo.jgameengine.core.gameessentials.Scene;
import rroyo.jgameengine.core.gameobjects.Dimension;
import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.Group;
import rroyo.jgameengine.core.gameobjects.Point;
import rroyo.jgameengine.core.gameutils.Camera;
import rroyo.jgameengine.core.gameutils.Keyboard;
import rroyo.jgameengine.core.gameutils.Time;
import rroyo.jgameengine.enums.SpriteHorizontalDirection;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;

void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

    AssetGroup assetGroup = new AssetGroup()
            .add("spriteStatic",
                    new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario.gif"))))
            .add("spriteWalk",
                    new Sprite(0.1f,
                            ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk1.gif")),
                            ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk2.gif")),
                            ImageIO.read(new File("src/main/resources/sprites/Super Mario - Walk3.gif"))
                    ))
            .add("spriteJump",
                    new Sprite(ImageIO.read(new File("src/main/resources/sprites/Super Mario - Jump.gif"))))
            .add("Queen-UnderPresure",
                    new Audio(new File("src/main/resources/audio/Queen - Under Pressure.wav")));


    GameElement floor = new GameElement(0, 255, 500, 10, new Sprite(Color.DARK_GRAY));
    Group platforms = new Group(
            new GameElement(-255, 0, 10, 500, new Sprite(Color.DARK_GRAY)),
            new GameElement(255, 0, 10, 500, new Sprite(Color.DARK_GRAY)),
            floor
    );

    Entity entity = new Entity(0, 150, 32, 64, (Sprite) assetGroup.get("spriteStatic"));

    Scene scene = new Scene() {

        private boolean jump = false;

        @Override
        public void update() {

            if (Keyboard.keyUp()) Camera.moveY(5);
            if (Keyboard.keyDown()) Camera.moveY(-5);
            if (Keyboard.keyLeft()) Camera.moveX(5);
            if (Keyboard.keyRight()) Camera.moveX(-5);
            if (Keyboard.key('0')) Camera.setZoom(Camera.getZoom() + 0.01);
            else if (Keyboard.key('9')) Camera.setZoom(Camera.getZoom() - 0.01);

            entity.setVelocityY(
                    entity.getVelocityY() + (800 * Time.deltaTime())
            );

            entity.setSprite((Sprite) assetGroup.get("spriteStatic"));
            entity.setVelocityX(0);
            if (Keyboard.key('d') || Keyboard.key('a')) {
                if (Keyboard.key('d')) {
                    entity.setVelocityX(125);
                    assetGroup.setHorizontalDirection(SpriteHorizontalDirection.RIGHT);
                }
                if (Keyboard.key('a')) {
                    entity.setVelocityX(-125);
                    assetGroup.setHorizontalDirection(SpriteHorizontalDirection.LEFT);
                }
                entity.setSprite((Sprite) assetGroup.get("spriteWalk"));
            }
            if (Keyboard.key('w') && jump) {
                jump = false;
                entity.setVelocityY(-300);
            }
            if (!jump) entity.setSprite((Sprite) assetGroup.get("spriteJump"));

            if (Keyboard.key('l')) ((Audio) assetGroup.get("Queen-UnderPresure")).loop();
            else if (Keyboard.key('1')) ((Audio) assetGroup.get("Queen-UnderPresure")).resume();
            else if (Keyboard.key('2')) ((Audio) assetGroup.get("Queen-UnderPresure")).pause();

            if (entity.collide(floor)) {
                entity.setVelocityY(0);
                jump = true;
            }

            entity.collide(platforms);

            add(entity, platforms);
        }
    };

    GameWindow window = new GameWindow(60, new Dimension(500, 500), scene);

}

private class Entity extends GameElement {

    public Entity(double x, double y, double width, double height, Sprite sprite) {
        super(x, y, width, height, sprite);
    }

    public Entity(Point point, Dimension dimension, Sprite sprite) {
        super(point, dimension, sprite);
    }

    @Override
    protected double transformVelocity(double velocity) {
        return velocity * Time.deltaTime();
    }

}