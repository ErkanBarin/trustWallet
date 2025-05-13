package com.trustwallet.utils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);
    private final AndroidDriver<MobileElement> driver;
    private final int explicitWaitTimeout;

    public WaitUtils(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
        this.explicitWaitTimeout = ConfigManager.getInstance().getIntProperty("explicit.wait");
    }

    public MobileElement waitForElementVisible(By locator) {
        logger.info("Waiting for element to be visible: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeout);
        try {
            return (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (WebDriverException e) {
            logger.error("Element not visible within timeout: {}", locator, e);
            throw e;
        }
    }

    public MobileElement waitForElementClickable(By locator) {
        logger.info("Waiting for element to be clickable: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeout);
        try {
            return (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (WebDriverException e) {
            logger.error("Element not clickable within timeout: {}", locator, e);
            throw e;
        }
    }

    public boolean waitForElementToDisappear(By locator) {
        logger.info("Waiting for element to disappear: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, explicitWaitTimeout);
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (WebDriverException e) {
            logger.error("Element still visible within timeout: {}", locator, e);
            throw e;
        }
    }

    public void waitForSeconds(int seconds) {
        logger.info("Waiting for {} seconds", seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted during wait", e);
        }
    }
} 