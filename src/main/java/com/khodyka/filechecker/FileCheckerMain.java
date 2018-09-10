package com.khodyka.filechecker;

import com.khodyka.filechecker.logic.FileChecker;
import com.khodyka.filechecker.logic.filesystem.indexer.NioFolderIndexer;
import com.khodyka.filechecker.logic.parser.RuleFileParser;

public class FileCheckerMain {
    public static void main(String[] args) {
        final NioFolderIndexer folderIndexer = new NioFolderIndexer();
        final RuleFileParser fileParser = new RuleFileParser();
        final FileChecker fileChecker = new FileChecker(folderIndexer, fileParser);
        fileChecker.checkFiles("D:\\my_projects\\filechecker\\example\\root");
    }
}
