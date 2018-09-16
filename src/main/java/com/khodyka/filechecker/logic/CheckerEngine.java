package com.khodyka.filechecker.logic;

import com.khodyka.filechecker.logic.filesystem.indexer.FolderIndexer;
import com.khodyka.filechecker.logic.parser.FileParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.khodyka.filechecker.logic.ConfigFileSymbols.INCLUDED_COMMENT_PREFIX;

public class CheckerEngine {

    private static final String LOGFILE_NAME = "/logger.txt";

    private final FolderIndexer<Map<String, List<String>>> folderIndexer;
    private final FileParser<List<String>> fileParser;

    public CheckerEngine(final FolderIndexer folderIndexer, final FileParser fileParser) {
        this.folderIndexer = folderIndexer;
        this.fileParser = fileParser;
    }

    public int checkFiles(final String configFilePath, final String searchFolderPath) {
        final Map<String, List<String>> folderToFilesIndex = folderIndexer
                .doIndexFolderTree(searchFolderPath);
        final List<String> configFileLines = fileParser.parse(configFilePath);
        final List<String> missedFilesLoggerLines = new ArrayList<>();

        String currentFolderName = null;
        List<String> filesInCurrentFolder = null;

        for (final String configFileLine : configFileLines) {
            if (isFolderDefinition(configFileLine)) {
                currentFolderName = configFileLine;
                filesInCurrentFolder = folderToFilesIndex.get(configFileLine);
                continue;
            }
            if (filesInCurrentFolder == null) {
                final List<String> folderFiles = getFolderFilesDefinedInConfigFile(currentFolderName, configFileLines);
                missedFilesLoggerLines.addAll(folderFiles);
            }
            if (isIncludedCommentDefinition(configFileLine)) {
                missedFilesLoggerLines.add(configFileLine);
            }
            if (isFileMissed(filesInCurrentFolder, configFileLine)) {
                if (isFolderNameNotLoggedYet(currentFolderName, missedFilesLoggerLines)) {
                    missedFilesLoggerLines.add(currentFolderName);
                }
                missedFilesLoggerLines.add(configFileLine);
            }
        }
        writeMissedFilesToLogFile(searchFolderPath, missedFilesLoggerLines);
        return 1;
    }

    private boolean isFolderDefinition(final String configLine) {
        return configLine.equals(ConfigFileSymbols.ROOT_FOLDER)
                || configLine.startsWith(ConfigFileSymbols.FOLDER_PREFIX);
    }

    private boolean isFileMissed(final List<String> filesInCurrentFolder, final String configFileLine) {
        return filesInCurrentFolder != null
                && !filesInCurrentFolder.contains(configFileLine);
    }

    private boolean isIncludedCommentDefinition(final String configLine) {
        return configLine.startsWith(INCLUDED_COMMENT_PREFIX);
    }

    private boolean isFolderNameNotLoggedYet(final String configLine, final List<String> loggerFileLines) {
        return !loggerFileLines.contains(configLine);
    }

    private List<String> getFolderFilesDefinedInConfigFile(final String folderName, final List<String> configFileLines) {
        final List<String> folderFiles = new ArrayList<>();
        for (String line : configFileLines) {
            if (line.equals(folderName)) {
                folderFiles.add(line);
                continue;
            }
            if (isFolderDefinition(line)) {
                break;
            }
            if (isIncludedCommentDefinition(line)) {
                continue;
            }
            if (!line.isEmpty()) {
                folderFiles.add(line);
            }
        }
        return folderFiles;
    }

    private void writeMissedFilesToLogFile(final String searchFolderPath, final List<String> missedFiles) {
        try (PrintWriter printWriter = new PrintWriter(searchFolderPath + LOGFILE_NAME)) {
            missedFiles.forEach(line -> {
                if (isFolderDefinition(line)) {
                    printWriter.println();
                }
                printWriter.println(line);
            });
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в лог файл", e);
        }
    }
}
