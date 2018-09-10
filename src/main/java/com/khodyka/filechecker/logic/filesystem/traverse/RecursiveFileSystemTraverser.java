package com.khodyka.filechecker.logic.filesystem.traverse;

import com.khodyka.filechecker.logic.RuleFileSympols;
import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RecursiveFileSystemTraverser implements FileSystemTraverser {

    private final FolderIndex folderIndex;

    @Override
    public void traverse(final String rootPath) {
        final Path root = Paths.get(rootPath);
        final List<String> filesInRootFolder = collectFilesInFolder(root);
        folderIndex.addIndex(RuleFileSympols.ROOT_FOLDER, filesInRootFolder);
        try {
            Files
                    .walk(root)
                    .filter(Files::isDirectory)
                    .forEach(this::doFolderIndex);
        } catch (IOException e) {
            throw new RuntimeException("Path doesn't exist: " + root);
        }
    }

    private void doFolderIndex(final Path folderPath) {
        final String folderName = RuleFileSympols.FOLDER_PREFIX + folderPath.getFileName().toString();
        final List<String> filesInFolder = collectFilesInFolder(folderPath);
        folderIndex.addIndex(folderName, filesInFolder);
    }

    private List<String> collectFilesInFolder(final Path folderPath) {
        try {
            return Files
                    .walk(folderPath, 1)
                    .filter(Files::isRegularFile)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Path doesn't exist: " + folderPath);
        }
    }
}
