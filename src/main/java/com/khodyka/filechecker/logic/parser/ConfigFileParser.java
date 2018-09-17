package com.khodyka.filechecker.logic.parser;

import com.khodyka.filechecker.logic.ConfigFileSymbols;
import com.khodyka.filechecker.logic.ConfigFileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.khodyka.filechecker.logic.ConfigFileSymbols.COMMENT_PREFIX;
import static com.khodyka.filechecker.logic.ConfigFileSymbols.ROOT_FOLDER;

public class ConfigFileParser implements FileParser<Map<String, List<String>>> {

    private static final String CHARSET_CP_1251 = "windows-1251";

    @Override
    public Map<String, List<String>> parse(final String filePath) {
        final Path rulesFilePath = Paths.get(filePath);
        try {
            final List<String> configFileLines = Files
                    .lines(rulesFilePath, Charset.forName(CHARSET_CP_1251))
                    .filter(line -> !line.isEmpty())
                    .filter(ConfigFileUtils::isNotComment)
                    .map(String::trim)
                    .map(this::getFilenameWithoutExtension)
                    .map(this::clearFilenameFromComments)
                    .collect(Collectors.toList());
            return mapListToFolderToFilesInFolder(configFileLines);
        } catch (IOException e) {
            throw new RuntimeException("Файл настроек не найден: " + filePath, e);
        }
    }

    private Map<String, List<String>> mapListToFolderToFilesInFolder(final List<String> configFileLines) {
        final Map<String, List<String>> folderToFilesInFolder = new HashMap<>();
        int folderBegin = -1;
        String folderName = null;
        for (int i = 0; i < configFileLines.size(); i++) {
            final String configLine = configFileLines.get(i);
            if (ConfigFileUtils.isFolderDefinition(configLine) && folderBegin != -1) {
                final ArrayList<String> filesInFolder = new ArrayList<>(configFileLines.subList(folderBegin + 1, i));
                folderToFilesInFolder.put(folderName, filesInFolder);
                folderBegin = -1;
                folderName = null;
            }
            if (ConfigFileUtils.isFolderDefinition(configLine)) {
                folderBegin = i;
                folderName = configLine;
            }
        }
        folderToFilesInFolder.put(ConfigFileSymbols.INCLUDE_META_INFO_PREFIX, collectMetaInfo(configFileLines));
        return folderToFilesInFolder;
    }

    private List<String> collectMetaInfo(final List<String> configFileLines) {
        return configFileLines
                .stream()
                .filter(ConfigFileUtils::isIncludedMetaInfoDefinition)
                .collect(Collectors.toList());
    }

    private String clearFilenameFromComments(final String line) {
        final int indexOfComment = line.indexOf(COMMENT_PREFIX) - 1;
        return indexOfComment > -1 ? line.substring(0, indexOfComment) : line;
    }

    private String getFilenameWithoutExtension(final String filename) {
        if (filename.equals(ROOT_FOLDER)) {
            return filename;
        }
        int indexOfDot = filename.lastIndexOf(".");
        return indexOfDot > -1 ? filename.substring(0, indexOfDot) : filename;
    }
}
