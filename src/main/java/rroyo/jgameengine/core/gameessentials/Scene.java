package rroyo.jgameengine.core.gameessentials;

import rroyo.jgameengine.core.gameobjects.GameElement;
import rroyo.jgameengine.core.gameobjects.GameObject;
import rroyo.jgameengine.core.gameobjects.Group;
import rroyo.jgameengine.core.gameobjects.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    private final List<GameElement> gameElements = new ArrayList<>();
    private final List<Text> texts = new ArrayList<>();

    private Render2D renderer;

    public Scene() {}

    public void  setRenderer(Render2D renderer) {
        this.renderer = renderer;
    }

    public abstract void update();

    public final void processFrame() {
        update();

        for (GameElement ge : gameElements) {
            if (!ge.isDeleted()) {
                ge.move();
            }
        }

        gameElements.removeIf(GameObject::isDeleted);
        texts.removeIf(Text::isDeleted);

        if (renderer != null) {
            renderer.prepareFrame(gameElements, texts);
            renderer.repaint();
        }
    }

    public final void add(GameObject... gameObjects) {
        for (GameObject go : gameObjects) {
            if (go instanceof  GameElement ge) {
                if (!gameElements.contains(ge)) gameElements.add(ge);
            } else if (go instanceof Group group) {
                for (GameElement ge : group.getMembers()) {
                    if (!gameElements.contains(ge)) gameElements.add(ge);
                }
            }
        }
    }

    public final void addText(Text... newTexts) {
        for (Text text : newTexts) {
            if (!texts.contains(text)) texts.add(text);
        }
    }

    public void clear() {
        gameElements.clear();
        texts.clear();
    }

}
