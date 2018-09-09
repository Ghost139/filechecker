package com.khodyka.filechecker.ui;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    public Panel() {
        super();

        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(2);
        gridLayout.setRows(2);

        this.setLayout(new BorderLayout());
        Button hello = new Button("Hello");
        hello.setSize(20,20);
        this.add(hello);
        this.add(hello);
        this.add(hello);
        this.add(hello);
    }
}
