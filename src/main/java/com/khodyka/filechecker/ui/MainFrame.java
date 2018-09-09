package com.khodyka.filechecker.ui;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final int FRAME_HEIGHT = 200;
    private static final int FRAME_WIDTH = 400;

    public MainFrame() {
        super();
        initFrame();
    }

    private void initFrame() {
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(new Panel());
    }
}
