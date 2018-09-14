package com.khodyka.filechecker.logic.parser;

public interface FileParser<R> {

    R parse(String filePath);
}
