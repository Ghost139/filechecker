package com.khodyka.filechecker.logic.filesystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FolderIndex {

    private final Map<String, List<String>> folderNameToFolderPath = new HashMap<>();

    public void addIndex(final String folderName, final List<String> filesInFolder) {
        folderNameToFolderPath.put(folderName, filesInFolder);
    }
}
