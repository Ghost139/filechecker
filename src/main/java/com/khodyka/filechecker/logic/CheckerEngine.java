package com.khodyka.filechecker.logic;

import com.khodyka.filechecker.logic.filesystem.indexer.FolderIndexer;
import com.khodyka.filechecker.logic.parser.FileParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.khodyka.filechecker.logic.ConfigFileSymbols.INCLUDE_META_INFO_PREFIX;
import static com.khodyka.filechecker.logic.ConfigFileUtils.isFolderDefinition;

public class CheckerEngine {

    private static final String LOGFILE_NAME = "/logger.txt";

    private final FolderIndexer<Map<String, List<String>>> folderIndexer;
    private final FileParser<Map<String, List<String>>> fileParser;

    public CheckerEngine(final FolderIndexer folderIndexer, final FileParser fileParser) {
        this.folderIndexer = folderIndexer;
        this.fileParser = fileParser;
    }

    public int checkFiles(final String configFilePath, final String searchFolderPath) {
        final Map<String, List<String>> folderToFilesIndex = folderIndexer.doIndexFolderTree(searchFolderPath);
        final Map<String, List<String>> configFileLines = fileParser.parse(configFilePath);
        final List<String> missedFilesLoggerLines = new ArrayList<>();

        final List<String> metaInfo = configFileLines.get(INCLUDE_META_INFO_PREFIX);
        if (isMetaInfoExists(metaInfo)) {
            missedFilesLoggerLines.addAll(metaInfo);
            configFileLines.remove(INCLUDE_META_INFO_PREFIX);
        }

        for (Map.Entry<String, List<String>> folderToFilesConfigEntry : configFileLines.entrySet()) {
            final String folderName = folderToFilesConfigEntry.getKey();
            final List<String> filesInFolder = folderToFilesConfigEntry.getValue();
            final List<String> filesInFolderIndex = folderToFilesIndex.get(folderName);

            if (filesInFolderIndex != null) {
                filesInFolder.removeAll(filesInFolderIndex);
            }
            if (!filesInFolder.isEmpty()) {
                missedFilesLoggerLines.add(folderName);
                missedFilesLoggerLines.addAll(filesInFolder);
            }
        }
        writeMissedFilesToLogFile(searchFolderPath, missedFilesLoggerLines);
        return 1;
    }

    private void writeMissedFilesToLogFile(final String searchFolderPath, final List<String> lines) {
        try (PrintWriter printWriter = new PrintWriter(searchFolderPath + LOGFILE_NAME)) {
            lines.forEach(line -> {
                if (isFolderDefinition(line)) {
                    printWriter.println();
                }
                printWriter.println(line);
            });
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в лог файл", e);
        }
    }

    private boolean isMetaInfoExists(final List<String> metaInfo) {
        return metaInfo != null && !metaInfo.isEmpty();
    }
}
