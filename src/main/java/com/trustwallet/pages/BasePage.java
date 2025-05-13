package com.trustwallet.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trustwallet.utils.WaitUtils;

import java.time.Duration;

/**
 * Base class for all page objects.
 */
public abstract class BasePage {
    protected final AppiumDriver<MobileElement> driver;
    protected final WaitUtils waitUtils;
    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);

    /**
     * Constructor for BasePage.
     * 
     * @param driver AppiumDriver instance
     */
    public BasePage(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);

        // Initialize elements with PageFactory
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
        log.info("Initialized {} page", this.getClass().getSimpleName());
    }

    /**
     * Check if the page is loaded.
     * 
     * @return true if page is loaded, false otherwise
     */
    public abstract boolean isPageLoaded();

    /**
     * Click on an element.
     * 
     * @param locator element locator
     */
    protected void click(By locator) {
        log.debug("Clicking on element: {}", locator);
        waitUtils.waitForElementToBeClickable(locator).click();
    }

    /**
     * Enter text in a field.
     * 
     * @param locator element locator
     * @param text    text to enter
     */
    protected void sendKeys(By locator, String text) {
        log.debug("Entering text '{}' in element: {}", text, locator);
        waitUtils.waitForElementToBeVisible(locator).sendKeys(text);
    }

    /**
     * Get text from an element.
     * 
     * @param locator element locator
     * @return text from element
     */
    protected String getText(By locator) {
        log.debug("Getting text from element: {}", locator);
        return waitUtils.waitForElementToBeVisible(locator).getText();
    }

    /**
     * Check if element is displayed.
     * 
     * @param locator element locator
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Swipe up on screen.
     */
    protected void swipeUp() {
        log.debug("Performing swipe up gesture");
        // Implementation for swipe up
    }

    /**
     * Swipe down on screen.
     */
    protected void swipeDown() {
        log.debug("Performing swipe down gesture");
        // Implementation for swipe down
    }

    @Step("Swipe from element {0} to element {1}")
    protected void swipeFromElementToElement(By fromLocator, By toLocator) {
        MobileElement fromElement = waitUtils.waitForElementVisible(fromLocator);
        MobileElement toElement = waitUtils.waitForElementVisible(toLocator);

        int fromX = fromElement.getLocation().getX() + fromElement.getSize().getWidth() / 2;
        int fromY = fromElement.getLocation().getY() + fromElement.getSize().getHeight() / 2;
        int toX = toElement.getLocation().getX() + toElement.getSize().getWidth() / 2;
        int toY = toElement.getLocation().getY() + toElement.getSize().getHeight() / 2;

        log.info("Swiping from ({},{}) to ({},{})", fromX, fromY, toX, toY);

        new TouchAction<>(driver)
                .press(PointOption.point(fromX, fromY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(toX, toY))
                .release()
                .perform();
    }
}