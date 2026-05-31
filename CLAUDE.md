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
| Screens | `ui/screens/` | Composable screens receiving ViewModel + Navigator |
| Components | `ui/components/` | Shared composables (buttons, cards, bottom nav) |
| Navigation | `navigation/Navigator.kt` | Custom back-stack navigator backed by `SnapshotStateList<Screen>` |
| DI | `di/KoinModules.kt` | `sharedModule` wires everything; `expect val platformModule` for per-platform extras |

### Navigation

Screens are identified by `sealed class Screen` (in `App.kt`). Navigation uses `androidx.navigation3` with Koin's `koinEntryProvider`. The `Navigator` wraps a `SnapshotStateList<Screen>` and provides `navigate`, `goBack`, `replace`, and `setRoot`.

### Database schema

Four Room entities: `Goal` → `Parameter` (1-to-many), `Goal` → `Workout` (1-to-many), `Workout` ↔ `Parameter` via `WorkoutParameter` junction. `WorkoutParameter` uses sparse typed columns (`value_float`, `value_int`, `value_string`) for polymorphic parameter values.

### DI wiring

Koin is initialized per-platform (`androidMain/di/KoinInit.kt`, `jvmMain/di/PlatformModule.jvm.kt`, etc.). The shared `sharedModule` registers the DB, repository, all ViewModels, the Navigator, and the navigation entry providers using `koin-compose-navigation3`.

### Platform-specific implementations

`expect`/`actual` pattern is used for:
- `DatabaseBuilder` — SQLite path differs per platform
- `platformModule` — platform-level Koin extras
- `Platform` — platform name string
