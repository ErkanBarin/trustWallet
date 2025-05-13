# Non-Functional Test Cases for Wallet Creation

## TC ID: M10

**Scenario:** Offline / flaky network  
**Pre-conditions:** Airplane mode / no net  
**Steps:**

1. Launch app
2. Try to open T&C link
3. Complete offline seed flow

**Expected Results:**

- App opens
- T&C link shows "No Internet"
- Wallet creation works fully offline

## TC ID: M11

**Scenario:** Orientation & layout integrity  
**Pre-conditions:** Any screen  
**Steps:**

1. Rotate device portrait↔landscape

**Expected Results:**

- UI reflows with no truncation
- Buttons remain tappable
- No crashes

## TC ID: M12

**Scenario:** Accessibility (TalkBack)  
**Pre-conditions:** Android TalkBack on  
**Steps:**

1. Navigate each step with TalkBack

**Expected Results:**

- All controls are announced
- Logical focus order
- Clear labels

## TC ID: M13

**Scenario:** Performance (cold start)  
**Pre-conditions:** Fresh install  
**Steps:**

1. Time from app icon to first screen
2. Time between each screen

**Expected Results:**

- All transitions <3 sec
- No ANRs

## TC ID: M14

**Scenario:** Security—clipboard auto-clear or warning  
**Pre-conditions:** After seed copy  
**Steps:**

1. Copy seed
2. Wait 30 sec
3. Paste

**Expected Results:**

- Clipboard auto-cleared or OS warns "Sensitive data copied"
- No seed remains indefinitely
