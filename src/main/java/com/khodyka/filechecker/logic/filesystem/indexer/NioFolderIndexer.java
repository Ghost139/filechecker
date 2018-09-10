package com.khodyka.filechecker.logic.filesystem.indexer;

import com.khodyka.filechecker.logic.RuleFileSymbols;
import com.khodyka.filechecker.logic.filesystem.FolderIndex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class NioFolderIndexer implements FolderIndexer<FolderIndex> {

    private static final FolderIndex folderIndex = new FolderIndex();

    @Override
    public FolderIndex doIndexFolderTree(final String rootPath) {
        final Path rootFolder = Paths.get(rootPath);
        final List<String> rootFolderFiles = getFolderFilesNames(rootFolder);

        folderIndex.addIndex(RuleFileSymbols.ROOT_FOLDER, rootFolderFiles);
        try {
            Files
                    .walk(rootFolder)
                    .filter(Files::isDirectory)
                    .filter(folder -> folder != rootFolder)
                    .forEach(this::doIndexFolder);
        } catch (IOException e) {
            throw new RuntimeException("Path doesn't exist: " + rootFolder);
        }
        return folderIndex;
    }

    @Override
    public FolderIndex getIndex() {
        return folderIndex;
    }

    private void doIndexFolder(final Path folderName) {
        folderIndex.addIndex(RuleFileSymbols.FOLDER_PREFIX + folderName.getFileName().toString(),
                getFolderFilesNames(folderName));
    }

    private List<String> getFolderFilesNames(final Path folderPath) {
        try {
            return Files
                    .walk(folderPath, 1)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Object::toString)
                    .map(this::getFilenameWithoutExtension)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Path doesn't exist: " + folderPath);
        }
    }

    private String getFilenameWithoutExtension(final String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }
}
