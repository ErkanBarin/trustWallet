package com.trustwallet.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Page object for the Create Wallet flow.
 */
public class CreateWalletPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(CreateWalletPage.class);

    // Element locators using Page Factory annotations
    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/terms_checkbox")
    private MobileElement termsCheckbox;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/terms_text")
    private MobileElement termsText;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/next_button")
    private MobileElement nextButton;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/reveal_phrase_button")
    private MobileElement revealPhraseButton;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/seed_phrase_container")
    private MobileElement seedPhraseContainer;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/copy_button")
    private MobileElement copyButton;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/seed_word")
    private List<MobileElement> seedWords;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/word_option")
    private List<MobileElement> wordOptions;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/continue_button")
    private MobileElement continueButton;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/pin_input")
    private MobileElement pinInput;

    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/error_message")
    private MobileElement errorMessage;

    // Backup locators for elements that might be challenging with Page Factory
    private static final By WELCOME_BANNER = By.id("com.wallet.crypto.trustapp:id/welcome_banner");

    /**
     * Constructor for CreateWalletPage.
     *
     * @param driver AppiumDriver instance
     */
    public CreateWalletPage(AppiumDriver<MobileElement> driver) {
        super(driver);
    }

    /**
     * Check if page is loaded.
     *
     * @return true if page is loaded, false otherwise
     */
    @Override
    public boolean isPageLoaded() {
        log.info("Checking if Create Wallet page is loaded");
        return isElementDisplayed(By.id("com.wallet.crypto.trustapp:id/terms_checkbox"));
    }

    /**
     * Accept terms and conditions.
     *
     * @return this page object
     */
    @Step("Accept terms and conditions")
    public CreateWalletPage acceptTerms() {
        log.info("Accepting terms and conditions");
        termsCheckbox.click();
        return this;
    }

    /**
     * Open terms and conditions.
     *
     * @return this page object
     */
    @Step("Open terms and conditions")
    public CreateWalletPage openTerms() {
        log.info("Opening terms and conditions");
        termsText.click();
        return this;
    }

    /**
     * Click next button.
     *
     * @return this page object
     */
    @Step("Click next button")
    public CreateWalletPage clickNext() {
        log.info("Clicking next button");
        nextButton.click();
        return this;
    }

    /**
     * Reveal seed phrase.
     *
     * @return this page object
     */
    @Step("Reveal seed phrase")
    public CreateWalletPage revealSeedPhrase() {
        log.info("Revealing seed phrase");
        revealPhraseButton.click();
        return this;
    }

    /**
     * Copy seed phrase.
     *
     * @return this page object
     */
    @Step("Copy seed phrase")
    public CreateWalletPage copySeedPhrase() {
        log.info("Copying seed phrase");
        copyButton.click();
        return this;
    }

    /**
     * Get seed phrase words.
     *
     * @return list of seed phrase words
     */
    @Step("Get seed phrase words")
    public List<String> getSeedPhraseWords() {
        log.info("Getting seed phrase words");
        return seedWords.stream()
                .map(MobileElement::getText)
                .toList();
    }

    /**
     * Confirm seed phrase.
     *
     * @param words list of words to confirm
     * @return this page object
     */
    @Step("Confirm seed phrase")
    public CreateWalletPage confirmSeedPhrase(List<String> words) {
        log.info("Confirming seed phrase");
        for (String word : words) {
            for (MobileElement option : wordOptions) {
                if (option.getText().equals(word)) {
                    option.click();
                    break;
                }
            }
        }
        return this;
    }

    /**
     * Click continue button.
     *
     * @return this page object
     */
    @Step("Click continue button")
    public CreateWalletPage clickContinue() {
        log.info("Clicking continue button");
        continueButton.click();
        return this;
    }

    /**
     * Enter PIN.
     *
     * @param pin PIN to enter
     * @return this page object
     */
    @Step("Enter PIN: {0}")
    public CreateWalletPage enterPin(String pin) {
        log.info("Entering PIN");
        pinInput.sendKeys(pin);
        return this;
    }

    /**
     * Get error message.
     *
     * @return error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        log.info("Getting error message");
        return errorMessage.getText();
    }

    /**
     * Check if wallet creation is successful.
     *
     * @return true if wallet creation is successful, false otherwise
     */
    @Step("Check if wallet creation is successful")
    public boolean isWalletCreationSuccessful() {
        log.info("Checking if wallet creation is successful");
        return isElementDisplayed(WELCOME_BANNER);
    }
}