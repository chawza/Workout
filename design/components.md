# Components — Deep Technical Reference

This document is the exhaustive component inventory for the Workout Tracker app. Each component is documented with its CSS class, token values, states, variants, and implementation notes.

---

## Buttons

All buttons share the `.btn` base class. Four variants exist.

### Base (`.btn`)

```css
display: inline-flex;
align-items: center;
justify-content: center;
gap: var(--space-2);            /* 8px */
padding: 10px 18px;
border-radius: var(--radius-md); /* 10px */
border: 1px solid transparent;
font-size: var(--text-sm);       /* 14px */
font-weight: 600;
letter-spacing: -0.005em;
transition: transform 0.05s ease,
            background var(--motion-fast) var(--ease-standard),
            border-color var(--motion-fast);
```

**Active state (all variants):** `transform: translateY(1px)` — a 1px downward shift provides tactile press feedback.

### Variants

| Variant      | Class            | Background        | Text Color    | Border Color     |
|--------------|------------------|--------------------|---------------|------------------|
| Primary      | `.btn-primary`   | `--accent`         | `--accent-on` | `--accent`       |
| Secondary    | `.btn-secondary` | `--surface`        | `--fg`        | `--border`       |
| Ghost        | `.btn-ghost`     | transparent        | `--fg-2`      | transparent      |
| Danger       | `.btn-danger`    | `--danger`         | `--accent-on` | `--danger`       |

### Hover States

| Variant      | Hover Background     | Hover Border        | Hover Text         |
|--------------|----------------------|---------------------|--------------------|
| Primary      | `--accent-hover`     | *(unchanged)*       | *(unchanged)*      |
| Secondary    | *(unchanged)*        | `--fg-2`            | *(unchanged)*      |
| Ghost        | `--accent-soft`      | *(unchanged)*       | `--accent`         |
| Danger       | *(not defined)*      | *(unchanged)*       | *(unchanged)*      |

### Size Variations (Inline Overrides)

| Context                    | Padding        | Font Size         | Additional                |
|----------------------------|----------------|-------------------|---------------------------|
| Default                    | 10px 18px      | 14px              | —                         |
| Full-width CTA             | 14px 18px      | 16px              | `width: 100%`             |
| Compact (goal card actions)| 6px 12px       | 12px              | —                         |
| Ghost close button         | 8px            | —                 | Square hit target         |
| Ghost nav (back, edit)     | 8px 12px       | —                 | `marginLeft: -8px`        |

---

## Cards

### Base (`.card`)

```css
background: var(--surface);
border: 1px solid var(--border);
border-radius: var(--radius-md);  /* 10px */
padding: var(--space-4);          /* 16px */
margin-bottom: var(--space-3);    /* 12px */
transition: box-shadow var(--motion-fast);
```

**Hover:** `box-shadow: 0 2px 8px rgba(17, 24, 39, 0.04)`

### Specialized Card Variants

| Variant          | Modifications                                                      | Used in                       |
|------------------|--------------------------------------------------------------------|-------------------------------|
| Stat box         | `margin: 0; padding: 12px 8px; text-align: center`                | Week summary, goal detail     |
| Settings group   | `padding: 0 16px` (rows provide internal padding)                  | Settings screen               |
| Parameter card   | `border: 1px solid --border; border-radius: --radius-md; bg: --bg` | Goal form parameter rows      |
| Metric group     | `padding: 2px 16px` (tight vertical for trend rows)               | Goal detail parameter trends  |

---

## Bottom Sheet / Modal

### Overlay (`.modal-overlay`)

```css
position: fixed;
inset: 0;
background: rgba(0, 0, 0, 0.35);
z-index: 30;
display: flex;
align-items: flex-end;          /* Mobile: bottom-aligned */
justify-content: center;
```

Desktop override (≥ 1024px): `align-items: center; padding: var(--space-8)`

### Sheet (`.sheet`)

```css
background: var(--bg);
border-radius: var(--radius-lg) var(--radius-lg) 0 0;  /* 14px top only (mobile) */
width: 100%;
max-height: 90vh;
overflow-y: auto;
animation: slideUp var(--motion-base) var(--ease-standard);
```

Desktop override: `border-radius: var(--radius-lg); max-width: 520px; max-height: 85vh; animation: fadeIn`

### Sheet Anatomy

```
┌─────────────────────────────────┐
│  .sheet-header                  │  ← sticky, 16px/24px padding, bottom border
│  ┌─── h2 ───┐  ┌─ close btn ─┐ │
│  └───────────┘  └─────────────┘ │
├─────────────────────────────────┤
│  .sheet-body                    │  ← 16px/24px padding, scrollable
│  (form fields, content)         │
│                                 │
├─────────────────────────────────┤
│  .sheet-footer                  │  ← flex, gap 12px, top border
│  [Cancel]          [Save]       │  ← buttons with flex: 1
└─────────────────────────────────┘
```

### Animations

```css
@keyframes slideUp {
  from { transform: translateY(40px); opacity: 0; }
  to   { transform: translateY(0); opacity: 1; }
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.98); }
  to   { opacity: 1; transform: scale(1); }
}
```

Mobile uses `slideUp` (bottom sheet metaphor). Desktop uses `fadeIn` (centered dialog metaphor).

### In-Frame Sheet (`SheetShell` component)

Used for pickers within the phone frame (not full-viewport). Positioned absolutely within the frame:

```jsx
position: 'absolute', inset: 0, zIndex: 40,
background: 'rgba(0,0,0,0.38)'
```

The sheet itself uses `.sheet` class with `maxHeight: '70%'` or `'94%'`.

---

## Toggle Group (Segmented Control)

### Container (`.toggle-group`)

```css
display: inline-flex;
background: var(--surface);
border: 1px solid var(--border);
border-radius: var(--radius-md);  /* 10px */
padding: 2px;
```

### Segments

```css
/* Inactive */
flex: 1;
padding: 6px 14px;
border: none;
background: transparent;
color: var(--muted);
font-size: var(--text-sm);
font-weight: 500;
border-radius: calc(var(--radius-sm) - 1px);  /* 5px */

/* Active */
background: var(--bg);
color: var(--fg);
box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
```

**Dark mode active shadow:** increased to `rgba(0, 0, 0, 0.4)`.

### Usage Contexts

| Context               | Width      | Segments                    |
|-----------------------|------------|-----------------------------|
| Home view toggle      | Auto       | List · Calendar             |
| Parameter type        | `flex: 1`  | Decimal · Integer · Text    |
| Goal status           | `width: 100%` | New · Active · Completed |
| Default view (settings)| Auto      | List · Calendar             |

---

## Form Inputs

### Text Input (`.input`)

```css
width: 100%;
padding: 10px 14px;
border: 1px solid var(--border);
border-radius: var(--radius-sm);  /* 6px */
background: var(--bg);
color: var(--fg);
font: inherit;
font-size: var(--text-base);     /* 16px — prevents iOS zoom */
```

**Focus state:**
```css
outline: none;
border-color: var(--accent);
box-shadow: 0 0 0 3px rgba(255, 107, 74, 0.14);
```

The 3px accent-tinted ring provides clear focus indication without shifting layout.

### Textarea (`.textarea`)

Same as `.input` plus: `min-height: 80px; resize: vertical`

### Select (`.select`)

Same as `.input`. Native select styling preserved for platform consistency.

### Field Container (`.field`)

```css
display: flex;
flex-direction: column;
gap: var(--space-2);           /* 8px */
margin-bottom: var(--space-4); /* 16px */
```

### Error State

```css
/* On the input: */
border-color: var(--danger);

/* Error message below: */
font-size: var(--text-xs);
color: var(--danger);
```

---

## List Items

### Base (`.list-item`)

```css
display: flex;
align-items: center;
gap: var(--space-3);           /* 12px */
padding: var(--space-4);       /* 16px */
border-bottom: 1px solid var(--border-soft);
```

**Hover:** `background: var(--surface)`

### Anatomy

```
┌─────┬──────────────────────┬────────┐
│     │ .title (16px / 600)  │ .meta  │
│.icon│                      │(12px)  │
│40×40│ .subtitle (14px/400) │ mono   │
└─────┴──────────────────────┴────────┘
```

**Icon container (`.list-item .icon`):** 40×40px, 6px radius, white background, 1px border, centered content. The icon color is set dynamically per goal using `goalColor()`.

---

## FAB (Floating Action Button)

```css
position: fixed;
bottom: calc(72px + env(safe-area-inset-bottom));
right: var(--space-5);         /* 24px */
width: 56px;
height: 56px;
border-radius: 50%;
background: var(--accent);
color: var(--accent-on);
border: none;
box-shadow: 0 4px 14px rgba(255, 107, 74, 0.28);
z-index: 20;
```

**Hover:** `background: var(--accent-hover)`
**Active:** `transform: scale(0.96)`
**Desktop:** `bottom: var(--space-6); right: var(--space-8)`

> **Note:** The FAB is not currently used in the prototype (replaced by full-width "Log Workout" button), but the CSS remains defined for future use.

---

## Badges

### Base (`.badge`)

```css
display: inline-flex;
align-items: center;
padding: 2px 10px;
border-radius: var(--radius-pill);
font-size: var(--text-xs);      /* 12px */
font-weight: 600;
text-transform: uppercase;
letter-spacing: 0.03em;
```

| Variant     | Class              | Background                       | Text Color     |
|-------------|--------------------|----------------------------------|----------------|
| Active      | `.badge-active`    | `rgba(16, 185, 129, 0.10)`      | `--success`    |
| Completed   | `.badge-completed` | `rgba(107, 114, 128, 0.10)`     | `--muted`      |

---

## Toast

Implemented as a React component (`Toast`), not a CSS class.

```
position: absolute; bottom: 92px;
left: 50%; transform: translateX(-50%);
background: var(--fg);
color: var(--bg);
padding: 10px 16px;
border-radius: var(--radius-pill);
font-size: var(--text-sm);
font-weight: 600;
z-index: 50;
box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
animation: slideUp var(--motion-base) var(--ease-standard);
```

**Auto-dismiss:** 1800ms via `setTimeout` in the `flash()` function.
**Content:** Check icon (16px) + message text.

---

## Switch (Toggle)

Custom implementation in `Settings.jsx`. Not a CSS class.

```
Track:  48×28px, pill radius, --accent (on) / --border (off)
Knob:   22×22px, circle, --surface, 1px shadow
        transform: translateX(20px) when checked
```

Transition: `var(--motion-fast)` on both track background and knob position.

---

## App Bar

### Base (`.appbar`)

```css
display: flex;
align-items: center;
justify-content: space-between;
padding: var(--space-4);
border-bottom: 1px solid var(--border);
background: var(--bg);
position: sticky;
top: 0;
z-index: 10;
```

**Title (`.appbar h1`):** Space Grotesk, 22px, 700, -0.018em tracking.
**Actions (`.appbar .actions`):** flex, center-aligned, 8px gap.

Within the phone frame, `position: static` is applied (sticky would conflict with the absolute positioning of the content region).

---

## Bottom Nav

### Base (`.bottomnav`)

```css
display: flex;
align-items: center;
justify-content: space-around;
padding: var(--space-2) 0 calc(var(--space-2) + env(safe-area-inset-bottom));
border-top: 1px solid var(--border);
background: var(--bg);
```

Hidden on desktop (≥ 1024px). Force-shown inside the phone frame via `!important`.

### Nav Items

```css
display: flex;
flex-direction: column;
align-items: center;
gap: var(--space-1);         /* 4px */
padding: var(--space-2) var(--space-4);
font-size: var(--text-xs);  /* 12px */
color: var(--muted);
```

**Active:** `color: var(--accent)`
**Icons:** 24×24px, 1.8 stroke width.

Three tabs: Home (house), Goals (target), Settings (gear).

---

## Calendar Grid

### Container (`.calendar`)

```css
display: grid;
grid-template-columns: repeat(7, 1fr);
gap: 1px;
background: var(--border);   /* gap color = border */
border: 1px solid var(--border);
border-radius: var(--radius-md);
overflow: hidden;
```

The 1px gap with border-colored background creates the grid lines — a single-element technique that avoids per-cell border management.

### Day Cells

| State        | Class           | Background        | Font Weight |
|--------------|-----------------|--------------------|----|
| Default      | `.day`          | `--bg`             | 400 |
| Today        | `.day.today`    | `--surface-warm`   | 700 |
| Other month  | `.day.other-month` | `--surface`     | 400, color: `--muted` |

**Mobile:** `min-height: 72px; padding: 8px`
**Desktop:** `min-height: 110px; padding: 12px`

### Workout Entry Chips

```css
display: flex;
align-items: center;
justify-content: space-between;
padding: 2px 4px;              /* mobile: 4px 8px on desktop */
border-radius: var(--radius-sm);
font-size: 10px;               /* mobile: 12px on desktop */
```

Color is set via the `--entry-color` CSS custom property (set inline per entry):
```css
background: color-mix(in srgb, var(--entry-color) 14%, transparent);
color: var(--entry-color);
```

Maximum 2 entries displayed per cell; overflow shows `+N` count.

---

## Sparkline (Trend Chart)

Implemented as an inline SVG in `Goals.jsx`. Not a CSS component.

```
Default size: 96×34px
Stroke: 2px, goal color, round caps/joins, 85% opacity
End dot: 2.6px radius circle, goal color, full opacity
```

Renders the last 8 data points. Falls back to empty `<div>` with matching dimensions if fewer than 2 values.

---

## Icon Container (Goal)

Used in goal cards, goal detail header, workout rows, and workout detail.

```
width: 38–46px (varies by context)
height: 38–46px
border-radius: var(--radius-md)    /* 10px */
background: goalBg(goal)           /* 14% opacity goal color */
color: goalColor(goal)             /* full saturation */
display: grid;
place-items: center;
```

| Context           | Size   | Icon Size |
|-------------------|--------|-----------|
| Goal card         | 38×38  | 20px      |
| Goal detail header| 46×46  | 24px      |
| Workout row       | 40×40  | 20px      |
| Workout detail    | 44×44  | 22px      |
