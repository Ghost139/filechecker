package com.khodyka.filechecker;

import com.khodyka.filechecker.ui.MainController;

public class FileCheckerMain {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.initUI();
        mainController.runSearch();
    }
}
