package com.khodyka.filechecker.ui.swing;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    /**
     * Main frame name
     */
    public static final String MAIN_FRAME_NAME = "File Checker v.1";

    /**
     * Main frame grid
     */
    private static final int MAIN_FRAME_GRID_LAYOUT_ROWS = 3;
    private static final int MAIN_FRAME_GRID_LAYOUT_COLS = 1;

    /**
     * Main frame size
     */
    private static final int MAIN_FRAME_WIDTH = 550;
    private static final int MAIN_FRAME_HEIGHT = 180;

    public MainFrame() {
        this.setName(MAIN_FRAME_NAME);
        this.setLayout(new GridLayout(MAIN_FRAME_GRID_LAYOUT_ROWS, MAIN_FRAME_GRID_LAYOUT_COLS));
        this.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
