package com.khodyka.filechecker.ui;

import com.khodyka.filechecker.logic.CheckerEngine;
import com.khodyka.filechecker.logic.filesystem.indexer.FolderIndexer;
import com.khodyka.filechecker.logic.filesystem.indexer.NioFolderIndexer;
import com.khodyka.filechecker.logic.parser.ConfigFileParser;
import com.khodyka.filechecker.logic.parser.FileParser;
import com.khodyka.filechecker.ui.swing.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.khodyka.filechecker.ui.Fonts.LABEL_FONT;
import static com.khodyka.filechecker.ui.Fonts.TEXT_FIELD_FONT;

public class MainController {

    private final FolderIndexer<Map<String, List<String>>> folderIndexer = new NioFolderIndexer();
    private final FileParser<Map<String, List<String>>> fileParser = new ConfigFileParser();
    private final CheckerEngine checkerEngine = new CheckerEngine(folderIndexer, fileParser);

    /**
     * Frame
     */
    private MainFrame mainFrame = new MainFrame();

    /**
     * Panels
     */
    private LabelButtonPanel configFilePanel = new LabelButtonPanel("Файл настроек:  ",
            LABEL_FONT, TEXT_FIELD_FONT);
    private LabelButtonPanel searchFolderPanel = new LabelButtonPanel("Папка поиска:     ",
            LABEL_FONT, TEXT_FIELD_FONT);
    private JPanel runSearchButtonPanel = new JPanel(new FlowLayout());

    /**
     * Buttons
     */
    private RunSearchButton runSearchBtn = new RunSearchButton();

    public void initUI() {
        runSearchButtonPanel.add(runSearchBtn);

        mainFrame.add(configFilePanel);
        mainFrame.add(searchFolderPanel);
        mainFrame.add(runSearchButtonPanel);

        mainFrame.setVisible(true);
    }

    public void runSearch() {
        runSearchBtn.addActionListener(e -> {
            try {
                final int result = checkerEngine.checkFiles(configFilePanel.getTextFieldPath().trim(),
                        searchFolderPanel.getTextFieldPath().trim());
                if (result == 1) {
                    JOptionPane.showMessageDialog(mainFrame, "Поиск завершен!");
                }
            } catch (final Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex);
            }
        });
    }
}
