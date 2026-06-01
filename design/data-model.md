# Data Model & Architecture — Deep Technical Reference

This document specifies the data schema, persistence layer, seed data strategy, and helper functions that power the Workout Tracker prototype.

---

## Entity Relationship

```
┌──────────────┐        ┌──────────────────┐        ┌────────────────────────┐
│     Goal     │ 1────* │    Parameter     │        │                        │
│              │        │                  │        │   WorkoutParameter     │
│  id          │        │  id              │  ┌───> │   (inline key:value)   │
│  name        │        │  name            │  │     │                        │
│  status      │        │  type            │  │     │   paramId → value      │
│  icon        │        │  unit            │  │     └────────────────────────┘
│  color       │        │  goalId (FK)     │  │                ▲
│  startAt     │        └──────────────────┘  │                │
│  completedAt │                              │     ┌──────────┴─────────────┐
│  createdAt   │ 1────────────────────────────*     │      Workout           │
│  parameters[]│                                    │                        │
└──────────────┘                                    │  id                    │
                                                    │  goalId (FK)           │
                                                    │  label                 │
                                                    │  date                  │
                                                    │  time                  │
                                                    │  duration              │
                                                    │  notes                 │
                                                    │  values {}             │
                                                    └────────────────────────┘
```

### Relationships

| Relationship          | Type   | Description                                              |
|-----------------------|--------|----------------------------------------------------------|
| Goal → Parameter      | 1 : *  | A goal defines N parameters (metrics to track)           |
| Goal → Workout        | 1 : *  | Each workout is logged against exactly one goal           |
| Workout → values      | 1 : 1  | `values` is an inline object mapping `paramId` → value   |

There is no standalone `WorkoutParameter` table — parameter values are stored as a flat `{ [paramId]: value }` object on each `Workout`. This denormalized shape keeps the prototype simple while preserving the logical relationship.

---

## Entity Schemas

### Goal

| Field         | Type     | Constraints              | Description                                      |
|---------------|----------|--------------------------|--------------------------------------------------|
| `id`          | integer  | Auto-increment, unique   | Primary key                                      |
| `name`        | string   | Required, non-empty      | Display name (e.g., "Half Marathon Prep")         |
| `status`      | enum     | `"active"` \| `"completed"` | Lifecycle state                               |
| `cat`         | string   | Legacy, optional         | Old category key (e.g., `"run"`, `"arm"`, `"walk"`) — used for backward compat in `goalColor()` / `goalIconName()` |
| `icon`        | string   | One of `GOAL_ICONS[].id` | Activity icon key (e.g., `"run"`, `"strength"`)  |
| `color`       | string   | Hex from `GOAL_COLORS`   | Display color (e.g., `"#0D9488"`)                |
| `startAt`     | ISO date | `YYYY-MM-DD`             | When the goal was started or scheduled            |
| `completedAt` | ISO date | Nullable                 | Set when `status` changes to `"completed"`        |
| `createdAt`   | ISO date | `YYYY-MM-DD`             | Creation timestamp                                |
| `parameters`  | array    | Inline `Parameter[]`     | Embedded array of parameter definitions           |

**Status transitions:**
```
new → active → completed
         ↑          │
         └──────────┘  (reactivate)
```

Status is set directly via the toggle group on the goal detail screen. There is no "new" status in practice — goals are created as `"active"`.

### Parameter

Parameters are embedded inside the `Goal.parameters` array, not stored separately.

| Field  | Type   | Constraints                       | Description                               |
|--------|--------|-----------------------------------|-------------------------------------------|
| `id`   | integer| Unique across all parameters      | Primary key (used as key in `Workout.values`) |
| `name` | string | Required                          | Display name (e.g., "Distance", "Sets")   |
| `type` | enum   | `"number"` \| `"integer"` \| `"text"` | Controls input type and validation    |
| `unit` | string | Optional                          | Display unit (e.g., "km", "bpm", "/10")   |

**Parameter types:**

| Type       | Input          | Validation                    | Display                  |
|------------|----------------|-------------------------------|--------------------------|
| `number`   | `type="number"` step="any" | Numeric, decimals OK   | Raw number + unit        |
| `integer`  | `type="number"` step="1"   | Whole numbers only     | Raw integer + unit       |
| `text`     | `type="text"`              | Any string             | Raw text + unit          |

### Workout

| Field      | Type     | Constraints           | Description                                        |
|------------|----------|-----------------------|----------------------------------------------------|
| `id`       | integer  | Auto-increment, unique| Primary key                                        |
| `goalId`   | integer  | FK → Goal.id          | The goal this workout was logged against            |
| `label`    | string   | Required              | User-defined session label (e.g., "Morning Run")   |
| `date`     | ISO date | `YYYY-MM-DD`          | Date of the workout                                |
| `time`     | string   | `HH:MM` 24h format   | Start time of the workout                          |
| `duration` | integer  | Minutes               | Total workout duration                             |
| `notes`    | string   | Optional              | Free-text notes                                    |
| `values`   | object   | `{ [paramId]: value }`| Parameter values, keyed by `Parameter.id`          |

**`values` example:**
```json
{
  "11": 5.2,    // Distance (km)
  "12": 5.7,    // Pace (min/km)
  "13": 156     // Heart Rate (bpm)
}
```

Keys are stringified `Parameter.id` values. Values match the parameter's `type` — numbers for `number`/`integer`, strings for `text`.

---

## Persistence

### Storage Mechanism

The prototype uses `localStorage` with a single key for the entire data store.

| Constant    | Value                 | Description                         |
|-------------|-----------------------|-------------------------------------|
| `STORE_KEY` | `"workout-proto-v1"`  | localStorage key for all app data   |

### Data Structure in Storage

```typescript
interface DataStore {
  goals: Goal[];
  workouts: Workout[];
  nextGoalId: number;       // Auto-increment counter for goals
  nextParamId: number;      // Auto-increment counter for parameters
  nextWorkoutId: number;    // Auto-increment counter for workouts
  enabledIcons: string[];   // Array of enabled icon IDs from GOAL_ICONS
}
```

### Functions

| Function       | Signature           | Description                                         |
|----------------|---------------------|-----------------------------------------------------|
| `loadData()`   | `() → DataStore`    | Reads from localStorage, falls back to `seed()`.    |
| `saveData(d)`  | `(DataStore) → void`| Serializes entire store to localStorage.             |
| `resetData()`  | `() → void`         | Removes the storage key (next load gets fresh seed). |

**Read-on-mount pattern:** `loadData()` is called once in `App.jsx`'s `useState` initializer. The returned object is held in React state (`data`) and passed to all child components via props.

**Write-on-mutate pattern:** Every mutation (add/edit/delete goal, log workout, toggle setting) creates a new `data` object via spread, updates React state, and calls `saveData(newData)` synchronously.

```javascript
// Typical mutation pattern:
const newData = { ...data, goals: [...data.goals, newGoal], nextGoalId: data.nextGoalId + 1 };
setData(newData);
saveData(newData);
```

### Preferences (Separate Key)

User preferences are stored under a separate localStorage key:

| Key              | Value                    | Description                      |
|------------------|--------------------------|----------------------------------|
| `"workout-prefs"`| `{ darkMode, defaultView }` | Theme + default home view     |

| Preference    | Type    | Default    | Description                             |
|---------------|---------|------------|-----------------------------------------|
| `darkMode`    | boolean | `false`    | Toggles `data-theme="dark"` on `.phone-app` |
| `defaultView` | string  | `"list"`   | Default home screen view (`"list"` or `"calendar"`) |

---

## Seed Data

The `seed()` function returns a fresh `DataStore` with pre-populated demo data. It's used on first load (no localStorage) and after `resetData()`.

### Seed Goals (4)

| ID | Name                 | Status    | Icon       | Color     | Parameters                        |
|----|----------------------|-----------|------------|-----------|-----------------------------------|
| 1  | Half Marathon Prep   | active    | run        | #0D9488   | Distance (km), Pace (min/km), Heart Rate (bpm) |
| 2  | Daily 5K             | active    | walk       | #059669   | Distance (km), Pace (min/km)      |
| 3  | Upper Body Strength  | active    | strength   | #7C3AED   | Sets, Reps, Weight (kg), RPE (/10)|
| 4  | Couch to 5K          | completed | run        | #0D9488   | Distance (km), Pace (min/km)      |

### Seed Workouts (12)

All workouts are dated between May 2–31, 2026, distributed across 3 active goals:

- **Half Marathon Prep (goal 1):** 5 workouts — morning runs with progressive distance (5.2 → 12.4 km)
- **Daily 5K (goal 2):** 2 workouts — evening walks at a relaxed pace
- **Upper Body Strength (goal 3):** 4 workouts — arm days with progressive sets/weight
- **Couch to 5K (goal 4, completed):** 0 workouts in seed (historical goal, data implied)

### Deterministic "Now"

```javascript
const TODAY = new Date(2026, 4, 31); // May 31, 2026
```

The prototype freezes "today" to a specific date so seed data is always positioned correctly relative to the current view. All date helpers (`dayDiff`, `relDate`, `recentWeekStart`) reference `TODAY`, not `new Date()`.

---

## ID Generation

IDs are monotonically increasing integers managed by counters in the data store.

| Counter         | Initial (Seed) | Scope          |
|-----------------|-----------------|----------------|
| `nextGoalId`    | 1025            | Goal IDs       |
| `nextParamId`   | 1000            | Parameter IDs  |
| `nextWorkoutId` | 200             | Workout IDs    |

**Why the gaps?** Seed goals use IDs 1–4, parameters 11–42, workouts 101–112. The counters start well above these ranges to avoid collisions with seed data while keeping IDs human-readable.

**Thread safety:** Not applicable — the prototype runs single-threaded in the browser. If ported to a backend, replace with database-generated IDs (UUIDs or auto-increment PKs).

---

## Date Helpers

All date operations use a small set of pure functions defined in `data.jsx`. No external date library is used.

| Function            | Signature                | Returns              | Description                                     |
|---------------------|--------------------------|----------------------|-------------------------------------------------|
| `parseD(iso)`       | `(string) → Date`       | `Date`               | Parses `YYYY-MM-DD` to a local-time `Date`      |
| `isoOf(dt)`         | `(Date) → string`       | `"YYYY-MM-DD"`       | Formats a `Date` to ISO date string             |
| `fmtDate(iso)`      | `(string) → string`     | `"May 31"`           | Short display format (month + day)              |
| `fmtLong(iso)`      | `(string) → string`     | `"May 31, 2026"`     | Full display format (month + day + year)        |
| `dayDiff(iso)`      | `(string) → number`     | Integer              | Days between `iso` and `TODAY` (negative = past)|
| `relDate(iso)`      | `(string) → string`     | `"Today"` / `"3d ago"` / `"May 28"` | Human-relative date string |
| `startOfWeek(dt)`   | `(Date) → Date`         | `Date` (Sunday)      | Sunday of the week containing `dt`              |
| `recentWeekStart()` | `() → Date`             | `Date`               | 7 days ago from `TODAY` (rolling window)        |

### `relDate()` Logic

```
dayDiff ==  0  → "Today"
dayDiff == -1  → "Yesterday"
dayDiff > -7   → "Nd ago" (e.g., "3d ago")
otherwise      → fmtDate (e.g., "May 28")
```

Future dates are not handled (always show `fmtDate`).

---

## Metric Helpers

### `metricSummary(workout, goal)`

Produces a compact string of the workout's key metrics for display in list items and calendar entries.

**Algorithm:**
1. Iterate `goal.parameters` in order.
2. For each parameter with a non-null value in `workout.values`:
   - If `name === "Pace"`: format with `paceStr()` (e.g., `"5:42 min/km"`)
   - If `name === "Sets"` or `name === "Reps"`: skip (combined below)
   - Otherwise: raw value + unit (e.g., `"5.2 km"`)
3. If both Sets and Reps exist, prepend `"4×10"` format.
4. Return at most 2 metrics joined with `" · "`.

**Example outputs:**
- Running workout: `"5.2 km · 5:42 /km"`
- Strength workout: `"4×10 · 38 kg"`
- Walking workout: `"3.1 km · 9:00 /km"`

### `paceStr(decimalMinutes)`

Converts a decimal pace value to `M:SS` format.

```javascript
paceStr(5.7) → "5:42"
// 5 minutes + (0.7 × 60 = 42 seconds)
```

---

## Data Flow Diagram

```
┌─────────────────────────────────────────────────┐
│                   App.jsx                        │
│                                                  │
│  const [data, setData] = useState(loadData())    │
│                                                  │
│  ┌────────────────────────────────────────────┐  │
│  │  Mutation handler (e.g. addGoal):          │  │
│  │    const newData = { ...data, ... }        │  │
│  │    setData(newData)  ──→ React re-render   │  │
│  │    saveData(newData) ──→ localStorage      │  │
│  └────────────────────────────────────────────┘  │
│                                                  │
│  data passed as props to:                        │
│    Home.jsx    (data.workouts, data.goals)        │
│    Goals.jsx   (data.goals, data.workouts)        │
│    Forms.jsx   (data.goals, mutation callbacks)   │
│    Settings.jsx (data, resetData callback)        │
└─────────────────────────────────────────────────┘
         │                       ▲
         ▼                       │
┌─────────────────┐    ┌────────────────┐
│  localStorage   │    │    seed()       │
│                 │    │  (first load /  │
│ workout-proto-v1│    │   after reset)  │
│ workout-prefs   │    └────────────────┘
└─────────────────┘
```

---

## Production Migration Notes

If this prototype is implemented as a production app, the following schema changes are recommended (from `model_structure.md`):

1. **Normalize `WorkoutParameter`** into its own table with `(workoutId, parameterId, value)` rows instead of a flat JSON object. This enables efficient querying ("all pace values for goal X").
2. **Add `User` entity** with authentication. All goals and workouts become scoped to a user.
3. **Replace integer IDs with UUIDs** for offline-first sync compatibility.
4. **Add `updatedAt` timestamps** to all entities for conflict resolution.
5. **Add `deletedAt` soft-delete** instead of hard deletion.
6. **Move `enabledIcons` to a `UserPreferences` table** alongside `darkMode` and `defaultView`.
7. **Add indexes** on `Workout.goalId`, `Workout.date`, and `Goal.status` for list/calendar query performance.
