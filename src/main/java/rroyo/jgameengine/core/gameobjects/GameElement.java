package rroyo.jgameengine.core.gameobjects;

import rroyo.jgameengine.core.assets.Sprite;

import java.awt.*;

public class GameElement extends GameObject {

    private final Rectangle bounds = new Rectangle();
    private final Overlap overlap = new Overlap(0, 0);

    protected Velocity velocity = new Velocity();

    protected Point point;
    protected Dimension dimension;
    protected Sprite sprite;

    public GameElement(double x, double y, double width, double height, Sprite sprite) {
        this(new Point(x, y), new Dimension(width, height), sprite);
    }

    public GameElement(Point point, Dimension dimension, Sprite sprite) {
        this.point = point;
        this.dimension = dimension;
        this.sprite = sprite;
    }

    protected final Overlap calculateOverlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return null;
        if (!this.inRange(go)) {
            return null;
        }

        // X
        double deltaX = Math.abs((int) this.getPoint().getX() - (int) go.getPoint().getX());
        double limitX = this.getDimension().getHalfWidth() + go.getDimension().getHalfWidth();
        double overlapX = limitX - deltaX;

        // Y
        double deltaY = Math.abs((int) this.getPoint().getY() - (int) go.getPoint().getY());
        double limitY = this.getDimension().getHalfHeight() + go.getDimension().getHalfHeight();
        double overlapY = limitY - deltaY;

        overlap.setOverlap(overlapX, overlapY);

        return overlap;
    }

    public final boolean overlap(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
        Overlap overlap = calculateOverlap(go);
        if (overlap == null) return false;
        return overlap.getOverlapX() > 0 && overlap.getOverlapY() > 0;
    }

    public final boolean overlap(Group group) {
        if (isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return true;
            }
        }
        return false;
    }

    public final Overlap getOverlap(GameElement go) {
        return overlap;
    }

    public final GameElement getOverlapElement(Group group) {
        if (isDeleted()) return null;
        for (GameElement go : group.getMembers()) {
            if (this.overlap(go)) {
                return go;
            }
        }
        return null;
    }

    public final boolean inRange(GameElement go) {
        if (isDeleted() || go.isDeleted()) return false;
        double secureRange = (this.getDimension().getWidth() + this.getDimension().getHeight()) +
                (go.getDimension().getWidth() + go.getDimension().getHeight());

        double deltaX = go.getPoint().getX() - this.getPoint().getX();
        double deltaY = go.getPoint().getY() - this.getPoint().getY();

        if (Math.abs(deltaX) > secureRange || Math.abs(deltaY) > secureRange) {
            return false;
        }

        double distance = (deltaX * deltaX) + (deltaY * deltaY);
        return distance <= (secureRange * secureRange);
    }

    public final boolean inRange(Group group) {
        if (isDeleted() || group.isDeleted()) return false;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return true;
            }
        }
        return false;
    }

    public final GameElement getInRangeElement(Group group) {
        if (isDeleted() || group.isDeleted()) return null;
        for (GameElement go : group.getMembers()) {
            if (this.inRange(go)) {
                return go;
            }
        }
        return null;
    }

    public boolean collide(GameElement gameElement) {
        if (this.overlap(gameElement)) {
            Overlap overlap = this.getOverlap(gameElement);
            if (overlap == null) return false;
            if (overlap.getOverlapX() < overlap.getOverlapY()) {
                if (this.getPoint().getX() < gameElement.getPoint().getX())
                    this.move(-overlap.getOverlapX(), 0);
                else
                    this.move(overlap.getOverlapX(), 0);
                this.setVelocityX(0);
            } else {
                if (this.getPoint().getY() < gameElement.getPoint().getY())
                    this.move(0, -overlap.getOverlapY());
                else
                    this.move(0, overlap.getOverlapY());
                this.setVelocityY(0);
            }
            return true;
        }
        return false;
    }

    public boolean collide(Group group) {
        for (GameElement go : group.getMembers()) {
            if (this.collide(go)) {
                return true;
            }
        }
        return false;
    }

    protected double transformVelocity(double velocity) {
        return velocity;
    }

    public final void move() {
        move(
                transformVelocity(velocity.getVelocityX()),
                transformVelocity(velocity.getVelocityY())
        );
    }

    public final void move(double dx, double dy) {
        if (isDeleted()) return ;
        point.setPoint(
                point.getX() + dx,
                point.getY() + dy
        );
    }

    public final void setVelocity(double velocityX, double velocityY) {
        try {
            this.getVelocity().setVelocityX(velocityX);
            this.getVelocity().setVelocityY(velocityY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final void setVelocityX (double velocityX) {
        try {
            this.getVelocity().setVelocityX(velocityX);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final void setVelocityY (double velocityY) {
        try {
            this.getVelocity().setVelocityY(velocityY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final double getVelocityX() {
        return this.getVelocity().getVelocityX();
    }

    public final double getVelocityY() {
        return this.getVelocity().getVelocityY();
    }

    public final Point getPoint() {
        return point;
    }

    public final Sprite getSprite() {
        return sprite;
    }

    public final Rectangle getBounds() {
        bounds.setBounds(
                (int) (point.getX() - this.getDimension().getHalfWidth()),
                (int) (point.getY() - this.getDimension().getHalfHeight()),
                (int) this.getDimension().getWidth(), (int) this.getDimension().getHeight()
        );
        return bounds;
    }

    public final Dimension getDimension() {
        return dimension;
    }

    public final void setSprite(Sprite sprite) {
        if (sprite == null || sprite.isClosed()) return;
        if (isDeleted()) return ;
        this.sprite = sprite;
    }

    public final Velocity getVelocity() {
        return velocity;
    }

}
