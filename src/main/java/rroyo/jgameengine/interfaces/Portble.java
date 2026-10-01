package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.objects.gameobjects.GameObject;

public interface Portble {

    default void setVelocity(double velocityX, double velocityY) {
        if (this instanceof GameObject self) {
            self.getVelocity().setVelocityX(velocityX);
            self.getVelocity().setVelocityY(velocityY);

            self.move();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default void setVelocityX (double velocityX) {
        if (this instanceof GameObject self) {
            self.getVelocity().setVelocityX(velocityX);
            self.move();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default void setVelocityY (double velocityY) {
        if (this instanceof GameObject self) {
            self.getVelocity().setVelocityY(velocityY);

            self.move();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default double getVelocityX() {
        if (this instanceof GameObject self) {
            return self.getVelocity().getVelocityX();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

    default double getVelocityY() {
        if (this instanceof GameObject self) {
            return self.getVelocity().getVelocityY();
        } else
            throw new RuntimeException("Required an instance of 'GameObject'");
    }

}
