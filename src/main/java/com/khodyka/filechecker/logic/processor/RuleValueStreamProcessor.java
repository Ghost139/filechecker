package com.khodyka.filechecker.logic.processor;

import com.khodyka.filechecker.logic.RuleFileSympols;
import com.khodyka.filechecker.logic.filesystem.FolderIndex;
import com.khodyka.filechecker.logic.parser.FileParser;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public class RuleValueStreamProcessor implements RuleValueProcessor {

    private final FileParser<Stream<String>> fileParser;
    private final FolderIndex folderIndex;

    @Override
    public void process(final String ruleFilePath) {
//        fileParser
//                .parse(ruleFilePath))

    }
}
