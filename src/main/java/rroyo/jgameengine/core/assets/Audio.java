package rroyo.jgameengine.core.assets;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio extends Asset {

    private Clip clip = AudioSystem.getClip();

    public Audio(File audioFile) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        clip.open(AudioSystem.getAudioInputStream(audioFile));
    }

    public synchronized void play() {
        if (isClosed()) return;
        clip.setFramePosition(0);
        clip.start();
    }

    public synchronized void pause() {
        if (isClosed()) return;
        clip.stop();
    }

    public synchronized void resume() {
        if (isClosed()) return;
        clip.start();
    }

    public synchronized void stop() {
        if (isClosed()) return;
        clip.stop();
        clip.setFramePosition(0);
    }

    public synchronized void loop() {
        if (isClosed()) return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        if (!isPlaying()) play();
    }

    public synchronized boolean isPlaying() {
        if (isClosed()) return false;
        return clip.isRunning();
    }

}
