package com.khodyka.filechecker;

import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import com.khodyka.filechecker.logic.filesystem.traverse.RecursiveFileSystemTraverser;

public class FilecheckerMain {
    public static void main(String[] args) {

        final FolderIndex folderIndex = new FolderIndex();

        final RecursiveFileSystemTraverser recursiveFileSystemTraverser = new RecursiveFileSystemTraverser(folderIndex);
        recursiveFileSystemTraverser.traverse("D:\\my_projects\\filechecker\\example\\root");

    }
}
