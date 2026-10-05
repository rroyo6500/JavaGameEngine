package rroyo.jgameengine.core.gameutils;

public class Overlap {
    private double overlapX;
    private double overlapY;

    public Overlap(double overlapX, double overlapY) {
        this.overlapX = overlapX;
        this.overlapY = overlapY;
    }

    public double getOverlapX() {
        return overlapX;
    }

    public double getOverlapY() {
        return overlapY;
    }

    public void setOverlap(double overlapX, double overlapY) {
        this.overlapX = overlapX;
        this.overlapY = overlapY;
    }

}
