package com.trustwallet.pages;

import com.trustwallet.utils.WaitUtils;
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

import java.time.Duration;

public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected final AndroidDriver<MobileElement> driver;
    protected final WaitUtils waitUtils;

    public BasePage(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Step("Click on element: {0}")
    protected void click(By locator) {
        logger.info("Clicking on element: {}", locator);
        waitUtils.waitForElementClickable(locator).click();
    }

    @Step("Enter text: {1} into element: {0}")
    protected void sendKeys(By locator, String text) {
        logger.info("Entering text: '{}' into element: {}", text, locator);
        MobileElement element = waitUtils.waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Get text from element: {0}")
    protected String getText(By locator) {
        logger.info("Getting text from element: {}", locator);
        return waitUtils.waitForElementVisible(locator).getText();
    }

    @Step("Check if element is displayed: {0}")
    protected boolean isElementDisplayed(By locator) {
        try {
            logger.info("Checking if element is displayed: {}", locator);
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            logger.info("Element is not displayed: {}", locator);
            return false;
        }
    }

    @Step("Swipe from element {0} to element {1}")
    protected void swipeFromElementToElement(By fromLocator, By toLocator) {
        MobileElement fromElement = waitUtils.waitForElementVisible(fromLocator);
        MobileElement toElement = waitUtils.waitForElementVisible(toLocator);

        int fromX = fromElement.getLocation().getX() + fromElement.getSize().getWidth() / 2;
        int fromY = fromElement.getLocation().getY() + fromElement.getSize().getHeight() / 2;
        int toX = toElement.getLocation().getX() + toElement.getSize().getWidth() / 2;
        int toY = toElement.getLocation().getY() + toElement.getSize().getHeight() / 2;

        logger.info("Swiping from ({},{}) to ({},{})", fromX, fromY, toX, toY);

        new TouchAction<>(driver)
                .press(PointOption.point(fromX, fromY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(toX, toY))
                .release()
                .perform();
    }

    @Step("Wait for page to load")
    public abstract boolean isPageLoaded();
}