package com.khodyka.filechecker.logic;

import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import com.khodyka.filechecker.logic.filesystem.indexer.FolderIndexer;
import com.khodyka.filechecker.logic.parser.FileParser;

import java.util.List;

public class FileChecker {

    private final FolderIndexer<FolderIndex> folderIndexer;
    private final FileParser<List<String>> fileParser;

    public FileChecker(final FolderIndexer folderIndexer, final FileParser fileParser) {
        this.folderIndexer = folderIndexer;
        this.fileParser = fileParser;
    }

    public void checkFiles(final String rootFolder) {
        final FolderIndex folderIndex = folderIndexer.doIndexFolderTree(rootFolder);
        final List<String> ruleLines = fileParser.parse(rootFolder);

        String currentFolder = null;
        for (final String ruleLine : ruleLines) {
            if (isFolderDefinition(ruleLine)) {
                currentFolder = ruleLine;
                continue;
            }
            final List<String> folderFiles = folderIndex.getFolderFiles(currentFolder);
            if (!folderFiles.contains(ruleLine)) {
                System.err.println(currentFolder);
                System.err.println(ruleLine + " is missing");
            }
        }
    }

    private boolean isFolderDefinition(final String ruleLine) {
        return ruleLine.equals(RuleFileSymbols.ROOT_FOLDER) || ruleLine.startsWith(RuleFileSymbols.FOLDER_PREFIX);
    }
}
