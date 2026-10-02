package rroyo.jgameengine.core.gameutils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class Audio {

    private final Clip clip = AudioSystem.getClip();

    public Audio(File audioFile) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        clip.open(AudioSystem.getAudioInputStream(audioFile));
    }

    public synchronized void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public synchronized void pause() {
        clip.stop();
    }

    public synchronized void resume() {
        clip.start();
    }

    public synchronized void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    public synchronized void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        if (!isPlaying()) play();
    }

    public synchronized void close() {
        clip.close();
    }

    public synchronized boolean isPlaying() {
        return clip.isRunning();
    }

}
