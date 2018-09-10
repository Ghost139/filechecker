package com.khodyka.filechecker.logic.filesystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FolderIndex {

    private final Map<String, List<String>> folderNameToFolderFilde = new HashMap<>();

    public void addIndex(final String folderName, final List<String> filesInFolder) {
        folderNameToFolderFilde.put(folderName, filesInFolder);
    }

    public List<String> getFolderFiles(String folderName) {
        return folderNameToFolderFilde.get(folderName);
    }
}
