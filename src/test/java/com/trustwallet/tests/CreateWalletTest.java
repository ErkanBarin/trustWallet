package com.trustwallet.tests;

import com.trustwallet.pages.CreateWalletPage;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for wallet creation flow.
 */
@Epic("Wallet Management")
@Feature("Wallet Creation")
public class CreateWalletTest extends BaseTest {
        private static final Logger log = LoggerFactory.getLogger(CreateWalletTest.class);
        private CreateWalletPage createWalletPage;

        /**
         * Set up method that runs before each test method.
         * Initializes the CreateWalletPage object.
         */
        @BeforeMethod
        public void setUpTest() {
                log.info("Setting up Create Wallet test");
                createWalletPage = new CreateWalletPage(driver);
                Assert.assertTrue(createWalletPage.isPageLoaded(), "Create Wallet page is not loaded");
        }

        /**
         * Test the happy path flow of wallet creation.
         */
        @Test(description = "Create wallet with valid inputs - happy path")
        @Severity(SeverityLevel.CRITICAL)
        @Story("User can create a new wallet following all steps correctly")
        @Description("Tests the complete wallet creation flow with valid inputs")
        public void testCreateWalletHappyPath() {
                log.info("Starting wallet creation happy path test");

                // Accept terms and continue
                createWalletPage.acceptTerms()
                                .clickNext();

                // Reveal and copy seed phrase
                createWalletPage.revealSeedPhrase();
                List<String> seedWords = createWalletPage.getSeedPhraseWords();
                Assert.assertEquals(seedWords.size(), 12, "Seed phrase should contain 12 words");

                createWalletPage.copySeedPhrase()
                                .clickNext();

                // Confirm seed phrase
                createWalletPage.confirmSeedPhrase(seedWords)
                                .clickContinue();

                // Set PIN
                createWalletPage.enterPin("123456")
                                .clickContinue();

                // Confirm PIN
                createWalletPage.enterPin("123456")
                                .clickContinue();

                // Verify wallet creation success
                Assert.assertTrue(createWalletPage.isWalletCreationSuccessful(),
                                "Wallet creation was not successful");

                log.info("Wallet creation happy path test completed successfully");
        }

        /**
         * Test that Terms and Conditions must be accepted before proceeding.
         */
        @Test(description = "Verify Terms and Conditions must be accepted")
        @Severity(SeverityLevel.NORMAL)
        @Story("User cannot proceed without accepting Terms and Conditions")
        @Description("Tests that the Next button is disabled until Terms and Conditions are accepted")
        public void testTermsAndConditionsRequired() {
                log.info("Starting Terms and Conditions requirement test");

                // Try to proceed without accepting terms
                createWalletPage.clickNext();

                // Verify we're still on the same page
                Assert.assertTrue(createWalletPage.isPageLoaded(),
                                "User was able to proceed without accepting Terms and Conditions");

                // Now accept terms and verify we can proceed
                createWalletPage.acceptTerms()
                                .clickNext();

                // Verify we've moved to the seed phrase screen
                createWalletPage.revealSeedPhrase();
                Assert.assertTrue(createWalletPage.getSeedPhraseWords().size() > 0,
                                "Failed to proceed to seed phrase screen after accepting terms");

                log.info("Terms and Conditions requirement test completed successfully");
        }

        /**
         * Test that seed phrase confirmation must be correct.
         */
        @Test(description = "Verify seed phrase confirmation must be correct")
        @Severity(SeverityLevel.CRITICAL)
        @Story("User must enter correct seed phrase during confirmation")
        @Description("Tests that incorrect seed phrase confirmation is rejected")
        public void testInvalidSeedPhraseConfirmation() {
                log.info("Starting invalid seed phrase confirmation test");

                // Accept terms and continue to seed phrase
                createWalletPage.acceptTerms()
                                .clickNext();

                // Reveal seed phrase
                createWalletPage.revealSeedPhrase();
                List<String> seedWords = createWalletPage.getSeedPhraseWords();

                // Continue to confirmation
                createWalletPage.clickNext();

                // Deliberately use incorrect order for confirmation (reverse the list)
                List<String> reversedWords = seedWords.reversed();
                createWalletPage.confirmSeedPhrase(reversedWords)
                                .clickContinue();

                // Verify error message is displayed
                String errorMessage = createWalletPage.getErrorMessage();
                Assert.assertTrue(errorMessage.contains("incorrect"),
                                "Expected error message for incorrect seed phrase confirmation");

                log.info("Invalid seed phrase confirmation test completed successfully");
        }

        /**
         * Test that PIN confirmation must match.
         */
        @Test(description = "Verify PIN confirmation must match")
        @Severity(SeverityLevel.CRITICAL)
        @Story("User must enter matching PINs during setup")
        @Description("Tests that mismatched PINs are rejected")
        public void testPinMismatch() {
                log.info("Starting PIN mismatch test");

                // Complete steps up to PIN entry
                createWalletPage.acceptTerms()
                                .clickNext();

                createWalletPage.revealSeedPhrase();
                List<String> seedWords = createWalletPage.getSeedPhraseWords();

                createWalletPage.clickNext();

                createWalletPage.confirmSeedPhrase(seedWords)
                                .clickContinue();

                // Enter initial PIN
                createWalletPage.enterPin("123456")
                                .clickContinue();

                // Enter different PIN for confirmation
                createWalletPage.enterPin("654321")
                                .clickContinue();

                // Verify error message is displayed
                String errorMessage = createWalletPage.getErrorMessage();
                Assert.assertTrue(errorMessage.contains("match"),
                                "Expected error message for PIN mismatch");

                log.info("PIN mismatch test completed successfully");
        }
}