package com.khodyka.filechecker.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LabelButtonPanel extends JPanel {

    private JLabel label;
    private SystemPathTextField pathTextField;
    private JButton selectPathButton;

    public LabelButtonPanel(final String labelText, final Font labelFont, final Font textFieldFont) {
        this.label = new Label(labelText, labelFont);
        this.pathTextField = new SystemPathTextField(textFieldFont);
        this.selectPathButton = new JButton("...");

        addButtonActionListener();

        this.setLayout(new FlowLayout());
        this.add(label);
        this.add(pathTextField);
        this.add(selectPathButton);
    }

    public String getTextFieldPath() {
        return this.pathTextField.getText();
    }

    private void addButtonActionListener() {
        this.selectPathButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                this.pathTextField.setText(selectedFile.getAbsolutePath());
            }
        });
    }
}
