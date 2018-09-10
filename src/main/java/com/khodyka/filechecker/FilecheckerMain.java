package com.khodyka.filechecker;

import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import com.khodyka.filechecker.logic.filesystem.indexer.NioFileSystemIndexer;
import com.khodyka.filechecker.logic.parser.RuleFileStreamParser;
import com.khodyka.filechecker.logic.processor.RuleFileStreamProcessor;

public class FilecheckerMain {
    public static void main(String[] args) {

        final FolderIndex folderIndex = new FolderIndex();
        final NioFileSystemIndexer recursiveFileSystemTraverser = new NioFileSystemIndexer(folderIndex);
        final RuleFileStreamParser fileParser = new RuleFileStreamParser();

        final RuleFileStreamProcessor ruleValueStreamProcessor = new RuleFileStreamProcessor(
                recursiveFileSystemTraverser, fileParser, folderIndex);

        ruleValueStreamProcessor.process("D:\\my_projects\\filechecker\\example\\root\\_rules.txt");
    }
}
