package com.trustwallet.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Utility class for common element interactions.
 */
public class ElementUtils {
    private static final Logger log = LoggerFactory.getLogger(ElementUtils.class);
    private final AppiumDriver<MobileElement> driver;
    private final WaitUtils waitUtils;

    /**
     * Constructor for ElementUtils.
     *
     * @param driver AppiumDriver instance
     */
    public ElementUtils(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    /**
     * Tap on element.
     *
     * @param locator element locator
     */
    public void tap(By locator) {
        try {
            log.debug("Tapping on element: {}", locator);
            MobileElement element = waitUtils.waitForElementToBeClickable(locator);
            element.click();
        } catch (TimeoutException e) {
            log.error("Failed to tap on element: {}", locator, e);
            throw e;
        }
    }

    /**
     * Tap on element.
     *
     * @param element MobileElement to tap on
     */
    public void tap(MobileElement element) {
        try {
            log.debug("Tapping on element: {}", element);
            element.click();
        } catch (Exception e) {
            log.error("Failed to tap on element: {}", element, e);
            throw e;
        }
    }

    /**
     * Long press on element.
     *
     * @param locator    element locator
     * @param durationMs duration of long press in milliseconds
     */
    public void longPress(By locator, long durationMs) {
        try {
            log.debug("Long pressing on element: {} for {}ms", locator, durationMs);
            MobileElement element = waitUtils.waitForElementToBeVisible(locator);

            TouchAction<?> touchAction = new TouchAction<>(driver);
            touchAction.longPress(PointOption.point(element.getCenter().getX(), element.getCenter().getY()))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(durationMs)))
                    .release()
                    .perform();
        } catch (TimeoutException e) {
            log.error("Failed to long press on element: {}", locator, e);
            throw e;
        }
    }

    /**
     * Swipe from one element to another.
     *
     * @param fromLocator source element locator
     * @param toLocator   destination element locator
     */
    public void swipeFromElementToElement(By fromLocator, By toLocator) {
        try {
            log.debug("Swiping from element {} to element {}", fromLocator, toLocator);
            MobileElement fromElement = waitUtils.waitForElementToBeVisible(fromLocator);
            MobileElement toElement = waitUtils.waitForElementToBeVisible(toLocator);

            int fromX = fromElement.getCenter().getX();
            int fromY = fromElement.getCenter().getY();
            int toX = toElement.getCenter().getX();
            int toY = toElement.getCenter().getY();

            TouchAction<?> touchAction = new TouchAction<>(driver);
            touchAction.press(PointOption.point(fromX, fromY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .moveTo(PointOption.point(toX, toY))
                    .release()
                    .perform();
        } catch (TimeoutException e) {
            log.error("Failed to swipe between elements", e);
            throw e;
        }
    }

    /**
     * Swipe up on screen.
     */
    public void swipeUp() {
        log.debug("Swiping up on screen");
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(startX, endY))
                .release()
                .perform();
    }

    /**
     * Swipe down on screen.
     */
    public void swipeDown() {
        log.debug("Swiping down on screen");
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * 0.8);

        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(startX, endY))
                .release()
                .perform();
    }

    /**
     * Swipe left on screen.
     */
    public void swipeLeft() {
        log.debug("Swiping left on screen");
        Dimension size = driver.manage().window().getSize();
        int startY = size.height / 2;
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);

        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(endX, startY))
                .release()
                .perform();
    }

    /**
     * Swipe right on screen.
     */
    public void swipeRight() {
        log.debug("Swiping right on screen");
        Dimension size = driver.manage().window().getSize();
        int startY = size.height / 2;
        int startX = (int) (size.width * 0.2);
        int endX = (int) (size.width * 0.8);

        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(endX, startY))
                .release()
                .perform();
    }

    /**
     * Check if element exists.
     *
     * @param locator element locator
     * @return true if element exists, false otherwise
     */
    public boolean elementExists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Get text from element.
     *
     * @param locator element locator
     * @return text from element
     */
    public String getText(By locator) {
        try {
            log.debug("Getting text from element: {}", locator);
            return waitUtils.waitForElementToBeVisible(locator).getText();
        } catch (TimeoutException e) {
            log.error("Failed to get text from element: {}", locator, e);
            throw e;
        }
    }

    /**
     * Enter text in element.
     *
     * @param locator element locator
     * @param text    text to enter
     */
    public void enterText(By locator, String text) {
        try {
            log.debug("Entering text '{}' in element: {}", text, locator);
            MobileElement element = waitUtils.waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
        } catch (TimeoutException e) {
            log.error("Failed to enter text in element: {}", locator, e);
            throw e;
        }
    }

    /**
     * Clear text in element.
     *
     * @param locator element locator
     */
    public void clearText(By locator) {
        try {
            log.debug("Clearing text in element: {}", locator);
            waitUtils.waitForElementToBeVisible(locator).clear();
        } catch (TimeoutException e) {
            log.error("Failed to clear text in element: {}", locator, e);
            throw e;
        }
    }
}