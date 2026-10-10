package rroyo.jge.core.gameobjects;

/**
 * The {@code Velocity} class is a data component that encapsulates the speed and direction
 * of a game element moving through a 2D space on its horizontal and vertical axes.
 */
public class Velocity {

    /**
     * The speed vector applied to the horizontal (X) axis.
     */
    private double velocityX = 0;

    /**
     * The speed vector applied to the vertical (Y) axis.
     */
    private double velocityY = 0;

    /**
     * Constructs a new {@code Velocity} instance initialized to zero (no movement).
     */
    public Velocity() {}

    /**
     * Retrieves the current velocity applied to the X-axis.
     *
     * @return The horizontal velocity.
     */
    public double getVelocityX(){
        return velocityX;
    }

    /**
     * Sets the current velocity applied to the X-axis.
     *
     * @param velocityX The new horizontal velocity.
     * @throws IllegalAccessException if constraints or internal architecture prevent this assignment.
     */
    public void setVelocityX(double velocityX) throws IllegalAccessException {
        this.velocityX = velocityX;
    }

    /**
     * Retrieves the current velocity applied to the Y-axis.
     *
     * @return The vertical velocity.
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Sets the current velocity applied to the Y-axis.
     *
     * @param velocityY The new vertical velocity.
     * @throws IllegalAccessException if constraints or internal architecture prevent this assignment.
     */
    public void setVelocityY(double velocityY) throws IllegalAccessException {
        this.velocityY = velocityY;
    }
}
