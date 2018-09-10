package com.khodyka.filechecker.logic.processor;

import com.khodyka.filechecker.logic.RuleFileSympols;
import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import com.khodyka.filechecker.logic.filesystem.indexer.FileSystemIndexer;
import com.khodyka.filechecker.logic.parser.FileParser;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class RuleFileStreamProcessor implements RuleFileProcessor {

    private final FileSystemIndexer fileSystemIndexer;
    private final FileParser<Stream<String>> fileParser;
    private final FolderIndex folderIndex;

    @Override
    public void process(final String ruleFilePath) {
        fileSystemIndexer.doIndex(ruleFilePath);

        final List<String> rules = fileParser.parse(ruleFilePath).collect(Collectors.toList());

        String currentFolder = null;

        for (String ruleLine : rules) {
            if (ruleLine.equals(RuleFileSympols.ROOT_FOLDER) || ruleLine.equals(RuleFileSympols.FOLDER_PREFIX)) {
                currentFolder = ruleLine;
                continue;
            }

            List<String> folderFiles = folderIndex.getFolderFiles(currentFolder);
            if (!folderFiles.contains(ruleLine)) {
                System.err.println("Folder doesn't containe file:" + ruleLine);
            }
        }
    }
}
