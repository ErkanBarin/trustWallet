package com.trustwallet.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing screenshots.
 */
public class ScreenshotUtils {
    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private final AppiumDriver<MobileElement> driver;
    private final ConfigManager configManager;

    /**
     * Constructor for ScreenshotUtils.
     *
     * @param driver AppiumDriver instance
     */
    public ScreenshotUtils(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        this.configManager = ConfigManager.getInstance();
    }

    /**
     * Capture screenshot and save to file.
     *
     * @param testName name of the test
     * @return path to the saved screenshot file or null if failed
     */
    public String captureScreenshotOnFailure(String testName) {
        log.info("Capturing screenshot for failed test: {}", testName);

        if (driver == null) {
            log.error("Driver is null, cannot capture screenshot");
            return null;
        }

        try {
            // Create screenshot directory if it doesn't exist
            String screenshotDir = configManager.getProperty("screenshot.path", "./screenshots/");
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate unique filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = String.format("%s_%s_%s.png",
                    testName.replaceAll("[^a-zA-Z0-9.-]", "_"),
                    timestamp,
                    configManager.getProperty("environment", "unknown"));

            // Capture screenshot
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filePath = screenshotDir + fileName;

            // Save screenshot to file
            FileUtils.copyFile(screenshotFile, new File(filePath));
            log.info("Screenshot saved to: {}", filePath);

            return filePath;
        } catch (IOException e) {
            log.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Capture screenshot as byte array for Allure reporting.
     *
     * @return screenshot as byte array or null if failed
     */
    public byte[] captureScreenshotAsBytes() {
        log.debug("Capturing screenshot as bytes");

        if (driver == null) {
            log.error("Driver is null, cannot capture screenshot");
            return null;
        }

        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot as bytes", e);
            return null;
        }
    }
}