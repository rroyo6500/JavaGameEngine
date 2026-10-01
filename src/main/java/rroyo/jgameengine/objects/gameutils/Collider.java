package rroyo.jgameengine.objects.gameutils;

import rroyo.jgameengine.enums.Direction;

import java.awt.*;

public class Collider {

    private final Polygon polygon;
    private final Direction direction;

    public Collider(Rectangle rectangle, Direction direction) {
        this(new Point(rectangle.getX(), rectangle.getY()), rectangle.getSize(), direction);
    }

    public Collider(Point point, Dimension dimension, Direction direction) {
        this.direction = direction;
        this.polygon = getPolygon(point, dimension, direction);
    }

    public static Polygon getPolygon(Point point, Dimension dimension, Direction direction) {
        Polygon polygon = new Polygon();
        switch (direction) {
            case UP -> {
                polygon.addPoint(
                        (int) (point.getX() - (dimension.getWidth() / 2)),
                        (int) (point.getY() - (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (int) (point.getX() + (dimension.getWidth() / 2)),
                        (int) (point.getY() - (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() + ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() - ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() - ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() - ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
            }
            case DOWN -> {
                polygon.addPoint(
                        (int) (point.getX() + (dimension.getWidth() / 2)),
                        (int) (point.getY() + (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (int) (point.getX() - (dimension.getWidth() / 2)),
                        (int) (point.getY() + (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() - ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() + ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() + ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() + ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
            }
            case LEFT -> {
                polygon.addPoint(
                        (int) (point.getX() - (dimension.getWidth() / 2)),
                        (int) (point.getY() + (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (int) (point.getX() - (dimension.getWidth() / 2)),
                        (int) (point.getY() - (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() - ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() - ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() - ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() + ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
            }
            case RIGHT -> {
                polygon.addPoint(
                        (int) (point.getX() + (dimension.getWidth() / 2)),
                        (int) (point.getY() - (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (int) (point.getX() + (dimension.getWidth() / 2)),
                        (int) (point.getY() + (dimension.getHeight() / 2))
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() + ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() + ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
                polygon.addPoint(
                        (dimension.getWidth() >= dimension.getHeight())
                                ? (int) (point.getX() + ((dimension.getWidth() / 2) - (dimension.getHeight() / 2)))
                                : (int) point.getX(),
                        (dimension.getHeight() >= dimension.getWidth())
                                ? (int) (point.getY() - ((dimension.getHeight() / 2) - (dimension.getWidth() / 2)))
                                : (int) point.getY()
                );
            }
        }
        return polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Direction getDirection() {
        return direction;
    }
}
