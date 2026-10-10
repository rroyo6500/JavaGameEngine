package rroyo.jge.core.assets;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * The {@code Audio} class represents a sound asset in the game. It extends {@link Asset}
 * and encapsulates the functionality to load, play, pause, resume, stop, and loop audio files
 * using the Java Sound API (specifically, the {@link Clip} interface).
 */
public class Audio extends Asset {

    /**
     * The underlying {@link Clip} used to manage and play the audio data.
     */
    private Clip clip = AudioSystem.getClip();

    /**
     * Constructs a new {@code Audio} object by loading sound data from the specified file.
     *
     * @param audioFile The {@link File} containing the audio data to load.
     * @throws LineUnavailableException If a line cannot be opened because it is unavailable.
     * @throws UnsupportedAudioFileException If the audio file format is not supported.
     * @throws IOException If an input/output exception occurs while reading the file.
     */
    public Audio(File audioFile) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        clip.open(AudioSystem.getAudioInputStream(audioFile));
    }

    /**
     * Starts playing the audio from the beginning.
     * If the asset is closed, this method does nothing.
     */
    public synchronized void play() {
        if (isClosed()) return;
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Pauses the playback of the audio. The current position is maintained,
     * so calling {@link #resume()} will continue from where it left off.
     * If the asset is closed, this method does nothing.
     */
    public synchronized void pause() {
        if (isClosed()) return;
        clip.stop();
    }

    /**
     * Resumes the playback of the audio from its currently paused position.
     * If the asset is closed, this method does nothing.
     */
    public synchronized void resume() {
        if (isClosed()) return;
        clip.start();
    }

    /**
     * Stops the playback of the audio and resets its position to the beginning.
     * If the asset is closed, this method does nothing.
     */
    public synchronized void stop() {
        if (isClosed()) return;
        clip.stop();
        clip.setFramePosition(0);
    }

    /**
     * Starts playing the audio and sets it to loop continuously.
     * If the audio is not currently playing, it starts playback.
     * If the asset is closed, this method does nothing.
     */
    public synchronized void loop() {
        if (isClosed()) return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        if (!isPlaying()) play();
    }

    /**
     * Checks if the audio is currently playing.
     *
     * @return {@code true} if the audio is running; {@code false} if it is stopped, paused, or closed.
     */
    public synchronized boolean isPlaying() {
        if (isClosed()) return false;
        return clip.isRunning();
    }

}
