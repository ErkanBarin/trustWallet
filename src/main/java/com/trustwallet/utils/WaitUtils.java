package com.trustwallet.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for handling waits in Appium tests.
 */
public class WaitUtils {
    private static final Logger log = LoggerFactory.getLogger(WaitUtils.class);
    private final AppiumDriver<MobileElement> driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    private final WebDriverWait longWait;

    /**
     * Constructor for WaitUtils.
     *
     * @param driver AppiumDriver instance
     */
    public WaitUtils(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 15);
        this.shortWait = new WebDriverWait(driver, 5);
        this.longWait = new WebDriverWait(driver, 30);

        // Configure wait to ignore specific exceptions
        this.wait.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Wait for element to be visible.
     *
     * @param locator element locator
     * @return MobileElement that is visible
     */
    public MobileElement waitForElementToBeVisible(By locator) {
        try {
            log.debug("Waiting for element to be visible: {}", locator);
            return (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Element not visible after waiting: {}", locator);
            throw e;
        }
    }

    /**
     * Wait for element to be clickable.
     *
     * @param locator element locator
     * @return MobileElement that is clickable
     */
    public MobileElement waitForElementToBeClickable(By locator) {
        try {
            log.debug("Waiting for element to be clickable: {}", locator);
            return (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            log.error("Element not clickable after waiting: {}", locator);
            throw e;
        }
    }

    /**
     * Wait for element to be invisible.
     *
     * @param locator element locator
     * @return true if element is invisible, false otherwise
     */
    public boolean waitForElementToBeInvisible(By locator) {
        try {
            log.debug("Waiting for element to be invisible: {}", locator);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Element still visible after waiting: {}", locator);
            throw e;
        }
    }

    /**
     * Wait for element with short timeout.
     *
     * @param locator element locator
     * @return MobileElement that is visible
     */
    public MobileElement waitForElementWithShortTimeout(By locator) {
        try {
            log.debug("Waiting for element with short timeout: {}", locator);
            return (MobileElement) shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Element not visible after short wait: {}", locator);
            throw e;
        }
    }

    /**
     * Wait for element with long timeout.
     *
     * @param locator element locator
     * @return MobileElement that is visible
     */
    public MobileElement waitForElementWithLongTimeout(By locator) {
        try {
            log.debug("Waiting for element with long timeout: {}", locator);
            return (MobileElement) longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            log.error("Element not visible after long wait: {}", locator);
            throw e;
        }
    }
}