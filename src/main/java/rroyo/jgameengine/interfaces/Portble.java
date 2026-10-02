package rroyo.jgameengine.interfaces;

import rroyo.jgameengine.objects.gameobjects.GameElement;

import java.lang.instrument.IllegalClassFormatException;

public interface Portble {

    default void setVelocity(double velocityX, double velocityY) throws IllegalClassFormatException {
        if (this instanceof GameElement self) {
            try {
                self.getVelocity().setVelocityX(velocityX);
                self.getVelocity().setVelocityY(velocityY);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            self.move();
        } else
            throw new IllegalClassFormatException("Required an instance of 'GameObject'");
    }

    default void setVelocityX (double velocityX) throws IllegalClassFormatException {
        if (this instanceof GameElement self) {
            try {
                self.getVelocity().setVelocityX(velocityX);
                self.move();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else
            throw new IllegalClassFormatException("Required an instance of 'GameObject'");
    }

    default void setVelocityY (double velocityY) throws IllegalClassFormatException {
        if (this instanceof GameElement self) {
            try {
                self.getVelocity().setVelocityY(velocityY);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            self.move();
        } else
            throw new IllegalClassFormatException("Required an instance of 'GameObject'");
    }

    default double getVelocityX() throws IllegalClassFormatException {
        if (this instanceof GameElement self) {
            return self.getVelocity().getVelocityX();
        } else
            throw new IllegalClassFormatException("Required an instance of 'GameObject'");
    }

    default double getVelocityY() throws IllegalClassFormatException {
        if (this instanceof GameElement self) {
            return self.getVelocity().getVelocityY();
        } else
            throw new IllegalClassFormatException("Required an instance of 'GameObject'");
    }

}
