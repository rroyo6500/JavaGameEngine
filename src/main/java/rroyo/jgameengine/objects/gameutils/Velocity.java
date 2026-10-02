package rroyo.jgameengine.objects.gameutils;

import rroyo.jgameengine.interfaces.Portble;

public class Velocity {

    private double velocityX = 0, velocityY = 0;

    public Velocity() {}

    public double getVelocityX(){
        return velocityX;
    }

    public void setVelocityX(double velocityX) throws IllegalAccessException {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) throws IllegalAccessException {
        this.velocityY = velocityY;
    }
}
