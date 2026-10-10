package rroyo.jge.core.gameutils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The {@code Keyboard} class is a global utility that tracks the current state of keyboard keys.
 * It provides a static listener that interfaces with Java AWT events and allows for polling
 * key states from anywhere in the game logic without needing direct event callbacks.
 */
public final class Keyboard {

    /**
     * A boolean array mapping key codes to their current pressed state.
     * Supports up to 1024 key codes.
     */
    private static final boolean[] keys = new boolean[1024];

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Keyboard() {}

    /**
     * Marks a specific key code as pressed.
     *
     * @param keyCode The integer key code to press.
     */
    public static void press(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    /**
     * Marks a specific key code as released.
     *
     * @param keyCode The integer key code to release.
     */
    public static void release(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }

    /**
     * Checks if a specific key is currently being held down.
     *
     * @param keyCode The integer key code to check (e.g., {@link KeyEvent#VK_SPACE}).
     * @return {@code true} if the key is pressed; {@code false} otherwise.
     */
    public static boolean key(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    /**
     * Checks if a specific character key is currently being held down.
     *
     * @param keyChar The character to check.
     * @return {@code true} if the corresponding key is pressed; {@code false} otherwise.
     */
    public static boolean key(char keyChar) {
        return key(KeyEvent.getExtendedKeyCodeForChar(keyChar));
    }

    /**
     * Convenience method to check if the UP arrow key is currently pressed.
     *
     * @return {@code true} if the UP arrow key is pressed; {@code false} otherwise.
     */
    public static boolean keyUp() {
        return key(KeyEvent.VK_UP);
    }

    /**
     * Convenience method to check if the DOWN arrow key is currently pressed.
     *
     * @return {@code true} if the DOWN arrow key is pressed; {@code false} otherwise.
     */
    public static boolean keyDown() {
        return key(KeyEvent.VK_DOWN);
    }

    /**
     * Convenience method to check if the LEFT arrow key is currently pressed.
     *
     * @return {@code true} if the LEFT arrow key is pressed; {@code false} otherwise.
     */
    public static boolean keyLeft() {
        return key(KeyEvent.VK_LEFT);
    }

    /**
     * Convenience method to check if the RIGHT arrow key is currently pressed.
     *
     * @return {@code true} if the RIGHT arrow key is pressed; {@code false} otherwise.
     */
    public static boolean keyRight() {
        return key(KeyEvent.VK_RIGHT);
    }

    /**
     * A global static {@link KeyAdapter} meant to be attached to the main window.
     * It automatically populates the internal key state array whenever AWT registers a key event.
     */
    public static final KeyAdapter listener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            press(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            release(e.getKeyCode());
        }
    };

}
