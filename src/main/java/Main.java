import rroyo.jgameengine.GameFrame;
import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Dimension;
import rroyo.jgameengine.objects.gameutils.Sprite;

import java.awt.*;

void main() {

    GameObject rojo = new GameObject(50, 0, new Sprite(new Dimension(100, 100), Color.RED));
    GameObject azul = new GameObject(250, 400, new Sprite(new Dimension(500, 25), Color.CYAN));

    GameFrame gf = new GameFrame(30, new Dimension(500, 500)) {
        @Override
        protected void code(GameFrame self) {

            if (!rojo.collide(azul)) {
                rojo.setVelocityY(1);
            } else {
                rojo.setVelocityX(1);
            }

            draw(rojo, azul);
        }

        @Override
        protected void canvas(Graphics g) {
            super.canvas(g);



        }
    };

    gf.start();

}