# Typography — Deep Technical Reference

This document specifies the complete typographic system for the Workout Tracker app: font families, loading strategy, the type scale, weight usage, and per-component application rules.

---

## Font Stack

### Display: Space Grotesk

| Property       | Value                                                         |
|----------------|---------------------------------------------------------------|
| Family         | `'Space Grotesk', -apple-system, BlinkMacSystemFont, system-ui, sans-serif` |
| CSS Variable   | `--font-display`                                              |
| Loaded weights | 500, 700                                                      |
| Source         | Google Fonts                                                  |
| Classification | Geometric sans-serif                                          |

**Rationale:** Space Grotesk's angular terminals and geometric construction evoke precision and energy. Its tight default metrics pair well with the sporty brand personality. Used exclusively for headings and the app logotype — never for body text or form labels.

**Where it appears:**
- App bar `<h1>` titles (22px / 700)
- Sheet header `<h2>` titles (22px / 700)
- Goal detail name (22px / 700)
- Workout detail title (28px / 700)
- Calendar month label (18px / 700)
- Sidebar `.brand` logotype (22px / 700)
- Stat box numbers use monospace, not display

### Body: DM Sans

| Property       | Value                                                         |
|----------------|---------------------------------------------------------------|
| Family         | `'DM Sans', -apple-system, BlinkMacSystemFont, system-ui, sans-serif` |
| CSS Variable   | `--font-body`                                                 |
| Loaded weights | 400, 500, 600, 700                                           |
| Source         | Google Fonts                                                  |
| Classification | Humanist sans-serif                                           |

**Rationale:** DM Sans has large, open apertures and a tall x-height that maintains legibility at small sizes (12–14px) common in mobile UIs. Its humanist curves soften the geometric display face, creating a warm-professional pairing.

**Where it appears:**
- All body text (16px / 400)
- Button labels (14px / 600)
- Form labels (14px / 500)
- List item titles (16px / 600)
- List item subtitles (14px / 400)
- Section headers (14px / 600, uppercase, 0.04em tracking)
- Badges (12px / 600, uppercase, 0.03em tracking)
- Settings row labels (16px / 600)
- Settings row descriptions (14px / 400)

### Data: System Monospace

| Property       | Value                                                         |
|----------------|---------------------------------------------------------------|
| Family         | `ui-monospace, 'SF Mono', Menlo, Monaco, monospace`           |
| CSS Variable   | `--font-mono`                                                 |
| Loaded weights | System-dependent (typically 400, 700)                         |
| Source         | System fonts — no external load                               |

**Rationale:** Monospace numerals provide tabular alignment in stat grids, calendar timestamps, and trend values. The system monospace stack ensures zero additional font loading weight. The "instrument readout" aesthetic reinforces the data-tracking purpose.

**Where it appears:**
- Stat box numbers (22px / 700, -0.02em tracking)
- List item trailing metadata (12px / 400)
- Calendar entry timestamps (10px mobile, 12px desktop)
- Parameter trend current values (18px / 700)
- Parameter trend deltas (12px / 600)
- Pace display values (via `paceStr()` formatter)

---

## Type Scale

The scale uses six fixed stops with no intermediate sizes. CSS custom properties are defined on `:root`.

| Token      | CSS Variable   | Size  | Typical use                                        |
|------------|----------------|-------|----------------------------------------------------|
| xs         | `--text-xs`    | 12px  | Badges, calendar day headers, meta captions, deltas|
| sm         | `--text-sm`    | 14px  | Buttons, form labels, subtitles, settings desc     |
| base       | `--text-base`  | 16px  | Body text, list titles, input text, settings labels|
| lg         | `--text-lg`    | 18px  | Calendar month heading, parameter trend values     |
| xl         | `--text-xl`    | 22px  | App bar titles, sheet headers, stat box numbers    |
| 2xl        | `--text-2xl`   | 28px  | Workout detail title (largest text in the app)     |

### Scale Ratios

```
12 → 14  (×1.167)
14 → 16  (×1.143)
16 → 18  (×1.125)
18 → 22  (×1.222)  ← intentional jump
22 → 28  (×1.273)  ← intentional jump
```

The two larger jumps (18→22, 22→28) create clear separation between body-tier and headline-tier content. This avoids the "everything looks the same size" problem common in linear scales on mobile.

---

## Weight Usage

| Weight | Name       | CSS `font-weight` | Usage                                                    |
|--------|------------|---------------------|----------------------------------------------------------|
| 400    | Regular    | 400                 | Body text, input values, list subtitles, descriptions    |
| 500    | Medium     | 500                 | Form labels, sidebar nav links, calendar day numbers     |
| 600    | Semi-Bold  | 600                 | Button labels, list titles, section headers, badges      |
| 700    | Bold       | 700                 | All headings (Space Grotesk), stat box numbers (mono)    |

**Rule:** Within any single view, use at most three weights. The typical pattern is:
- 700 for the screen title (Space Grotesk)
- 600 for item titles and interactive labels (DM Sans)
- 400 for body text and descriptions (DM Sans)

Monospace 700 is additive and doesn't count toward this limit since it's visually distinct as a different face.

---

## Letter Spacing

| Context                    | Value      | Reason                                              |
|----------------------------|------------|-----------------------------------------------------|
| Display headings           | -0.018em   | Tightens Space Grotesk at large sizes for cohesion  |
| Body text                  | 0 (normal) | DM Sans is optimized for default spacing at 14–16px |
| Button labels              | -0.005em   | Subtle tightening for compact button text           |
| Section headers (uppercase)| 0.04em     | Opens up ALL-CAPS for readability                   |
| Badges (uppercase)         | 0.03em     | Slightly tighter than section headers               |
| Stat box numbers (mono)    | -0.02em    | Tightens monospace for a dense, dashboard feel      |

---

## Line Height

| Context           | Value | Computed at 16px |
|-------------------|-------|------------------|
| Body text (global)| 1.52  | ~24px            |
| Headings          | 1.15–1.2 | 25–34px       |
| Buttons / labels  | 1.3   | ~18px            |
| Calendar entries  | 1.3   | ~13px            |
| Stat box values   | 1.0   | Matches font size|

The global `line-height: 1.52` on `body` is slightly taller than the common 1.5 default, giving DM Sans extra breathing room that improves readability in the data-dense workout list views.

---

## Text Rendering

```css
body {
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
}
```

`optimizeLegibility` enables kerning and ligature tables in both Google Fonts. `-webkit-font-smoothing: antialiased` produces thinner, crisper strokes on macOS/iOS — appropriate for the clean, minimal aesthetic.

---

## Font Loading Strategy

Fonts are loaded via Google Fonts with `display=swap` and preconnected origins:

```html
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Space+Grotesk:wght@500;700&display=swap" rel="stylesheet" />
```

**`display=swap`** ensures text is immediately visible in the system fallback stack, then swaps to the web font once loaded. The fallback stack (`-apple-system, BlinkMacSystemFont, system-ui, sans-serif`) provides a reasonable metric-compatible substitute during the swap window.

**Performance note:** Two families × limited weight subsets = ~120KB total font payload (WOFF2). Both families are Latin-only in the current configuration. If CJK support is needed, consider adding `noto-sans-jp` or similar with a separate subset request.

---

## Section Header Pattern

A recurring typographic pattern used for content grouping throughout the app:

```css
{
  fontSize: 'var(--text-sm)',     /* 14px */
  color: 'var(--muted)',          /* #6B7280 */
  textTransform: 'uppercase',
  letterSpacing: '0.04em',
  fontWeight: 600,
  margin: '0 0 var(--space-3)'   /* 12px bottom */
}
```

Used for: "This week" / "Earlier" in workout list, "Active" / "Completed" in goals list, "Parameter Trends" / "Recent Workouts" in goal detail, all setting group headers.

This pattern is implemented as the `<SectionTitle>` component in `Goals.jsx` and repeated inline in other screens. It should be extracted into a shared utility if the component count grows.
