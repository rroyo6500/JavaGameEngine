import rroyo.jgameengine.GameFrame;
import rroyo.jgameengine.enums.Direction;
import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Collider;
import rroyo.jgameengine.objects.gameutils.Point;
import rroyo.jgameengine.objects.gameutils.Sprite;

import java.awt.*;

void main() {

    GameObject go = new GameObject(new Point(0, 50), new Sprite(new Dimension(100, 100), Color.RED));
    GameObject go2 = new GameObject(new Point(300, 300), new Sprite(new Dimension(100, 100), Color.RED));

    GameFrame gf = new GameFrame(30, new Dimension(400, 400)) {
        @Override
        protected void code(GameFrame self) {

            go.setVelocity(1, 1);

            go.collide(go2);

            draw(go, go2);

        }

        @Override
        protected void canvas(Graphics g) {
            super.canvas(g);



        }
    };

    gf.start();

}