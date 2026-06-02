# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kotlin Multiplatform (KMP) workout tracking app targeting Android, iOS, and Desktop (JVM). Uses Compose Multiplatform for shared UI across all targets. Web targets (JS/Wasm) exist in the codebase but are currently commented out of the build.

## Build & Run Commands

```bash
# Android
./gradlew :androidApp:assembleDebug

# Desktop
./gradlew :desktopApp:run          # standard run
./gradlew :desktopApp:hotRun --auto  # hot reload

# iOS — open /iosApp in Xcode and run from there
```

## Testing

```bash
./gradlew :shared:testAndroidHostTest   # Android
./gradlew :shared:jvmTest               # Desktop/JVM
./gradlew :shared:iosSimulatorArm64Test # iOS
```

Room database code generation requires KSP to run before tests pass on a clean checkout.

### E2E (Maestro)

End-to-end UI tests live in `.maestro/` and drive the real app on an Android emulator / iOS
simulator. **When writing or running E2E tests, follow [`docs/e2e-testing.md`](docs/e2e-testing.md)** —
it covers conventions (text/`contentDescription` selectors, no `testTag` changes), the authoring
loop, and key gotchas (boot the emulator with `-gpu host`, pass `APP_ID` via `-e`).

```bash
emulator -avd <name> -gpu host &   # hardware GPU required or the UI renders blank
./gradlew :androidApp:installDebug
./.maestro/run-android.sh           # add --include-tags smoke for a subset
```

## Architecture

All business logic and UI live in `shared/` (the KMP module). Platform-specific apps (`androidApp/`, `desktopApp/`, `iosApp/`) are thin entry points that boot Koin and host the shared `App()` composable.

### Layer structure (all in `shared/src/commonMain/`)

| Layer | Package | Notes |
|---|---|---|
| Entities | `entity/` | Room `@Entity` data classes: `Goal`, `Parameter`, `Workout`, `WorkoutParameter` |
| DAOs | `dao/` | Room DAOs; `GoalDao` returns `GoalWithParameter` (JOIN result) |
| Database | `database/` | `AppDatabase` (Room), `DatabaseBuilder` (`expect`/`actual` per platform) |
| Repository | `repository/` | `GoalRepository` — single source of truth, exposes `Flow`s |
| ViewModels | `viewmodel/` | AndroidX `ViewModel` subclasses; UI state modeled as sealed classes |
| Screens | `ui/screens/` | Composable screens receiving ViewModel + Navigator. Follows the **Screen / Content / Preview** pattern (see below). |
| Components | `ui/components/` | Shared composables (buttons, cards, bottom nav) |
| Navigation | `navigation/Navigator.kt` | Custom back-stack navigator backed by `SnapshotStateList<Screen>` |
| DI | `di/KoinModules.kt` | `sharedModule` wires everything; `expect val platformModule` for per-platform extras |

### Navigation

Screens are identified by `sealed class Screen` (in `App.kt`). Navigation uses `androidx.navigation3` with Koin's `koinEntryProvider`. The `Navigator` wraps a `SnapshotStateList<Screen>` and provides `navigate`, `goBack`, `replace`, and `setRoot`.

### Database schema

Four Room entities: `Goal` → `Parameter` (1-to-many), `Goal` → `Workout` (1-to-many), `Workout` ↔ `Parameter` via `WorkoutParameter` junction. `WorkoutParameter` uses sparse typed columns (`value_float`, `value_int`, `value_string`) for polymorphic parameter values.

### DI wiring

Koin is initialized per-platform (`androidMain/di/KoinInit.kt`, `jvmMain/di/PlatformModule.jvm.kt`, etc.). The shared `sharedModule` registers the DB, repository, all ViewModels, the Navigator, and the navigation entry providers using `koin-compose-navigation3`.

### Screen / Content / Preview pattern

Every screen in `ui/screens/` follows a three-part structure:

1. **`*Content`** — `private @Composable`
   - Accepts only plain UI state (e.g. `List<Goal>`, `FormState`) and callbacks (`onAdd`, `onDelete`, `onNavigateToDetail`).
   - **Never** receives a `ViewModel`, `Navigator`, `StateFlow`, or `Flow`.
   - Contains all layout logic (Scaffold, lists, cards, etc.).

2. **`*Screen`** — `public @Composable`
   - Accepts the `ViewModel` (and `Navigator` if needed).
   - Collects from `StateFlow`s / observes UI state.
   - Wires ViewModel actions and navigation into plain callbacks, then delegates to `*Content`.

3. **`*Preview`** — `private @Composable @Preview`
   - Calls the private `*Content` with fake/static data.
   - No repository, database, or ViewModel required.

**Example:**
```kotlin
@Composable
fun GoalIndexScreen(viewModel: GoalIndexViewModel, navigator: Navigator) { /* collects + wires */ }

@Composable
private fun GoalIndexContent(activeGoals: List<Goal>, newGoals: List<Goal>, ...) { /* UI only */ }

@Preview
@Composable
private fun GoalIndexPreview() { GoalIndexContent(fakeGoals, fakeGoals) }
```

## Design Guidelines

A comprehensive design spec lives in [`DESIGN.md`](DESIGN.md). It covers the full color palette (light + dark), typography scale, spacing, radius, component patterns (buttons, cards, inputs, nav), motion, and iconography. **For any UI work, consult `DESIGN.md` first** — it is the source of truth for visual decisions and takes precedence over the summary below.

## Design System

Design tokens, color palette, typography scale, spacing, and component patterns are fully specified in [`DESIGN.md`](DESIGN.md).

### Kotlin/Compose usage rules

- **Typography:** use `MaterialTheme.typography.*` slots — never hardcode `fontFamily`. Numbers/stats use `FontFamily.Monospace` (no Material slot).
- **Colors:** prefer `MaterialTheme.colorScheme.*`; access custom tokens via `AppColor` directly. `ThemeColor` is a legacy alias — avoid.
- **Spacing/Radius:** use `AppSpace.*` and `AppRadius.*`.

### Platform-specific implementations

`expect`/`actual` pattern is used for:
- `DatabaseBuilder` — SQLite path differs per platform
- `platformModule` — platform-level Koin extras
- `Platform` — platform name string
