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

## Design System

### Typography — Three Fonts

The app uses a three-font system matching the CSS design tokens. Fonts are configured per Material3 typography slot in `Theme.kt`.

| Font | Role | Material3 Slots | How to Access |
|---|---|---|---|
| **Space Grotesk** | Display — brand names, screen titles, large headings | `displayLarge`, `headlineLarge`, `titleLarge`, `titleMedium` | `MaterialTheme.typography.titleLarge` etc. |
| **DM Sans** | Body — all UI text, labels, buttons, descriptions | `titleSmall`, `bodyLarge`, `bodyMedium`, `bodySmall`, `labelLarge`, `labelMedium`, `labelSmall` | `MaterialTheme.typography.bodyMedium` etc. |
| **Monospace** | Numbers — stats, dates, times, metadata | None (manual only) | `FontFamily.Monospace` |

**Rule:** Do **not** hardcode `fontFamily` on individual `Text()` calls. Use the semantic `MaterialTheme.typography.*` slot and the correct font is applied automatically.

**Examples:**
```kotlin
// Gets Space Grotesk automatically
Text("Workouts", style = MaterialTheme.typography.titleMedium)

// Gets DM Sans automatically
Text("Morning Run", style = MaterialTheme.typography.bodyMedium)

// Mono for numbers/stats (no Material slot — manual)
Text("24km", fontFamily = FontFamily.Monospace)
Text("07:30", fontFamily = FontFamily.Monospace)
```

### Colors

Colors are defined in `AppColor` (CSS-aligned tokens) and mirrored into `ThemeColor` (legacy names). **Prefer `MaterialTheme.colorScheme` in UI code.**

| Token | Material3 equivalent | Usage |
|---|---|---|
| `AppColor.accent` | `MaterialTheme.colorScheme.primary` | Coral buttons, active states, focus borders |
| `AppColor.bg` | `MaterialTheme.colorScheme.background` | App background |
| `AppColor.surface` | `MaterialTheme.colorScheme.surface` | Cards, sheets, dialogs |
| `AppColor.fg` | `MaterialTheme.colorScheme.onSurface` | Primary text |
| `AppColor.muted` | `MaterialTheme.colorScheme.onSurfaceVariant` | Secondary text, inactive icons |
| `AppColor.border` | `MaterialTheme.colorScheme.outline` | Borders, dividers |
| `AppColor.borderSoft` | `MaterialTheme.colorScheme.outlineVariant` | Subtle separators |
| `AppColor.danger` | `MaterialTheme.colorScheme.error` | Destructive actions |

**Custom tokens** (not in Material3 palette) are accessed via `AppColor` directly:
```kotlin
AppColor.accentHover    // #E85A3A
AppColor.accentSoft     // 10% opacity coral
AppColor.surfaceWarm    // #FFF5F3
```

### Spacing & Radius

```kotlin
AppSpace.s1  // 4.dp
AppSpace.s4  // 16.dp
AppRadius.md // 10.dp
AppRadius.pill // 999.dp
```

### Platform-specific implementations

`expect`/`actual` pattern is used for:
- `DatabaseBuilder` — SQLite path differs per platform
- `platformModule` — platform-level Koin extras
- `Platform` — platform name string
