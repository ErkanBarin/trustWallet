package com.trustwallet.tests;

import com.trustwallet.utils.ConfigManager;
import com.trustwallet.utils.ScreenshotUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Attachment;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected AndroidDriver<MobileElement> driver;
    protected ScreenshotUtils screenshotUtils;
    protected ConfigManager configManager;

    @BeforeMethod
    @Parameters({ "deviceName", "platformVersion", "udid" })
    public void setUp(String deviceName, String platformVersion, String udid) {
        logger.info("Setting up test");
        configManager = ConfigManager.getInstance();

        String testMode = configManager.getProperty("test.mode");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if ("browserstack".equalsIgnoreCase(testMode)) {
            setupBrowserStackCapabilities(capabilities);
        } else {
            setupLocalCapabilities(capabilities, deviceName, platformVersion, udid);
        }

        // Initialize driver
        try {
            String appiumServerUrl = "browserstack".equalsIgnoreCase(testMode)
                    ? "https://hub-cloud.browserstack.com/wd/hub"
                    : configManager.getProperty("appium.server.url");

            driver = new AndroidDriver<>(new URL(appiumServerUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(
                    configManager.getIntProperty("implicit.wait"),
                    TimeUnit.SECONDS);

            screenshotUtils = new ScreenshotUtils(driver);
            logger.info("Driver initialized successfully");
        } catch (MalformedURLException e) {
            logger.error("Failed to initialize driver", e);
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }

    private void setupLocalCapabilities(DesiredCapabilities capabilities, String deviceName, String platformVersion,
            String udid) {
        // Device capabilities
        capabilities.setCapability("deviceName",
                deviceName != null ? deviceName : configManager.getProperty("device.name"));
        capabilities.setCapability("platformName", configManager.getProperty("device.platform"));
        capabilities.setCapability("platformVersion",
                platformVersion != null ? platformVersion : configManager.getProperty("device.version"));

        if (udid != null && !udid.isEmpty()) {
            capabilities.setCapability("udid", udid);
        } else if (configManager.getProperty("device.udid") != null
                && !configManager.getProperty("device.udid").isEmpty()) {
            capabilities.setCapability("udid", configManager.getProperty("device.udid"));
        }

        // Application capabilities
        String appPath = configManager.getProperty("app.path");
        File app = new File(appPath);
        if (app.exists()) {
            capabilities.setCapability("app", app.getAbsolutePath());
        } else {
            capabilities.setCapability("appPackage", configManager.getProperty("app.package"));
            capabilities.setCapability("appActivity", configManager.getProperty("app.activity"));
        }

        // Other capabilities
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("noReset", false);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("newCommandTimeout", 180);
    }

    private void setupBrowserStackCapabilities(DesiredCapabilities capabilities) {
        // BrowserStack credentials
        capabilities.setCapability("browserstack.user", configManager.getProperty("browserstack.username"));
        capabilities.setCapability("browserstack.key", configManager.getProperty("browserstack.access.key"));

        // App
        capabilities.setCapability("app", configManager.getProperty("browserstack.app.url"));

        // Device
        capabilities.setCapability("device", configManager.getProperty("browserstack.device"));
        capabilities.setCapability("os_version", configManager.getProperty("browserstack.os.version"));

        // Other capabilities
        capabilities.setCapability("project", "Trust Wallet Test");
        capabilities.setCapability("build", "Trust Wallet Build 1.0");
        capabilities.setCapability("name", "Create Wallet Test");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.info("Test failed: {}", result.getName());
            if (driver != null) {
                String screenshotPath = screenshotUtils.captureScreenshotOnFailure(result.getName());
                if (screenshotPath != null) {
                    logger.info("Screenshot captured at: {}", screenshotPath);
                }
            }
        }

        if (driver != null) {
            logger.info("Quitting driver");
            driver.quit();
        }
    }

    @Attachment(value = "Screenshot", type = "image/png")
    protected byte[] attachScreenshot() {
        if (driver != null) {
            return driver.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
        }
        return null;
    }
}