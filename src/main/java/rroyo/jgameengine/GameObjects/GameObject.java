package rroyo.jgameengine.GameObjects;

import lombok.Getter;
import rroyo.jgameengine.Gameutils.Sprite;

import java.awt.*;
import java.awt.geom.Point2D;

@Getter
public abstract class GameObject {

    protected final Point point;
    protected final Sprite sprite;

    public GameObject(Point point, Sprite sprite) {
        this.point = point;
        this.sprite = sprite;
    }
}
