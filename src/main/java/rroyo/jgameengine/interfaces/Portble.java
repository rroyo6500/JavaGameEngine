package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.objects.gameobjects.GameObject;

public interface Portble {

    default void setVelocity(double velocityX, double velocityY) {
        if (this instanceof GameObject) {
            ((GameObject) this).getVelocity().setVelocityX(velocityX);
            ((GameObject) this).getVelocity().setVelocityY(velocityY);

            ((GameObject) this).move();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default void setVelocityX (double velocityX) {
        if (this instanceof GameObject) {
            ((GameObject) this).getVelocity().setVelocityX(velocityX);

            ((GameObject) this).move();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default void setVelocityY (double velocityY) {
        if (this instanceof GameObject) {
            ((GameObject) this).getVelocity().setVelocityY(velocityY);

            ((GameObject) this).move();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default double getVelocityX() {
        if (this instanceof GameObject) {
            return ((GameObject) this).getVelocity().getVelocityX();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default double getVelocityY() {
        if (this instanceof GameObject) {
            return ((GameObject) this).getVelocity().getVelocityY();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

}
