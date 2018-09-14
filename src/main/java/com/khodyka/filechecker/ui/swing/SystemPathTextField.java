package com.khodyka.filechecker.ui.swing;

import javax.swing.*;
import java.awt.*;

public class SystemPathTextField extends JTextField {

    private static final int COLUMNS = 29;

    public SystemPathTextField(final Font font) {
        this.setFont(font);
        this.setEditable(true);
        this.setColumns(COLUMNS);
        this.setBackground(Color.WHITE);
    }
}
