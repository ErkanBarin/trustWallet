package com.trustwallet.tests;

import com.trustwallet.pages.CreateWalletPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Wallet Management")
@Feature("Wallet Creation")
public class CreateWalletTest extends BaseTest {

    @Test(description = "Create a new wallet with PIN")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User creates a new wallet")
    @Description("Test the complete flow of creating a new wallet: agree to T&C, reveal seed phrase, confirm seed phrase, and set PIN")
    public void testCreateWallet() {
        logger.info("Starting Create Wallet test");

        // Initialize page object
        CreateWalletPage createWalletPage = new CreateWalletPage(driver);

        // Verify page is loaded
        Assert.assertTrue(createWalletPage.isPageLoaded(), "Create Wallet page is not loaded");

        // Step 1: Agree to Terms and Conditions
        createWalletPage.agreeTerms();

        // Step 2: Reveal seed phrase and remember it
        List<String> seedWords = createWalletPage.revealSeed();
        Assert.assertFalse(seedWords.isEmpty(), "Seed phrase should not be empty");
        Assert.assertEquals(seedWords.size(), 12, "Seed phrase should contain 12 words");

        // Step 3: Confirm seed phrase
        createWalletPage.confirmSeed(seedWords);

        // Step 4: Set PIN
        String pin = "123456";
        createWalletPage.setPIN(pin);

        // Verify wallet was created successfully
        Assert.assertTrue(createWalletPage.isWalletCreated(), "Wallet creation was not successful");

        logger.info("Create Wallet test completed successfully");
    }
}