# E2E Testing Guide (Maestro)

How to write, run, and debug end-to-end UI tests for this app. The suite lives in
[`.maestro/`](../.maestro); each flow is plain YAML and drives the **real app** on an
Android emulator or iOS simulator. See [`.maestro/README.md`](../.maestro/README.md) for
the QA-facing run instructions; this doc is the deeper how-to for adding tests.

## Prerequisites

```bash
# Maestro CLI (one time)
curl -fsSL "https://get.maestro.mobile.dev" | bash   # adds ~/.maestro/bin

# Android tooling lives under ~/Library/Android/sdk
export PATH="$HOME/.maestro/bin:$HOME/Library/Android/sdk/platform-tools:$HOME/Library/Android/sdk/emulator:$PATH"
```

## Running

```bash
# Android: boot an emulator WITH HARDWARE GPU, install, run.
emulator -avd <name> -gpu host &      # see gotcha #1 below
./gradlew :androidApp:installDebug
./.maestro/run-android.sh                       # all flows
./.maestro/run-android.sh --include-tags smoke  # subset by tag

# iOS: build the iosApp scheme onto a booted simulator from Xcode, then:
./.maestro/run-ios.sh
```

## Layout & conventions

```
.maestro/
  config.yaml          # globs flows/*.yaml
  flows/*.yaml         # one user journey per file; tagged smoke / regression
  subflows/*.yaml      # reusable steps, included via `runFlow:`
  run-android.sh       # sets APP_ID=com.nabeelkm.workout, passes it with -e
  run-ios.sh           # sets APP_ID=com.nabeelkm.workout.Workout
```

- **Cross-platform `appId`:** every flow starts with `appId: ${APP_ID}`. The same YAML runs
  on both platforms; only the id differs (handled by the runner scripts).
- **Clean state:** flows begin with `runFlow: ../subflows/launch_app.yaml`, which does
  `clearState` + `launchApp`, so each flow is independent and starts from a fresh install.
- **Selectors (this app's rule):** match on **visible text** and icon **`contentDescription`**
  only — *do not add `testTag`/`testTagsAsResourceId` to Compose code*. The screens already
  expose good anchors, e.g. bottom-nav `"Home menu"`/`"Goals menu"`/`"Settings menu"`,
  buttons like `"+ New Goal"` / `"Save Goal"`, and titles like `"Goals"` / `"Settings"`.

## Authoring loop

1. Boot the app (see above) and run `maestro studio` — a browser inspector. Click an element
   to copy a working selector.
2. Or dump the live tree: `maestro hierarchy` (every matchable `text` / `accessibilityText`).
3. Write/adjust the flow, then `maestro test -e APP_ID=com.nabeelkm.workout .maestro/flows/<x>.yaml`.
4. On failure, add `--debug-output /tmp/m` for per-step **screenshots + hierarchy dumps**:
   `maestro test --debug-output /tmp/m .maestro/flows/<x>.yaml && open /tmp/m`.

## Common commands

```yaml
- tapOn: "Button Text"             # by visible text / contentDescription
- inputText: "hello"               # into focused field (tapOn a field first)
- assertVisible: "Some Text"
- assertNotVisible: "Some Text"
- back
- hideKeyboard
- scrollUntilVisible:              # for controls below the fold
    element: { text: "Save Goal" }
    direction: DOWN
- runFlow: ../subflows/launch_app.yaml
- tapOn: { text: "Delete", below: "Temp Goal" }   # disambiguate duplicate text
```

## Gotchas (learned the hard way — check these first when a flow misbehaves)

1. **Blank UI / taps do nothing → emulator GPU.** With the default/software GPU the Compose
   UI renders as a **blank white screen** while `maestro hierarchy` still lists elements, so
   every `tapOn` "completes" but nothing happens. Always launch with `emulator -avd <name> -gpu host`.
   Sanity-check the real frame with `adb exec-out screencap -p > /tmp/s.png` (Maestro's own
   screenshots also come out blank here, so don't trust those).
2. **`${APP_ID}` shows as `undefined` → pass with `-e`.** Maestro does *not* read shell env
   for `${...}` in flows. Use `-e APP_ID=...` (the runner scripts already do this); exporting
   the shell var alone fails with "Unable to launch app undefined".
3. **Save button is `"Save Goal"`** (not "Save") and sits at the bottom of a scrollable form —
   `scrollUntilVisible` before tapping.
4. **Back-navigation** from a pushed screen (e.g. Workout Icons) returns to the **Goals** home
   root, not the previous tab. Assert on what the app actually shows, not what you expect.

## Adding a new flow

1. Copy an existing file in `flows/` as a template; keep the `appId: ${APP_ID}` header and the
   `runFlow: ../subflows/launch_app.yaml` first step.
2. Tag it (`smoke` for fast critical-path, `regression` otherwise).
3. Use real on-screen labels (verify with `maestro hierarchy`).
4. Run it green on Android before committing. Update [`.maestro/README.md`](../.maestro/README.md)
   if it adds a new journey worth listing for QA.
