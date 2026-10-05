package rroyo.jgameengine.core.gameutils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class Keyboard {

    private static final boolean[] keys = new boolean[1024];

    private Keyboard() {}

    public static void press(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    public static void release(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }

    public static boolean key(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    public static boolean key(char keyChar) {
        return key(KeyEvent.getExtendedKeyCodeForChar(keyChar));
    }

    public static boolean keyUp() {
        return key(KeyEvent.VK_UP);
    }

    public static boolean keyDown() {
        return key(KeyEvent.VK_DOWN);
    }

    public static boolean keyLeft() {
        return key(KeyEvent.VK_LEFT);
    }

    public static boolean keyRight() {
        return key(KeyEvent.VK_RIGHT);
    }

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
