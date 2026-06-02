# E2E tests (Maestro)

End-to-end UI tests that drive the **real app** on Android and iOS. Flows are
plain YAML — **no Kotlin or build step** — so QA can read and edit them directly.

- Tool: [Maestro](https://docs.maestro.dev/) (`maestro --version` → 2.6.0+)
- Flows live in [`flows/`](flows); reusable steps in [`subflows/`](subflows).
- Selectors match the app's **visible text** and icon **content descriptions**
  (e.g. `"Goals menu"`), so they need no code changes to the app.

## Install Maestro (one time)

```bash
curl -fsSL "https://get.maestro.mobile.dev" | bash
# then add ~/.maestro/bin to your PATH (the installer prints how)
```

## Run the suite

### Android
```bash
# 1. Build + install the app on a running emulator/device:
./gradlew :androidApp:installDebug
# 2. Run all flows:
./.maestro/run-android.sh
# 2b. Smoke flows only:
./.maestro/run-android.sh --include-tags smoke
```
No emulator yet? `emulator -list-avds` then
`emulator -avd <name>` (binaries under `~/Library/Android/sdk`).

### iOS
```bash
# Build the iosApp scheme in Xcode for a Simulator and run it once, then:
./.maestro/run-ios.sh
```
The same flows run on both platforms; only the app id differs (handled by the
scripts via `APP_ID`).

## Flows

| Flow | Tags | What it checks |
|---|---|---|
| `smoke_launch.yaml` | smoke | App launches; Home/Goals/Settings tabs open |
| `goal_create.yaml` | smoke, goals | Create a goal; it appears in the list |
| `goal_delete.yaml` | regression, goals | Create then delete a goal |
| `workout_nav.yaml` | regression, workouts | Toggle List/Calendar on Home |
| `settings_toggle.yaml` | regression, settings | Toggle Dark Mode; open Workout Icons |

## Editing flows (for QA)

Each step is one line. The common commands:

```yaml
- tapOn: "Some Button Text"      # tap an element by its visible text / description
- inputText: "hello"             # type into the focused field
- assertVisible: "Some Text"     # fail if not on screen
- assertNotVisible: "Some Text"
- back                           # device back button
- runFlow: ../subflows/x.yaml    # reuse a shared sequence
```

To **add a check**: add an `assertVisible:` line with the on-screen text.
To **add a tap**: add a `tapOn:` line with the button's label.

## When a flow fails — debugging

Run with debug output to get per-step **screenshots** + a **view-hierarchy** dump:

```bash
maestro test --debug-output /tmp/maestro-debug .maestro/flows/goal_create.yaml
open /tmp/maestro-debug
```

To see every element/selector currently on screen:

```bash
maestro hierarchy        # text dump of the live screen
maestro studio           # point-and-click inspector; click an element to copy its selector
```

If a `tapOn:`/`assertVisible:` value no longer matches, open `maestro studio`,
click the element, and paste the suggested selector into the flow.

## Notes / gotchas

- **Emulator GPU mode (important):** launch the Android emulator with hardware GPU,
  e.g. `emulator -avd <name> -gpu host`. With the default/software GPU the Compose UI
  can render as a **blank white screen** while `maestro hierarchy` still lists elements —
  taps then land on nothing and every `tapOn` "completes" but nothing happens. If flows
  mysteriously do nothing, check `adb exec-out screencap -p > /tmp/s.png` first.
- Flows start with `runFlow: ../subflows/launch_app.yaml`, which does
  `clearState` — every flow runs from a **clean install**, independent of others.
- The goal Save button is labelled **"Save Goal"** and lives at the bottom of a
  scrollable form, so flows `scrollUntilVisible` before tapping it.
- App back-navigation from a pushed screen (e.g. Workout Icons) returns to the **Goals**
  home root, not the previous tab — assert on what the app actually shows.
- Duplicate on-screen text? Scope a selector with `below:` / `index:`
  (see commented example in `goal_delete.yaml`).
