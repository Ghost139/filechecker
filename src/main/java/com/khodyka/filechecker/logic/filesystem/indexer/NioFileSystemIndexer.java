package com.khodyka.filechecker.logic.filesystem.indexer;

import com.khodyka.filechecker.logic.RuleFileSympols;
import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NioFileSystemIndexer implements FileSystemIndexer {

    private final FolderIndex folderIndex;

    @Override
    public void doIndex(final String rootPath) {
        final Path rootFolder = Paths.get(rootPath);
        final List<String> rootFolderFiles = getFolderFiles(rootFolder);
        indexingFolder(RuleFileSympols.ROOT_FOLDER, rootFolderFiles);

        try {
            Files
                    .walk(rootFolder)
                    .filter(Files::isDirectory)
                    .filter(folder -> folder != rootFolder)
                    .map(Path::getFileName)
                    .forEach(folder -> indexingFolder(folder, getFolderFiles(folder)));
        } catch (IOException e) {
            throw new RuntimeException("Path doesn't exist: " + root);
        }
    }

    private void indexingFolder(Path folderName, final List<String> folderFiles) {
        final String folderName = RuleFileSympols.FOLDER_PREFIX + folderPath.getFileName().toString();
        final List<String> filesInFolder = getFolderFiles(folderPath);
        folderIndex.addIndex(folderName, filesInFolder);
    }

    private List<String> getFolderFiles(final Path folderPath) {
        try {
            return Files
                    .walk(folderPath, 1)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Object::toString)
                    .map(s -> s.substring(0, s.lastIndexOf('.')))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Path doesn't exist: " + folderPath);
        }
    }
}
