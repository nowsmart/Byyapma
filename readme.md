# BüyüYapma (Spell Generator App)

**BüyüYapma** is a Jetpack Compose-based Android application that generates customized text patterns (spells/phrases) based on user input, dynamic text lists (`ebarat`), and named variables (`talibName` and `talibMotherName`).

---

## Features

* **Dynamic Text Input Fields:** Dynamically appends a new text field as you type into the last input field.
* **RTL (Right-to-Left) UI Layout:** Renders primary and secondary results using RTL layout orientation for proper display of Arabic/Urdu scripts.
* **Interleaving Algorithm:** Interleaves input characters index-by-index, pads the sequence, chunks the resulting string into triplets, and appends suffix values.
* **Persistent Storage:** Uses `SharedPreferences` (`ByyapmaPrefs`) to save user input across sessions.
* **Clipboard Support:** One-tap copy button to copy generated combined strings.
* **Modern UI:** Built using **Jetpack Compose** and **Material 3** components, supporting Edge-to-Edge display and scrolling.

---

## Technical Stack

| Component | Technology |
| --- | --- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Persistence** | Android `SharedPreferences` |
| **Minimum SDK** | Configured in `build.gradle.kts` |
| **Architecture** | Single-Activity Architecture (`ComponentActivity`) |

---

## How It Works

1. **Input Collection:**
* **Target Name (`talibName`):** Saved under key `saved_talib_name`.
* **Mother's Name (`talibMotherName`):** Saved under key `saved_talib_mother_name`.
* **Text List (`ebarat`):** Array of text entries saved as pipe-separated strings (`|`) under key `saved_ebarat`.


2. **Calculation (`calculateResult`):**
* Removes spaces and interleaves characters across all entries in `ebarat`.
* Pads the interleaved output to make its length divisible by 3.
* Chunks characters into triplets, appending `yoosh` suffix to each chunk.
* Generates both standard and doubled result variants.


3. **Spell Generation (`createSpell`):**
* Substitutes placeholder tokens (`فُلَانِ ` and `فُلَانَةَ `) in `spellBase` with the provided `talibName` and `talibMotherName`.



---

## Setup & Installation

1. **Clone the Repository:**
```bash
git clone https://github.com/your-username/BuyuYapma.git
cd BuyuYapma

```


2. **Open in Android Studio:**
* Open Android Studio (Ladybug or newer recommended).
* Select **Open an Existing Project** and choose the cloned repository root.


3. **Build & Run:**
* Sync Gradle files.
* Select an emulator or connected physical Android device.
* Press **Run** (`Shift + F10`).