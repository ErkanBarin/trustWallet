package com.trustwallet.pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.List;

public class CreateWalletPage extends BasePage {

    // Terms and Conditions locators
    private static final By TERMS_TITLE = By.id("com.wallet.crypto.trustapp:id/title");
    private static final By AGREE_BUTTON = By.id("com.wallet.crypto.trustapp:id/action_confirm");

    // Seed phrase locators
    private static final By SEED_PHRASE_TITLE = By.xpath("//android.widget.TextView[contains(@text, 'Secret Phrase')]");
    private static final By SEED_PHRASE_WORDS = By.xpath("//android.widget.TextView[contains(@resource-id, 'word')]");
    private static final By CONTINUE_BUTTON = By.id("com.wallet.crypto.trustapp:id/action_continue");

    // Confirm seed phrase locators
    private static final By CONFIRM_TITLE = By.xpath("//android.widget.TextView[contains(@text, 'Verify')]");
    private static final By SEED_WORD_BUTTONS = By.xpath("//android.widget.Button[contains(@resource-id, 'word')]");
    private static final By VERIFY_BUTTON = By.id("com.wallet.crypto.trustapp:id/action_verify");

    // PIN setup locators
    private static final By PIN_TITLE = By.xpath("//android.widget.TextView[contains(@text, 'Create PIN')]");
    private static final By PIN_BUTTONS = By
            .xpath("//android.widget.TextView[@resource-id='com.wallet.crypto.trustapp:id/text']");
    private static final By PIN_CONFIRM_TITLE = By.xpath("//android.widget.TextView[contains(@text, 'Confirm PIN')]");

    // Success screen locators
    private static final By SUCCESS_MESSAGE = By.xpath("//android.widget.TextView[contains(@text, 'Wallet Created')]");

    public CreateWalletPage(AndroidDriver<MobileElement> driver) {
        super(driver);
    }

    @Override
    public boolean isPageLoaded() {
        return isElementDisplayed(TERMS_TITLE) ||
                isElementDisplayed(SEED_PHRASE_TITLE) ||
                isElementDisplayed(CONFIRM_TITLE) ||
                isElementDisplayed(PIN_TITLE);
    }

    @Step("Agree to Terms and Conditions")
    public CreateWalletPage agreeTerms() {
        logger.info("Agreeing to Terms and Conditions");
        waitUtils.waitForElementVisible(TERMS_TITLE);
        click(AGREE_BUTTON);
        return this;
    }

    @Step("Reveal seed phrase")
    public List<String> revealSeed() {
        logger.info("Revealing seed phrase");
        waitUtils.waitForElementVisible(SEED_PHRASE_TITLE);

        // Collect all seed words
        List<MobileElement> seedWordElements = driver.findElements(SEED_PHRASE_WORDS);
        List<String> seedWords = seedWordElements.stream()
                .map(MobileElement::getText)
                .collect(java.util.stream.Collectors.toList());

        logger.info("Seed phrase collected: {}", seedWords);
        click(CONTINUE_BUTTON);
        return seedWords;
    }

    @Step("Confirm seed phrase")
    public CreateWalletPage confirmSeed(List<String> seedWords) {
        logger.info("Confirming seed phrase");
        waitUtils.waitForElementVisible(CONFIRM_TITLE);

        // Select each word in the correct order
        for (String word : seedWords) {
            List<MobileElement> wordButtons = driver.findElements(SEED_WORD_BUTTONS);
            for (MobileElement button : wordButtons) {
                if (button.getText().equals(word)) {
                    button.click();
                    break;
                }
            }
        }

        click(VERIFY_BUTTON);
        return this;
    }

    @Step("Set PIN: {0}")
    public CreateWalletPage setPIN(String pin) {
        logger.info("Setting PIN");
        waitUtils.waitForElementVisible(PIN_TITLE);
        enterPIN(pin);

        // Confirm PIN
        waitUtils.waitForElementVisible(PIN_CONFIRM_TITLE);
        enterPIN(pin);

        return this;
    }

    private void enterPIN(String pin) {
        for (char digit : pin.toCharArray()) {
            String digitStr = String.valueOf(digit);
            By digitLocator = By
                    .xpath("//android.widget.TextView[@resource-id='com.wallet.crypto.trustapp:id/text' and @text='"
                            + digitStr + "']");
            click(digitLocator);
        }
    }

    @Step("Check if wallet was created successfully")
    public boolean isWalletCreated() {
        return isElementDisplayed(SUCCESS_MESSAGE);
    }
}