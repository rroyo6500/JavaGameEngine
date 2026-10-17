package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifies keyboard press, release, helper, and reset behavior. */
class KeyboardTest {

    /** Clears the global keyboard state so tests remain independent. */
    @AfterEach
    void clearGlobalKeyboardState() {
        Keyboard.clear();
    }

    /** Confirms key codes transition between pressed and released states. */
    @Test
    void tracksPressedAndReleasedKeyCodes() {
        Keyboard.press(KeyEvent.VK_SPACE);
        assertTrue(Keyboard.key(KeyEvent.VK_SPACE));

        Keyboard.release(KeyEvent.VK_SPACE);
        assertFalse(Keyboard.key(KeyEvent.VK_SPACE));
    }

    /** Confirms character and directional helpers map to their expected key codes. */
    @Test
    void characterAndDirectionalHelpersResolveToExpectedKeys() {
        Keyboard.press(KeyEvent.VK_A);
        Keyboard.press(KeyEvent.VK_LEFT);

        assertTrue(Keyboard.key('A'));
        assertTrue(Keyboard.keyLeft());
        assertFalse(Keyboard.keyRight());
    }

    /** Confirms clearing keyboard state releases all tracked keys. */
    @Test
    void clearingStateReleasesEveryTrackedKey() {
        Keyboard.press(KeyEvent.VK_A);
        Keyboard.press(KeyEvent.VK_B);

        Keyboard.clear();

        assertFalse(Keyboard.key(KeyEvent.VK_A));
        assertFalse(Keyboard.key(KeyEvent.VK_B));
    }
}
