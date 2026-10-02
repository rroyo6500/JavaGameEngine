package rroyo.jgameengine.objects.gameutils;

import rroyo.jgameengine.enums.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Collider {

    private final Polygon polygon;
    private final Point center;
    private final Direction direction;

    public Collider(Rectangle rectangle, Direction direction) {
        this(new Point(rectangle.getX(), rectangle.getY()), rectangle.getSize(), direction);
    }

    public Collider(Point point, Dimension dimension, Direction direction) {
        this.direction = direction;
        this.polygon = createPolygon(point, dimension, direction);
        this.center = new Point(polygon.getBounds().getCenterX(), polygon.getBounds().getCenterY());
    }

    public boolean intersects(Collider collider) {
        List<Point> selfPoints = getPointList(this);
        List<Point> otherPoints = getPointList(collider);

        for (Point p : selfPoints) {
            if (collider.getPolygon().contains(p.getX(), p.getY())) {
                return true;
            }
        }
        for (Point p : otherPoints) {
            if (this.getPolygon().contains(p.getX(), p.getY())) {
                return true;
            }
        }

        return false;
    }

    private List<Point> getPointList(Collider collider) {
        List<Point> pointList = new ArrayList<>();
        pointList.add(collider.getCenter());
        for (int i = 0; i < collider.getPolygon().npoints; i++) {
            pointList.add(new Point(
                    getPolygon().xpoints[i],
                    getPolygon().ypoints[i]
            ));
        }
        return pointList;
    }

    public void move(int deltaX, int deltaY) {
        polygon.translate(deltaX, deltaY);
        center.setPoint(
                center.getX() + deltaX,
                center.getY() + deltaY
        );
    }

    public static Polygon createPolygon(Point point, Dimension dimension, Direction direction) {
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

    public Point getCenter() {
        return center;
    }
}
