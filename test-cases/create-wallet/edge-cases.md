# Edge Case Test Cases for Wallet Creation

## TC ID: M03

**Scenario:** Prevent skip backup  
**Pre-conditions:** On seed screen, no confirm  
**Steps:**

1. Tap Next without saving or confirming

**Expected Results:**

- Inline warning: "Please back up your recovery phrase before continuing."

## TC ID: M05

**Scenario:** Confirm seed—wrong order  
**Pre-conditions:** On confirm screen  
**Steps:**

1. Tap words out of sequence
2. Tap Continue

**Expected Results:**

- Error tooltip: "The phrase you entered is incorrect. Please try again."

## TC ID: M07

**Scenario:** PIN set—non-numeric  
**Pre-conditions:** On PIN entry  
**Steps:**

1. Try typing letters or symbols into PIN field

**Expected Results:**

- Only digits accepted
- Invalid keys ignored

## TC ID: M08

**Scenario:** PIN set—mismatch  
**Pre-conditions:** On PIN confirm  
**Steps:**

1. Enter two different PINs
2. Tap Continue

**Expected Results:**

- Inline error: "PINs do not match. Please try again."

## TC ID: M09

**Scenario:** Back-button navigation  
**Pre-conditions:** At each sub-screen  
**Steps:**

1. Press device Back at T&C, seed reveal, confirm seed, PIN screens

**Expected Results:**

- Proper back navigation (seed → T&C → exit app on first)
- No crashes or data loss at each step
