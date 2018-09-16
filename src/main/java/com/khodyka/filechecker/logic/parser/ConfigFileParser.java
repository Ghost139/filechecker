package com.khodyka.filechecker.logic.parser;

import com.khodyka.filechecker.logic.ConfigFileSymbols;
import com.khodyka.filechecker.logic.ConfigFileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.khodyka.filechecker.logic.ConfigFileSymbols.COMMENT_PREFIX;
import static com.khodyka.filechecker.logic.ConfigFileSymbols.INCLUDE_META_INFO_PREFIX;
import static com.khodyka.filechecker.logic.ConfigFileSymbols.ROOT_FOLDER;

public class ConfigFileParser implements FileParser<List<String>> {

    private static final String CHARSET_CP_1251 = "windows-1251";

    @Override
    public List<String> parse(final String filePath) {
        final Path rulesFilePath = Paths.get(filePath);
        try {
            List<String> collect = Files
                    .lines(rulesFilePath, Charset.forName(CHARSET_CP_1251))
                    .filter(this::isNotEmpty)
                    .filter(ConfigFileUtils::isNotComment)
                    .filter(ConfigFileUtils::isNotIncludedMetaInfoDefinition)
                    .map(String::trim)
                    .map(this::getFilenameWithoutExtension)
                    .map(this::clearFilenameFromComments)
                    .collect(Collectors.toList());

            int folderBegin = -1;
            int folderEnd = -1;
            String folderName = null;
            for (int i = 0; i < collect.size(); i++) {
                String configLine = collect.get(i);
                if (ConfigFileUtils.isFolderDefinition(configLine)) {
                    folderBegin = i;
                    continue;
                }
                if (ConfigFileUtils.isFolderDefinition(configLine) && folderBegin != -1) {
                    folderEnd = i;
                }
                if (folderBegin != -1 && folderEnd != -1) {

                }
            }

            return collect;
        } catch (IOException e) {
            throw new RuntimeException("Файл настроек не найден: " + filePath, e);
        }
    }

    private boolean isNotEmpty(final String line) {
        return !line.isEmpty();
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
