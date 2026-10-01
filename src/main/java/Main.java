import rroyo.jgameengine.GameFrame;
import rroyo.jgameengine.objects.gameobjects.GameObject;
import rroyo.jgameengine.objects.gameutils.Point;
import rroyo.jgameengine.objects.gameutils.Sprite;

import javax.swing.*;
import java.awt.*;

void main() {

    JFrame frame = new JFrame();
    frame.setSize(400, 400);

    JPanel panel = new JPanel();
    panel.setBounds(0, 0, 400, 400);
    frame.add(panel);

    GameObject go = new GameObject(new Point(), new Sprite(new Dimension(), Color.CYAN));

    GameFrame gf = new GameFrame(panel, 15) {
        @Override
        protected void code() {
            go.setVelocity(0.5, 0);
            draw(go);
        }
    };

    frame.setVisible(true);
}