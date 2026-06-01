# Iconography — Deep Technical Reference

This document specifies the icon system: rendering approach, the full icon inventory, the goal-icon subsystem, sizing conventions, and rules for adding new icons.

---

## Rendering Approach

All icons are **inline SVGs rendered as React components** via the `Icon` component in `data.jsx`. There are no icon font files, no sprite sheets, and no external icon libraries.

```jsx
<Icon name="run" size={24} stroke={1.8} style={{}} />
```

### `Icon` Component API

| Prop     | Type    | Default | Description                              |
|----------|---------|---------|------------------------------------------|
| `name`   | string  | —       | Icon key from the `paths` lookup object  |
| `size`   | number  | 24      | Width and height in pixels               |
| `stroke` | number  | 1.8     | SVG stroke width                         |
| `style`  | object  | —       | Additional inline styles (e.g., `color`) |

### SVG Properties (Shared)

All icon paths share these SVG attributes:

```
fill: none
stroke: currentColor
strokeWidth: {stroke}  (default 1.8)
strokeLinecap: round
strokeLinejoin: round
```

Icons inherit their color from the parent element's CSS `color` property via `currentColor`. This means icon color is always controlled by the container, never hardcoded.

**ViewBox:** All icons use `viewBox="0 0 24 24"` — the standard 24×24 grid.

---

## Full Icon Inventory

### Navigation Icons

| Key        | Visual Description                         | Used in                           |
|------------|---------------------------------------------|-----------------------------------|
| `home`     | House with chimney and interior partition   | Bottom nav, sidebar               |
| `goals`    | Three concentric circles (bullseye)         | Bottom nav, sidebar               |
| `settings` | Gear/cog with center circle                | Bottom nav, sidebar               |
| `back`     | Left-pointing arrow with horizontal bar     | Back navigation buttons           |
| `chevron`  | Right-pointing chevron                      | Settings row trailing indicator   |

### Action Icons

| Key        | Visual Description                         | Used in                           |
|------------|---------------------------------------------|-----------------------------------|
| `plus`     | Vertical + horizontal crosshair lines       | "New Goal" button, "Log Workout"  |
| `x`        | Diagonal cross (×)                          | Sheet close buttons, param remove |
| `check`    | Checkmark polyline                          | Toast confirmation, icon mgmt     |
| `edit`     | Pencil on paper corner                      | Reserved (not currently used)     |
| `trash`    | Trash can with lid and body                 | Workout detail delete button      |
| `download` | Downward arrow into tray                    | Settings export button            |

### Data / Display Icons

| Key        | Visual Description                         | Used in                           |
|------------|---------------------------------------------|-----------------------------------|
| `calendar` | Calendar rectangle with pins and divider    | Reserved for future use           |
| `clock`    | Circle with hour/minute hands               | Reserved for future use           |
| `chart`    | Line chart with upward trend                | Reserved for future use           |
| `target`   | Two concentric circles (smaller bullseye)   | Default/general goal icon         |
| `flame`    | Single flame shape                          | Cardio goal icon                  |

### Activity Icons (Goal System)

| Key         | Visual Description                        | Default Goal Type    | Default Color |
|-------------|--------------------------------------------|-----------------------|---------------|
| `run`       | Lightning bolt (energy/speed)              | Running               | #0D9488       |
| `walk`      | Stick figure in walking pose               | Walking               | #059669       |
| `strength`  | Dumbbell/barbell with crossed ends         | Strength              | #7C3AED       |
| `bike`      | Bicycle (two wheels + frame)               | Cycling               | #F97316       |
| `swim`      | Wavy lines + figure/arms                   | Swimming              | #3B82F6       |
| `yoga`      | Figure in seated pose with extended limbs  | Yoga                  | #EC4899       |
| `flame`     | Single flame                               | Cardio / HIIT         | #EF4444       |
| `target`    | Two concentric circles                     | General / fallback    | #6366F1       |

---

## Goal Icon System (`GOAL_ICONS`)

The goal icon system is a curated set of 8 activity icons that users can assign to goals. Defined as an array in `data.jsx`:

```javascript
const GOAL_ICONS = [
  { id: 'run',      name: 'Running',   iconName: 'run' },
  { id: 'walk',     name: 'Walking',   iconName: 'walk' },
  { id: 'strength', name: 'Strength',  iconName: 'strength' },
  { id: 'bike',     name: 'Cycling',   iconName: 'bike' },
  { id: 'swim',     name: 'Swimming',  iconName: 'swim' },
  { id: 'yoga',     name: 'Yoga',      iconName: 'yoga' },
  { id: 'flame',    name: 'Cardio',    iconName: 'flame' },
  { id: 'target',   name: 'General',   iconName: 'target' },
];
```

### Icon Management

Users can enable/disable icons from the set via `Settings > Workout Icons > IconManagementScreen`. Disabled icons are hidden from the `IconColorPicker` when creating/editing goals. The enabled set is stored in `data.enabledIcons` (an array of icon IDs) and persisted to `localStorage`.

**Constraints:**
- At least one icon must remain enabled (the `toggleIcon` function prevents disabling the last one).
- Disabling an icon doesn't affect existing goals already using it.
- The enabled icons list defaults to all 8 icons when not set.

### Icon Picker (`IconColorPicker`)

Displayed as a bottom sheet when the user taps the icon button in the goal form. Shows a 4-column grid of enabled icons plus a color swatch row.

**Icon grid item states:**

| State    | Border                          | Background                                      | Text Color |
|----------|---------------------------------|--------------------------------------------------|------------|
| Inactive | `1.5px solid var(--border)`     | `var(--surface)`                                 | `--fg-2`   |
| Active   | `1.5px solid {selectedColor}`   | `color-mix(in srgb, {color} 12%, transparent)`   | `{color}`  |

---

## Sizing Conventions

Icons are rendered at different sizes depending on context. The size is always passed as the `size` prop to the `Icon` component.

| Context                        | Size   | Stroke Width | Notes                              |
|--------------------------------|--------|--------------|-------------------------------------|
| Bottom nav items               | 24px   | 1.8          | Standard navigation size            |
| Button inline icons            | 16–18px| 1.8          | Smaller to sit alongside text       |
| List item icon container       | 20px   | 1.8          | Inside 40×40px container            |
| Goal card icon container       | 20px   | 1.8          | Inside 38×38px container            |
| Goal detail header icon        | 24px   | 1.8          | Inside 46×46px container            |
| Workout detail icon            | 22px   | 1.8          | Inside 44×44px container            |
| Empty state icon               | 26px   | 1.8          | Inside 56×56px circle              |
| Goal selector chips            | 16px   | 2.0          | Tighter stroke for small size       |
| Toast check icon               | 16px   | 1.8          | Small, inline with text             |
| Sheet close button             | 20px   | 1.8          | `x` icon                           |
| Back button                    | 18px   | 1.8          | `back` icon                        |
| Settings row trailing          | 16px   | 1.8          | `chevron` or `download` icon       |
| Parameter remove button        | 16px   | 1.8          | `x` icon                           |
| Icon management grid           | 20px   | 1.8          | Inside 38×38px container            |
| Icon color picker grid         | 22px   | 1.8          | Inside button with padding          |

### Stroke Width Notes

The default stroke width is `1.8px` at all sizes. The only exception is goal selector chips in the log-workout form, which use `stroke={2}` at 16px to maintain legibility at the smaller size.

**Rule:** Never go below 1.5px stroke width. At smaller icon sizes, increase stroke rather than decrease — thin strokes become invisible on low-DPI screens.

---

## Icon Container Patterns

Icons rarely appear bare. They're almost always placed inside a container that provides a background and padding.

### Pattern 1: Goal-Colored Container

```jsx
<div style={{
  width: 38, height: 38,
  borderRadius: 'var(--radius-md)',    // 10px
  background: goalBg(goal),            // 14% opacity goal color
  color: goalColor(goal),              // full-saturation goal color
  display: 'grid',
  placeItems: 'center',
  flexShrink: 0,
}}>
  <Icon name={goalIconName(goal)} size={20} />
</div>
```

### Pattern 2: Neutral Container (List Item)

```jsx
<div className="icon" style={{ color: goalColor(goal) }}>
  <Icon name={iconName} size={20} />
</div>
```

Uses the `.list-item .icon` class: 40×40px, 6px radius, white background, 1px border.

### Pattern 3: Accent Circle (Empty State)

```jsx
<div style={{
  width: 56, height: 56,
  borderRadius: '50%',
  background: 'var(--accent-soft)',
  color: 'var(--accent)',
  display: 'grid',
  placeItems: 'center',
}}>
  <Icon name="run" size={26} />
</div>
```

---

## Rules for Adding New Icons

1. **All icons must use the 24×24 viewBox** with stroke-based paths (no fills except for solid shapes like circles in the `goals` icon).
2. **Use `currentColor` for stroke** — never hardcode a color. The parent container controls the color.
3. **Maintain consistent stroke width.** Default is 1.8px. Use `round` line caps and joins.
4. **Add the icon to the `paths` object** in the `Icon` component in `data.jsx`.
5. **If it's an activity icon,** also add an entry to `GOAL_ICONS` array and update `guessIconColor()` with relevant keyword patterns.
6. **Keep path data minimal.** Each icon should be expressible in 1–3 SVG elements (path, circle, polyline, rect). Complex illustrations are not icons.
7. **Test at 16px, 20px, and 24px** to ensure legibility across all usage sizes. If detail is lost at 16px, simplify the paths.
8. **No emoji.** The icon system is strictly SVG stroke art. Emoji would break the visual consistency.
