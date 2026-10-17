package rroyo.jge.core.gameutils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Keyboard} class is a global utility that tracks the current state of keyboard keys.
 * It provides a static listener that interfaces with Java AWT events and allows for polling
 * key states from anywhere in the game logic without needing direct event callbacks.
 */
public final class Keyboard {

    /** Current pressed state keyed by AWT key code. */
    private static final Map<Integer, Boolean> keys = new HashMap<>();

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
        keys.put(keyCode, true);
    }

    /**
     * Marks a specific key code as released.
     *
     * @param keyCode The integer key code to release.
     */
    public static void release(int keyCode) {
        keys.put(keyCode, false);
    }

    /**
     * Checks if a specific key is currently being held down.
     *
     * @param keyCode The integer key code to check (e.g., {@link KeyEvent#VK_SPACE}).
     * @return {@code true} if the key is pressed; {@code false} otherwise.
     */
    public static boolean key(int keyCode) {
        return keys.getOrDefault(keyCode, false);
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
     * Marks every key as released, typically when the game window loses focus so a missed
     * release event cannot leave a key stuck in the pressed state.
     */
    public static void clear() {
        keys.clear();
    }

    /**
     * A global static {@link KeyAdapter} meant to be attached to the main window.
     * It automatically populates the internal key state array whenever AWT registers a key event.
     * AWT listener that updates the shared key-state table from press and release events. */
    public static final KeyAdapter listener = new KeyAdapter() {
        /** Records a key press in the shared keyboard state. */
        @Override
        public void keyPressed(KeyEvent e) {
            press(e.getKeyCode());
        }

        /** Records a key release in the shared keyboard state. */
        @Override
        public void keyReleased(KeyEvent e) {
            release(e.getKeyCode());
        }
    };

}
