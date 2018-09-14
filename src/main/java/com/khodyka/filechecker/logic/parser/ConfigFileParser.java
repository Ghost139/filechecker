package com.khodyka.filechecker.logic.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFileParser implements FileParser<List<String>> {

    @Override
    public List<String> parse(final String filePath) {
        final Path rulesFilePath = Paths.get(filePath);
        try {
            return Files
                    .lines(rulesFilePath)
                    .filter(this::isNotEmpty)
                    .map(String::trim)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Config file by path not found: " + filePath);
        }
    }

    private boolean isNotEmpty(final String line) {
        return !line.isEmpty();
    }
}
