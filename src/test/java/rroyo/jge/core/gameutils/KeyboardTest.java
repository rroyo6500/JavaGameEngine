package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardTest {

    @AfterEach
    void clearGlobalKeyboardState() {
        Keyboard.clear();
    }

    @Test
    void tracksPressedAndReleasedKeyCodes() {
        Keyboard.press(KeyEvent.VK_SPACE);
        assertTrue(Keyboard.key(KeyEvent.VK_SPACE));

        Keyboard.release(KeyEvent.VK_SPACE);
        assertFalse(Keyboard.key(KeyEvent.VK_SPACE));
    }

    @Test
    void characterAndDirectionalHelpersResolveToExpectedKeys() {
        Keyboard.press(KeyEvent.VK_A);
        Keyboard.press(KeyEvent.VK_LEFT);

        assertTrue(Keyboard.key('A'));
        assertTrue(Keyboard.keyLeft());
        assertFalse(Keyboard.keyRight());
    }

    @Test
    void clearingStateReleasesEveryTrackedKey() {
        Keyboard.press(KeyEvent.VK_A);
        Keyboard.press(KeyEvent.VK_B);

        Keyboard.clear();

        assertFalse(Keyboard.key(KeyEvent.VK_A));
        assertFalse(Keyboard.key(KeyEvent.VK_B));
    }
}
