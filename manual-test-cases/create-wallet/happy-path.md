# Happy Path Test Cases for Wallet Creation

## TC ID: M01

**Scenario:** T&C acceptance gating  
**Pre-conditions:** Fresh app launch  
**Steps:**

1. Open app
2. On "Create a new wallet" screen, tap Terms & Conditions link
3. Close T&C and try Next
4. Check "I agree" checkbox

**Expected Results:**

- In-app browser opens T&C
- Next remains disabled without checkbox
- Next becomes enabled after checkbox is checked

## TC ID: M02

**Scenario:** Reveal & copy seed phrase  
**Pre-conditions:** "Create a new wallet" → Next  
**Steps:**

1. Tap Reveal Secret Phrase
2. Verify 12-word grid appears
3. Tap Copy
4. Paste into a safe field

**Expected Results:**

- 12 words appear in correct order
- Copying places exact sequence into clipboard

## TC ID: M04

**Scenario:** Confirm seed—happy path  
**Pre-conditions:** Revealed phrase, tapped Next  
**Steps:**

1. On "Confirm your phrase," tap each of the 12 words in correct order
2. Tap Continue

**Expected Results:**

- Transitions to "Set PIN" screen

## TC ID: M06

**Scenario:** PIN set—valid  
**Pre-conditions:** Confirmed seed  
**Steps:**

1. Enter a new 6-digit numeric PIN
2. Re-enter the same PIN
3. Tap Continue

**Expected Results:**

- Dashboard displays with welcome banner
- Wallet appears in list
