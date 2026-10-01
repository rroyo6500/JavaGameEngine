import rroyo.jgameengine.GameFrame;
import rroyo.jgameengine.enums.Direction;
import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Collider;
import rroyo.jgameengine.objects.gameutils.Point;
import rroyo.jgameengine.objects.gameutils.Sprite;

import javax.swing.*;
import java.awt.*;
import java.util.List;

void main() {

    GameObject go = new GameObject(new Point(0, 200), new Sprite(new Dimension(100, 200), Color.RED));

    GameFrame gf = new GameFrame(30, new Dimension(400, 400)) {
        @Override
        protected void code(GameFrame self) {

            go.setVelocityX(1);

            draw(go);

        }

        @Override
        public void canvas(Graphics g) {

            List<Collider> colliders = go.getColliders();

            g.setColor(Color.GREEN);
            g.fillPolygon(colliders.get(0).getPolygon());
            g.setColor(Color.CYAN);
            g.fillPolygon(colliders.get(1).getPolygon());
            g.setColor(Color.MAGENTA);
            g.fillPolygon(colliders.get(2).getPolygon());
            g.setColor(Color.YELLOW);
            g.fillPolygon(colliders.get(3).getPolygon());

        }
    };

    gf.start();

}