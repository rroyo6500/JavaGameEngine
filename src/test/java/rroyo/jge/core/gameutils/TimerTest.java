package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.testing.TestState;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies timer validation, callback execution, cancellation, and looping. */
class TimerTest {
    /** Resets shared timer and time state before each test. */
    @BeforeEach
    void setUp() {
        TestState.resetTimers();
        TestState.resetTime();
    }

    /** Resets shared timer and time state after each test. */
    @AfterEach
    void tearDown() {
        TestState.resetTimers();
        TestState.resetTime();
    }

    /** Confirms zero and negative durations are rejected. */
    @Test
    void rejectsZeroAndNegativeDurations() {
        assertThrows(IllegalArgumentException.class, () -> Timer.schedule(0, () -> { }));
        assertThrows(IllegalArgumentException.class, () -> Timer.schedule(-1, () -> { }));
    }

    /** Confirms an expired one-shot timer runs once and reports completion. */
    @Test
    void oneShotTimerIsFinishedAfterItsCallbackRuns() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(0.1f, calls::incrementAndGet);

        timer.update(0.2f);

        assertEquals(1, calls.get());
        assertTrue(timer.isFinished(), "An expired non-looping timer should report finished");
    }

    /** Confirms a one-shot callback is not repeated by later updates. */
    @Test
    void oneShotCallbackDoesNotRunAgainOnLaterUpdates() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(0.1f, calls::incrementAndGet);

        timer.update(0.2f);
        timer.update(0.2f);

        assertEquals(1, calls.get(), "A one-shot callback must run only once after expiration");
    }

    /** Confirms cancellation completes a timer without invoking its callback. */
    @Test
    void cancelledTimerIsImmediatelyFinishedAndNeverCallsBack() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(10f, calls::incrementAndGet);

        timer.cancel();
        timer.update(20f);

        assertTrue(timer.isFinished(), "Cancellation should count as completion for registry cleanup");
        assertEquals(0, calls.get());
    }

    /** Confirms a looping timer remains active after invoking its callback. */
    @Test
    void loopingTimerIsNotFinishedAfterItFires() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(0.1f, calls::incrementAndGet, true);

        timer.update(0.1f);

        assertEquals(1, calls.get());
        assertFalse(timer.isFinished());
    }
}
