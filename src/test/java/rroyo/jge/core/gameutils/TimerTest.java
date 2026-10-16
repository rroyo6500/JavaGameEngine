package rroyo.jge.core.gameutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rroyo.jge.testing.TestState;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {
    @BeforeEach
    void setUp() {
        TestState.resetTimers();
        TestState.resetTime();
    }

    @AfterEach
    void tearDown() {
        TestState.resetTimers();
        TestState.resetTime();
    }

    @Test
    void rejectsZeroAndNegativeDurations() {
        assertThrows(IllegalArgumentException.class, () -> Timer.schedule(0, () -> { }));
        assertThrows(IllegalArgumentException.class, () -> Timer.schedule(-1, () -> { }));
    }

    @Test
    void oneShotTimerIsFinishedAfterItsCallbackRuns() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(0.1f, calls::incrementAndGet);

        timer.update(0.2f);

        assertEquals(1, calls.get());
        assertTrue(timer.isFinished(), "An expired non-looping timer should report finished");
    }

    @Test
    void oneShotCallbackDoesNotRunAgainOnLaterUpdates() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(0.1f, calls::incrementAndGet);

        timer.update(0.2f);
        timer.update(0.2f);

        assertEquals(1, calls.get(), "A one-shot callback must run only once after expiration");
    }

    @Test
    void cancelledTimerIsImmediatelyFinishedAndNeverCallsBack() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(10f, calls::incrementAndGet);

        timer.cancel();
        timer.update(20f);

        assertTrue(timer.isFinished(), "Cancellation should count as completion for registry cleanup");
        assertEquals(0, calls.get());
    }

    @Test
    void loopingTimerIsNotFinishedAfterItFires() {
        AtomicInteger calls = new AtomicInteger();
        Timer timer = Timer.schedule(0.1f, calls::incrementAndGet, true);

        timer.update(0.1f);

        assertEquals(1, calls.get());
        assertFalse(timer.isFinished());
    }
}
