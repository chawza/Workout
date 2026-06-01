# Spacing, Layout & Elevation — Deep Technical Reference

This document covers the spatial system: the spacing scale, layout grid, content regions, border-radius tokens, elevation layers, and responsive breakpoints.

---

## Spacing Scale

All spacing is derived from a **4px base unit**. Seven named stops are defined as CSS custom properties on `:root`.

| Token    | CSS Variable  | Value | Common uses                                          |
|----------|---------------|-------|------------------------------------------------------|
| space-1  | `--space-1`   | 4px   | Icon-to-label gap in bottom nav, micro adjustments   |
| space-2  | `--space-2`   | 8px   | Button icon gap, toggle group padding, chip gaps     |
| space-3  | `--space-3`   | 12px  | Card bottom margin, stat grid gap, intra-card gap    |
| space-4  | `--space-4`   | 16px  | Card padding, content area padding, appbar padding   |
| space-5  | `--space-5`   | 24px  | Sheet body padding, section spacing, week summary mb |
| space-6  | `--space-6`   | 32px  | Section title top margin, sidebar padding            |
| space-8  | `--space-8`   | 48px  | Empty-state vertical padding, desktop FAB offset     |

### Spacing Principles

1. **All measurements are multiples of 4.** No 5px, 7px, 3px, etc. The single exception is `2px` for toggle group internal padding and `1px` for borders/separators.
2. **Padding is consistent within component types.** All cards use 16px. All sheet bodies use 24px. All inputs use `10px 14px`.
3. **Margin between siblings uses space-3 (12px).** Cards stack with `margin-bottom: var(--space-3)`. This creates a tight-but-readable rhythm.
4. **Section gaps use space-5 (24px) or space-6 (32px).** Larger gaps separate semantically distinct groups (e.g., "This week" from "Earlier").
5. **The 12px step fills the gap between 8 and 16.** Many components need tighter-than-16 but looser-than-8 internal spacing. The half-step at 12px handles stat box padding, parameter row padding, and chip gaps.

---

## Layout Structure

### Mobile (< 1024px)

```
┌──────────────────────────────────────┐
│            App Bar (sticky)          │  ← 16px h-padding, 1px bottom border
├──────────────────────────────────────┤
│                                      │
│         Content (scrollable)         │  ← 16px padding all sides
│                                      │
│                                      │
│                                      │
├──────────────────────────────────────┤
│          Bottom Nav (fixed)          │  ← 8px top padding + safe-area-inset
└──────────────────────────────────────┘
           max-width: 430px
           border-inline: 1px solid --border-soft
```

The app shell (`.app`) is constrained to `max-width: 430px` and centered with `margin: 0 auto`. This mimics an iPhone Pro Max viewport. On wider mobile screens, the inline borders create a visual boundary.

**Content region** uses `flex: 1` and `overflow-y: auto` to create an internally scrolling area between the app bar and bottom nav. The app bar is `position: sticky` within this flex column.

**Bottom nav** is positioned absolutely within the phone frame at `bottom: 0`, with `paddingBottom: 22px` to clear the iOS home indicator.

### Desktop (≥ 1024px)

```
┌──────────┬───────────────────────────┐
│          │        App Bar            │
│ Sidebar  ├───────────────────────────┤
│ 220px    │                           │
│          │     Content (scrollable)  │
│          │                           │
│          │                           │
└──────────┴───────────────────────────┘
           max-width: removed (100%)
           sidebar: 220px, 32px padding, right border
           bottom nav: hidden
```

At the 1024px breakpoint:
- `.sidebar` switches from `display: none` to `display: flex` (column, 220px fixed width)
- `.bottomnav` switches to `display: none`
- `.app` removes its `max-width` and switches to `flex-direction: row`
- Calendar day cells increase to `min-height: 110px` with `padding: var(--space-3)`

### Phone Frame (Prototype Context)

The prototype wraps the app in an iOS device frame (`IOSDevice` component) at a fixed `402×874px`. A viewport-fitting script scales the frame via `transform: scale()` to fit any browser window:

```javascript
function fitFrame() {
  const s = Math.min(1, (window.innerHeight - 28) / 874, (window.innerWidth - 28) / 402);
  frame.style.transform = 'scale(' + s + ')';
}
```

Within the phone frame, the app occupies the full area with:
- `STATUS_BAR = 54px` clearance at top (under Dynamic Island)
- `NAV_H = 78px` reserved at bottom for bottom nav
- Content fills the space between: `top: 54px, bottom: 78px` (absolute positioning)

**Important override:** The CSS hides `.bottomnav` above 1024px for the desktop sidebar layout. Since the phone frame lives in a wide browser viewport, a forced override is needed:

```css
.phone-app .bottomnav { display: flex !important; }
```

---

## Border Radius Tokens

| Token      | CSS Variable    | Value  | Usage                                                      |
|------------|-----------------|--------|------------------------------------------------------------|
| sm         | `--radius-sm`   | 6px    | Inputs, textarea, parameter cards, icon containers, calendar cells |
| md         | `--radius-md`   | 10px   | Cards, buttons, sheets (desktop), toggle groups, sidebar links, calendar container |
| lg         | `--radius-lg`   | 14px   | Bottom sheets (mobile top corners), modal dialogs          |
| pill       | `--radius-pill` | 999px  | Badges, goal selector chips, FAB, toasts, color swatches, switch track |

### Radius Rules

- **Nested radius reduction:** When a rounded container holds a rounded child, the child's radius should be smaller. Example: sheet (14px) contains cards (10px) contains inputs (6px).
- **No radius above 14px on rectangular UI.** The 48px device bezel radius is OS-level chrome, not app UI. Pill shapes (999px) are only for elements that are semantically "tags" or circular.
- **Toggle group inner radius:** Active button uses `calc(var(--radius-sm) - 1px)` = 5px, accounting for the 2px container padding.

---

## Elevation System

The app uses a **border-first elevation model** — depth is primarily communicated through borders and tonal contrast, not shadows. Shadows are reserved for floating elements and interactive feedback.

### Elevation Levels

| Level | Surface           | Border                      | Shadow                                        | Examples                                   |
|-------|-------------------|-----------------------------|-----------------------------------------------|--------------------------------------------|
| 0     | `--bg` (#F8F9FB)  | None                        | None                                          | App background, content area canvas        |
| 1     | `--surface` (#FFF)| 1px solid `--border`        | **Hover only:** `0 2px 8px rgba(17,24,39,0.04)` | Cards, inputs, sidebar, toggle groups     |
| 2     | `--bg` (#F8F9FB)  | Top/bottom 1px `--border`   | None (overlay at `rgba(0,0,0,0.35)`)          | Bottom sheets, modal dialogs               |
| 3     | `--fg` (#111827)  | None                        | `0 8px 24px rgba(0,0,0,0.25)`                | Toasts                                     |
| 4     | `--accent`        | None                        | `0 4px 14px rgba(255,107,74,0.28)`            | FAB                                        |

### Shadow Specifications

```css
/* Card hover — Level 1 interactive */
.card:hover {
  box-shadow: 0 2px 8px rgba(17, 24, 39, 0.04);
}

/* FAB — Level 4 */
.fab {
  box-shadow: 0 4px 14px rgba(255, 107, 74, 0.28);
}

/* Toast — Level 3 */
/* Inline style, 0 8px 24px rgba(0,0,0,0.25) */

/* Modal overlay — not a shadow, a backdrop */
.modal-overlay {
  background: rgba(0, 0, 0, 0.35);
}

/* Toggle active button */
.toggle-group button.active {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}
/* Dark mode: increased to rgba(0,0,0,0.4) */
```

### Elevation Principles

1. **Static cards have zero shadow.** The 1px border provides sufficient separation against the slightly-toned background.
2. **Shadow appears on hover as feedback,** confirming that the card is interactive. The shadow is extremely subtle (4% opacity) — felt more than seen.
3. **Floating elements get shadows.** FAB, toasts, and any element that overlaps content gets a visible shadow to establish spatial relationship.
4. **The FAB shadow is accent-tinted.** Using `rgba(255, 107, 74, 0.28)` instead of neutral black creates a warm "glow" effect that reinforces the brand color.
5. **Sheets use overlays, not shadows.** A 35% black overlay behind the sheet communicates modality. The sheet itself has no shadow — the overlay does the depth work.

---

## Responsive Breakpoints

The app uses a single breakpoint at `1024px`:

| Range          | Layout              | Nav              | Max Width | Calendar Cells |
|----------------|---------------------|------------------|-----------|----------------|
| < 1024px       | Single column       | Bottom nav       | 430px     | min-h: 72px   |
| ≥ 1024px       | Sidebar + content   | Sidebar nav      | None      | min-h: 110px  |

### Target Viewport Matrix

For production implementation, validate across these viewports (from `DESIGN-MANIFEST.json`):

| Name               | Width × Height | Category      |
|--------------------|----------------|---------------|
| Mobile compact     | 360 × 800      | Mobile        |
| Mobile standard    | 390 × 844      | Mobile        |
| Mobile large       | 430 × 932      | Mobile        |
| Foldable/tablet    | 600 × 960      | Foldable      |
| Tablet portrait    | 820 × 1180     | Tablet        |
| Tablet landscape   | 1024 × 768     | Tablet        |
| Laptop             | 1366 × 768     | Desktop       |
| Desktop            | 1440 × 900     | Desktop       |
| Wide               | 1920 × 1080    | Wide          |

**All viewports must avoid horizontal scroll.** The 430px max-width mobile shell ensures content never overflows horizontally on any mobile device.

---

## Content Density Zones

Different areas of the app operate at different density levels:

| Zone              | Density  | Padding   | Gap     | Context                            |
|-------------------|----------|-----------|---------|-------------------------------------|
| Week summary      | High     | 12px/8px  | 12px    | 4-column stat grid, compact cards  |
| Calendar grid     | High     | 8px       | 1px     | 7-column grid, 10px entry text     |
| Workout list      | Medium   | 16px      | 0       | List items with border separators  |
| Goal cards        | Medium   | 16px      | 12px    | Card with header + action footer   |
| Form screens      | Low      | 16px/24px | 16px    | Generous field spacing for touch   |
| Settings          | Low      | 16px      | 0       | Wide rows with trailing controls   |
| Empty states      | Spacious | 48px      | 12px    | Centered icon + title + subtitle   |

The density gradient creates natural visual rhythm: data-dense views (calendar, stats) feel information-rich, while editing views (forms, settings) feel spacious and accessible.
