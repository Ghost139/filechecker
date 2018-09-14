package com.khodyka.filechecker.logic;

import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import com.khodyka.filechecker.logic.filesystem.indexer.FolderIndexer;
import com.khodyka.filechecker.logic.parser.FileParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CheckerEngine {

    private final FolderIndexer<FolderIndex> folderIndexer;
    private final FileParser<List<String>> fileParser;

    public CheckerEngine(final FolderIndexer folderIndexer, final FileParser fileParser) {
        this.folderIndexer = folderIndexer;
        this.fileParser = fileParser;
    }

    public int checkFiles(final String configFilePath, final String searchFolderPath) {
        final FolderIndex folderIndex = folderIndexer.doIndexFolderTree(searchFolderPath);
        final List<String> ruleLines = fileParser.parse(configFilePath);

        try (PrintWriter printWriter = new PrintWriter(searchFolderPath + "/log.txt")) {

            String currentFolder = null;
            for (final String ruleLine : ruleLines) {
                if (isFolderDefinition(ruleLine)) {
                    currentFolder = ruleLine;
                    continue;
                }
                final List<String> folderFiles = folderIndex.getFolderFiles(currentFolder);
                if (!folderFiles.contains(ruleLine)) {
                    printWriter.println(currentFolder);
                    printWriter.println(ruleLine + " - файл отсутствует!");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private boolean isFolderDefinition(final String ruleLine) {
        return ruleLine.equals(ConfigFileSymbols.ROOT_FOLDER)
                || ruleLine.startsWith(ConfigFileSymbols.FOLDER_PREFIX);
    }
}
