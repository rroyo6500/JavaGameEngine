package rroyo.jge.core.gameessentials;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {
    @Test
    void rejectsNonPositiveTargetFps() {
        assertThrows(IllegalArgumentException.class, () -> new GameLoop(0, null, null));
        assertThrows(IllegalArgumentException.class, () -> new GameLoop(-1, null, null));
    }

    @Test
    @Timeout(value = 4, unit = TimeUnit.SECONDS)
    void stopWaitsForAnInProgressCallbackAndThenReturns() throws InterruptedException {
        CountDownLatch callbackEntered = new CountDownLatch(1);
        CountDownLatch releaseCallback = new CountDownLatch(1);
        CountDownLatch stopReturned = new CountDownLatch(1);
        AtomicReference<Thread> loopThread = new AtomicReference<>();

        GameLoop loop = new GameLoop(60, () -> {
            loopThread.compareAndSet(null, Thread.currentThread());
            callbackEntered.countDown();
            try {
                releaseCallback.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, null);

        Thread stopper = null;
        try {
            loop.start();
            assertTrue(callbackEntered.await(2, TimeUnit.SECONDS), "The loop should enter its update callback");

            stopper = new Thread(() -> {
                loop.stop();
                stopReturned.countDown();
            }, "GameLoopTest-stopper");
            stopper.start();

            assertFalse(stopReturned.await(150, TimeUnit.MILLISECONDS),
                    "stop() is documented to wait while the update callback is still running");
        } finally {
            releaseCallback.countDown();
            loop.stop();
            if (stopper != null) {
                stopper.join(1000);
            }
            Thread runningThread = loopThread.get();
            if (runningThread != null) {
                runningThread.join(1000);
            }
        }

        assertEquals(0, stopReturned.getCount(), "stop() should return after the game loop thread exits");
        assertNotNull(loopThread.get());
        assertFalse(loopThread.get().isAlive(), "The loop thread must have terminated");
    }
}
