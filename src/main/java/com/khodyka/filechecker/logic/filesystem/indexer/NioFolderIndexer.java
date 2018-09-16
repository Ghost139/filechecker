package com.khodyka.filechecker.logic.filesystem.indexer;

import com.khodyka.filechecker.logic.ConfigFileSymbols;
import com.khodyka.filechecker.logic.ConfigFileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NioFolderIndexer implements FolderIndexer<Map<String, List<String>>> {

    private final Map<String, List<String>> index = new HashMap<>();

    @Override
    public Map<String, List<String>> doIndexFolderTree(final String rootPath) {
        final Path rootFolder = Paths.get(rootPath);
        final List<String> filesInRootFolder = getFilesFromFolder(rootFolder);

        index.put(ConfigFileSymbols.ROOT_FOLDER, filesInRootFolder);

        try {
            Files
                    .walk(rootFolder)
                    .filter(Files::isDirectory)
                    .filter(folder -> folder != rootFolder)
                    .forEach(this::addFolderAndFilesToIndex);
        } catch (IOException e) {
            throw new RuntimeException("Некорректный путь: " + rootPath);
        }
        return index;
    }

    private void addFolderAndFilesToIndex(final Path folderName) {
        index.put(ConfigFileSymbols.FOLDER_PREFIX + folderName.getFileName().toString(),
                getFilesFromFolder(folderName));
    }

    private List<String> getFilesFromFolder(final Path folderPath) {
        try {
            return Files
                    .walk(folderPath, 1)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Object::toString)
                    .map(this::getFilenameWithoutExtension)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Некорректный путь: " + folderPath, e);
        }
    }

    private String getFilenameWithoutExtension(final String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }
}
