package com.yourname.financetracker.core;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FinancePaths {
	
	private static final String APP_DIR = ".finance-app";
    private static final String FILE_NAME = "finance-data.xml";

    private FinancePaths() {}

    public static Path getDataFilePath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, APP_DIR, FILE_NAME);
    }
}
