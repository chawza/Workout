# Colors — Deep Technical Reference

This document is the exhaustive color specification for the Workout Tracker app. It maps every token to its CSS custom property, documents the exact values for both light and dark themes, defines the goal-category palette, and specifies usage rules with WCAG contrast context.

## Light Theme Tokens

All colors are defined as CSS custom properties on `:root` in `css/app.css`.

### Core Neutrals

| Token            | CSS Variable      | Value     | Role                                                                 |
|------------------|--------------------|-----------|----------------------------------------------------------------------|
| background       | `--bg`             | `#F8F9FB` | App canvas. Applied to `body`, `.app`, `.content`, `.appbar`.        |
| surface          | `--surface`        | `#FFFFFF` | Cards, sheets, input backgrounds when elevated, sidebar background.  |
| surface-warm     | `--surface-warm`   | `#FFF5F3` | Calendar "today" cell, contextual warm highlights.                   |
| foreground       | `--fg`             | `#111827` | Primary text — headings, body copy, high-emphasis labels.            |
| foreground-2     | `--fg-2`           | `#374151` | Secondary text — button labels, supporting copy, sidebar links.      |
| muted            | `--muted`          | `#6B7280` | Tertiary text — metadata, timestamps, captions, placeholders.        |
| border           | `--border`         | `#E5E7EB` | Structural borders — cards, inputs, calendar grid, sheet dividers.   |
| border-soft      | `--border-soft`    | `#F3F4F6` | Subtle separators — list item dividers, internal card separators.    |

### Accent

| Token            | CSS Variable       | Value                          | Role                                                   |
|------------------|---------------------|--------------------------------|--------------------------------------------------------|
| accent           | `--accent`          | `#FF6B4A`                      | Primary CTA fills, active nav, FAB, focus rings.       |
| accent-on        | `--accent-on`       | `#FFFFFF`                      | Text/icon color on accent backgrounds.                 |
| accent-soft      | `--accent-soft`     | `rgba(255, 107, 74, 0.10)`    | Hover backgrounds for ghost buttons, active nav tint.  |
| accent-hover     | `--accent-hover`    | `#E85A3A`                      | Primary button hover state.                            |
| accent-active    | `--accent-active`   | `#D14E30`                      | Primary button pressed state (not yet wired).          |
| meta             | `--meta`            | `#FF6B4A`                      | Alias of accent. Used in list-item trailing metadata.  |

### Semantic

| Token   | CSS Variable  | Value     | Role                                              |
|---------|---------------|-----------|---------------------------------------------------|
| success | `--success`   | `#10B981` | Active badge background (10% opacity), positive Δ. |
| warn    | `--warn`      | `#F59E0B` | Reserved for future caution states.                |
| danger  | `--danger`    | `#EF4444` | Delete buttons, validation errors, negative Δ.     |

### Contrast Ratios (Light Theme)

| Pair                         | Ratio   | WCAG AA (4.5:1) | WCAG AAA (7:1) |
|------------------------------|---------|------------------|----------------|
| `--fg` on `--bg`             | 14.8:1  | ✅ Pass          | ✅ Pass        |
| `--fg-2` on `--bg`           | 9.5:1   | ✅ Pass          | ✅ Pass        |
| `--muted` on `--bg`          | 5.0:1   | ✅ Pass          | ❌ Fail        |
| `--muted` on `--surface`     | 4.6:1   | ✅ Pass          | ❌ Fail        |
| `--accent` on `--surface`    | 3.4:1   | ❌ Fail (large text OK) | ❌ Fail |
| `--accent-on` on `--accent`  | 3.4:1   | ❌ Fail (large text OK) | ❌ Fail |
| `--fg` on `--surface`        | 16.0:1  | ✅ Pass          | ✅ Pass        |

> **Note:** The coral accent (#FF6B4A) does not pass WCAG AA for normal-sized text on white. This is acceptable because accent-colored text is always used at ≥14px bold (buttons, nav labels, badge-like elements), which qualifies as "large text" under WCAG (3:1 minimum). Body text is never set in the accent color.

---

## Dark Theme Tokens

Dark mode is activated by setting `data-theme="dark"` on the `.phone-app` container. The theme overrides CSS custom properties, keeping the same variable names so all components adapt automatically.

| Token            | Light Value                     | Dark Value                      | Notes                                      |
|------------------|---------------------------------|---------------------------------|--------------------------------------------|
| `--bg`           | `#F8F9FB`                       | `#0F1115`                       | Near-black with slight blue undertone.     |
| `--surface`      | `#FFFFFF`                       | `#181B20`                       | Dark gray, not pure black.                 |
| `--surface-warm` | `#FFF5F3`                       | `#241A16`                       | Warm dark brown for "today" calendar cell. |
| `--fg`           | `#111827`                       | `#F3F4F6`                       | Near-white for primary text.               |
| `--fg-2`         | `#374151`                       | `#C9CDD3`                       | Light gray for secondary text.             |
| `--muted`        | `#6B7280`                       | `#8A9099`                       | Slightly lighter to maintain readability.  |
| `--border`       | `#E5E7EB`                       | `#2A2E35`                       | Dark border — subtle on dark surfaces.     |
| `--border-soft`  | `#F3F4F6`                       | `#20242B`                       | Near-invisible separator.                  |
| `--accent`       | `#FF6B4A`                       | `#E2674A`                       | Muted coral — less neon on dark canvas.    |
| `--accent-hover` | `#E85A3A`                       | `#EC7558`                       | Lighter hover on dark (inverse direction). |
| `--accent-soft`  | `rgba(255, 107, 74, 0.10)`     | `rgba(226, 103, 74, 0.18)`     | Higher opacity to remain visible on dark.  |
| `--meta`         | `#FF6B4A`                       | `#E2674A`                       | Tracks accent.                             |

### Additional Dark Overrides

```css
/* Calendar other-month cells */
.phone-app[data-theme="dark"] .calendar .day.other-month {
  background: #14171C;
}

/* Toggle group active shadow needs more weight */
.phone-app[data-theme="dark"] .toggle-group button.active {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
}
```

Dark mode is toggled via `Settings > Dark Mode`, stored in `localStorage` under `workout-prefs`, and read at app initialization via `loadPrefs()`.

---

## Goal Category Palette

Each goal selects one color from this fixed 10-color palette. The color is stored on the `Goal` object as a hex string and resolved at render time.

| ID     | Hex       | sRGB                  | Default mapping        |
|--------|-----------|-----------------------|------------------------|
| teal   | `#0D9488` | rgb(13, 148, 136)     | Running                |
| green  | `#059669` | rgb(5, 150, 105)      | Walking                |
| blue   | `#3B82F6` | rgb(59, 130, 246)     | Swimming               |
| indigo | `#6366F1` | rgb(99, 102, 241)     | General / default      |
| purple | `#7C3AED` | rgb(124, 58, 237)     | Strength               |
| pink   | `#EC4899` | rgb(236, 72, 153)     | Yoga                   |
| red    | `#EF4444` | rgb(239, 68, 68)      | Cardio                 |
| orange | `#F97316` | rgb(249, 115, 22)     | Cycling                |
| amber  | `#F59E0B` | rgb(245, 158, 11)     | Reserved               |
| slate  | `#64748B` | rgb(100, 116, 139)    | Neutral / miscellaneous|

### Goal Color Application

Colors are applied through three helper functions in `data.jsx`:

```
goalColor(goal)  → hex string (e.g. "#0D9488")
goalBg(goal)     → "color-mix(in srgb, #0D9488 14%, transparent)"
goalIconName(goal) → icon key (e.g. "run")
```

**Usage patterns:**
- **Icon container background:** `goalBg(goal)` — 14% opacity tint
- **Icon stroke color:** `goalColor(goal)` — full saturation
- **Calendar entry chip:** CSS custom property `--entry-color` set inline, consumed by `color-mix` in the stylesheet
- **Goal selector chips (log-workout):** Full-color icon + accent border when selected
- **Sparkline stroke:** Full-saturation goal color

### Auto-assignment (`guessIconColor`)

When creating a new goal, the name is matched against keyword patterns to auto-assign an icon + color pair:

| Pattern                                    | Icon       | Color     |
|--------------------------------------------|------------|-----------|
| swim, pool, lap                            | swim       | #3B82F6   |
| bike, cycl, ride, spin                     | bike       | #F97316   |
| yoga, stretch, flex, pilat                 | yoga       | #EC4899   |
| walk, hike, step                           | walk       | #059669   |
| strength, arm, lift, gym, weight, push, pull, muscle | strength | #7C3AED |
| cardio, hiit, burn, circuit                | flame      | #EF4444   |
| run, jog, sprint, marathon, 5k, 10k       | run        | #0D9488   |
| *(fallback)*                               | target     | #6366F1   |

The auto-assignment is overridden if the user manually picks an icon/color via the `IconColorPicker` sheet.

---

## Color Usage Rules

1. **Accent is for interaction only.** Never use `--accent` for informational text, backgrounds, or decoration. It marks tappable, actionable elements.
2. **One accent action per screen.** Each screen should have at most one primary-colored CTA. Secondary actions use `btn-secondary` or `btn-ghost`.
3. **Goal colors are scoped.** They appear only within goal-related contexts (goal cards, calendar entries, detail screens, workout rows). They never leak into navigation, settings, or structural UI.
4. **Semantic colors are semantic.** `--success` = positive/active, `--danger` = destructive/error, `--warn` = caution. Never repurpose them for decoration.
5. **Dark mode uses the same variables.** Components should always reference `var(--token)`, never hardcode hex values. The theme switch overrides the variables automatically.
6. **Opacity for tints.** Use `color-mix(in srgb, {color} N%, transparent)` or `rgba()` for tinted backgrounds. Never lighten a color by mixing with white — use transparency over the existing background.
