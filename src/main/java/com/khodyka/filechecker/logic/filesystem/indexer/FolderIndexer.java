package com.khodyka.filechecker.logic.filesystem.indexer;

public interface FolderIndexer<I> {

    I doIndexFolderTree(String rootPath);
}
