package com.khodyka.filechecker.logic;

import static com.khodyka.filechecker.logic.ConfigFileSymbols.COMMENT_PREFIX;
import static com.khodyka.filechecker.logic.ConfigFileSymbols.INCLUDE_META_INFO_PREFIX;

public class ConfigFileUtils {

    public static boolean isFolderDefinition(final String configLine) {
        return configLine.equals(ConfigFileSymbols.ROOT_FOLDER)
                || configLine.startsWith(ConfigFileSymbols.FOLDER_PREFIX);
    }

    public static boolean isNotFolderDefinition(final String configLine) {
        return !(configLine.equals(ConfigFileSymbols.ROOT_FOLDER)
                || configLine.startsWith(ConfigFileSymbols.FOLDER_PREFIX));
    }

    public static boolean isIncludedMetaInfoDefinition(final String configLine) {
        return configLine.startsWith(INCLUDE_META_INFO_PREFIX);
    }

    public static boolean isNotIncludedMetaInfoDefinition(final String configLine) {
        return !configLine.startsWith(INCLUDE_META_INFO_PREFIX);
    }

    public static boolean isNotComment(final String line) {
        return !line.startsWith(COMMENT_PREFIX);
    }

    public static boolean isComment(final String line) {
        return line.startsWith(COMMENT_PREFIX);
    }



}
