package com.khodyka.filechecker.logic.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.khodyka.filechecker.logic.ConfigFileSymbols.COMMENT_PREFIX;
import static com.khodyka.filechecker.logic.ConfigFileSymbols.ROOT_FOLDER;

public class ConfigFileParser implements FileParser<List<String>> {

    @Override
    public List<String> parse(final String filePath) {
        final Path rulesFilePath = Paths.get(filePath);
        try {
            return Files
                    .lines(rulesFilePath, Charset.forName("windows-1251"))
                    .filter(this::isNotEmpty)
                    .filter(this::isNotComment)
                    .map(String::trim)
                    .map(this::getFilenameWithoutExtension)
                    .map(this::clearFilenameFromComments)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Файл настроек не найден: " + filePath, e);
        }
    }

    private boolean isNotEmpty(final String line) {
        return !line.isEmpty();
    }

    private boolean isNotComment(final String line) {
        return !line.startsWith(COMMENT_PREFIX);
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
