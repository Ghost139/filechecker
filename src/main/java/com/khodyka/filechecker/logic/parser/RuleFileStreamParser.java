package com.khodyka.filechecker.logic.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RuleFileStreamParser implements FileParser<Stream<String>> {

    @Override
    public Stream<String> parse(final String path) {
        final Path rulesFilePath = Paths.get(path);
        try {
            return Files
                    .lines(rulesFilePath)
                    .filter(this::isNotEmpty)
                    .map(String::trim);
        } catch (IOException e) {
            throw new RuntimeException("File by path not found: " + path);
        }
    }

    private boolean isNotEmpty(final String line) {
        return !line.isEmpty();
    }
}
