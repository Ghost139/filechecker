package com.khodyka.filechecker.logic.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RuleFileParser implements FileParser<List<String>> {

    private final static String RULE_FILE_NAME = "_rules.txt";

    @Override
    public List<String> parse(final String path) {
        final Path rulesFilePath = Paths.get(String.format("%s/%s", path, RULE_FILE_NAME));
        try {
            return Files
                    .lines(rulesFilePath)
                    .filter(this::isNotEmpty)
                    .map(String::trim)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("File by path not found: " + path);
        }
    }

    private boolean isNotEmpty(final String line) {
        return !line.isEmpty();
    }
}
