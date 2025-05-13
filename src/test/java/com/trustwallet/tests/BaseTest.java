package com.trustwallet.tests;

import com.trustwallet.utils.ConfigManager;
import com.trustwallet.utils.ScreenshotUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Base class for all test classes.
 * Handles driver setup, configuration loading, and test lifecycle.
 */
public class BaseTest {
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected AppiumDriver<MobileElement> driver;
    protected ConfigManager configManager;
    protected ScreenshotUtils screenshotUtils;

    /**
     * Setup method that runs before each test class.
     * Loads configuration and initializes the driver.
     * 
     * @param environment test environment to use (dev, staging, prod)
     * @throws Exception if driver initialization fails
     */
    @Parameters({ "environment" })
    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("dev") String environment) throws Exception {
        log.info("Setting up test environment: {}", environment);

        // Load environment-specific configuration
        configManager = ConfigManager.getInstance();
        configManager.loadConfig(environment);

        // Initialize driver
        initializeDriver();

        // Initialize screenshot utility
        screenshotUtils = new ScreenshotUtils(driver);

        log.info("Test setup complete");
    }

    /**
     * Initialize the Appium driver with capabilities from configuration.
     * 
     * @throws Exception if driver initialization fails
     */
    private void initializeDriver() throws Exception {
        log.info("Initializing Appium driver");

        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Set common capabilities
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, configManager.getProperty("device.platform"));
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, configManager.getProperty("device.version"));
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, configManager.getProperty("device.name"));
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180);

        // Set application capabilities
        String appPath = configManager.getProperty("app.path");
        if (appPath != null && !appPath.isEmpty()) {
            File app = new File(appPath);
            if (app.exists()) {
                capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            } else {
                log.warn("App file not found at: {}", appPath);
            }
        }

        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, configManager.getProperty("app.package"));
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, configManager.getProperty("app.activity"));

        // Set test mode specific capabilities
        String testMode = configManager.getProperty("test.mode");
        if ("browserstack".equals(testMode)) {
            setupBrowserStackCapabilities(capabilities);
        }

        // Set reset strategy
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);

        // Create driver instance
        String appiumServerUrl = configManager.getProperty("appium.server.url");
        log.info("Connecting to Appium server at: {}", appiumServerUrl);
        driver = new AndroidDriver<>(new URL(appiumServerUrl), capabilities);

        // Set implicit wait
        int implicitWait = configManager.getIntProperty("implicit.wait");
        driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);

        log.info("Driver initialized successfully");
    }

    /**
     * Set up BrowserStack specific capabilities.
     * 
     * @param capabilities DesiredCapabilities to modify
     */
    private void setupBrowserStackCapabilities(DesiredCapabilities capabilities) {
        log.info("Setting up BrowserStack capabilities");

        capabilities.setCapability("browserstack.user", configManager.getProperty("browserstack.username"));
        capabilities.setCapability("browserstack.key", configManager.getProperty("browserstack.access.key"));
        capabilities.setCapability("app", configManager.getProperty("browserstack.app.url"));
        capabilities.setCapability("device", configManager.getProperty("browserstack.device"));
        capabilities.setCapability("os_version", configManager.getProperty("browserstack.os.version"));
        capabilities.setCapability("project", "Trust Wallet");
        capabilities.setCapability("build", "Build " + System.currentTimeMillis());
        capabilities.setCapability("name", "Wallet Creation Tests");
    }

    /**
     * Method that runs after each test method.
     * Takes screenshot on test failure and attaches it to Allure report.
     * 
     * @param result test result
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test failed: {}", result.getName());
            captureScreenshot(result.getName());
        }
    }

    /**
     * Teardown method that runs after each test class.
     * Quits the driver and releases resources.
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        log.info("Tearing down test environment");

        if (driver != null) {
            driver.quit();
            log.info("Driver quit successfully");
        }
    }

    /**
     * Capture screenshot and attach it to Allure report.
     * 
     * @param testName name of the test
     * @return screenshot as byte array
     */
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] captureScreenshot(String testName) {
        log.info("Capturing screenshot for failed test: {}", testName);

        try {
            return driver.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot", e);
            return null;
        }
    }
}