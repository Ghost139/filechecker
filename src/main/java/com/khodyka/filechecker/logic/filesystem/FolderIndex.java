package com.khodyka.filechecker.logic.filesystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FolderIndex {

    private final Map<String, List<String>> folderNameToFolderFiles = new HashMap<>();

    public void addIndex(final String folderName, final List<String> filesInFolder) {
        folderNameToFolderFiles.put(folderName, filesInFolder);
    }

    public List<String> getFolderFiles(final String folderName) {
        return folderNameToFolderFiles.get(folderName);
    }
}
