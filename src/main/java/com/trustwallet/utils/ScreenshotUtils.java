package com.trustwallet.utils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private final AndroidDriver<MobileElement> driver;
    private final String screenshotPath;

    public ScreenshotUtils(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
        this.screenshotPath = ConfigManager.getInstance().getProperty("screenshot.path");
        createScreenshotDirectory();
    }

    private void createScreenshotDirectory() {
        File directory = new File(screenshotPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.info("Screenshot directory created: {}", screenshotPath);
            } else {
                logger.warn("Failed to create screenshot directory: {}", screenshotPath);
            }
        }
    }

    public String captureScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = screenshotPath + fileName;
        
        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileUtils.copyFile(screenshot, destination);
            logger.info("Screenshot captured: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
            return null;
        }
    }

    public String captureScreenshotOnFailure(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "FAILURE_" + testName + "_" + timestamp + ".png";
        String filePath = screenshotPath + fileName;
        
        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileUtils.copyFile(screenshot, destination);
            logger.info("Failure screenshot captured: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture failure screenshot", e);
            return null;
        }
    }
} 