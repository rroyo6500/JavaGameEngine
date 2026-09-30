package rroyo.jgameengine;

import javax.swing.*;
import java.awt.*;

public class GameFrame {

    private final Graphics2D g;
    private Timer timer;

    public GameFrame(JPanel frame) {
        this((Graphics2D) frame.getGraphics());
    }

    public GameFrame(Graphics2D g) {
        this.g = g;
    }



}

