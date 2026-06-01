---
version: alpha
name: Workout Tracker
description: A fun-casual-sporty workout tracking app with coral accent, geometric display type, and airy surfaces.
colors:
  primary: "#FF6B4A"
  primary-hover: "#E85A3A"
  primary-active: "#D14E30"
  on-primary: "#FFFFFF"
  primary-soft: "rgba(255, 107, 74, 0.10)"
  surface: "#FFFFFF"
  surface-warm: "#FFF5F3"
  background: "#F8F9FB"
  foreground: "#111827"
  foreground-secondary: "#374151"
  muted: "#6B7280"
  border: "#E5E7EB"
  border-soft: "#F3F4F6"
  success: "#10B981"
  warning: "#F59E0B"
  error: "#EF4444"
  dark-background: "#0F1115"
  dark-surface: "#181B20"
  dark-surface-warm: "#241A16"
  dark-foreground: "#F3F4F6"
  dark-foreground-secondary: "#C9CDD3"
  dark-muted: "#8A9099"
  dark-border: "#2A2E35"
  dark-border-soft: "#20242B"
  dark-primary: "#E2674A"
  dark-primary-hover: "#EC7558"
  dark-primary-soft: "rgba(226, 103, 74, 0.18)"
typography:
  headline-display:
    fontFamily: Space Grotesk
    fontSize: 28px
    fontWeight: 700
    lineHeight: 1.15
    letterSpacing: -0.018em
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 22px
    fontWeight: 700
    lineHeight: 1.2
    letterSpacing: -0.018em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 18px
    fontWeight: 700
    lineHeight: 1.3
  body-lg:
    fontFamily: DM Sans
    fontSize: 18px
    fontWeight: 400
    lineHeight: 1.52
  body-md:
    fontFamily: DM Sans
    fontSize: 16px
    fontWeight: 400
    lineHeight: 1.52
  body-sm:
    fontFamily: DM Sans
    fontSize: 14px
    fontWeight: 400
    lineHeight: 1.52
  label-lg:
    fontFamily: DM Sans
    fontSize: 16px
    fontWeight: 600
    lineHeight: 1.3
  label-md:
    fontFamily: DM Sans
    fontSize: 14px
    fontWeight: 600
    lineHeight: 1.3
  label-sm:
    fontFamily: DM Sans
    fontSize: 12px
    fontWeight: 600
    lineHeight: 1.3
    letterSpacing: 0.04em
  mono-lg:
    fontFamily: ui-monospace, SF Mono, Menlo, Monaco, monospace
    fontSize: 22px
    fontWeight: 700
    letterSpacing: -0.02em
  mono-sm:
    fontFamily: ui-monospace, SF Mono, Menlo, Monaco, monospace
    fontSize: 12px
    fontWeight: 400
rounded:
  sm: 6px
  md: 10px
  lg: 14px
  full: 999px
spacing:
  xs: 4px
  sm: 8px
  md: 12px
  lg: 16px
  xl: 24px
  2xl: 32px
  3xl: 48px
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
    padding: 10px 18px
  button-primary-hover:
    backgroundColor: "{colors.primary-hover}"
  button-primary-active:
    backgroundColor: "{colors.primary-active}"
  button-secondary:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.md}"
    padding: 10px 18px
  button-ghost:
    backgroundColor: transparent
    textColor: "{colors.foreground-secondary}"
    rounded: "{rounded.md}"
    padding: 8px 12px
  button-ghost-hover:
    backgroundColor: "{colors.primary-soft}"
    textColor: "{colors.primary}"
  button-danger:
    backgroundColor: "{colors.error}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
  card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.md}"
    padding: 16px
  fab:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.full}"
    size: 56px
  input:
    backgroundColor: "{colors.background}"
    textColor: "{colors.foreground}"
    rounded: "{rounded.sm}"
    padding: 10px 14px
  badge-active:
    backgroundColor: "rgba(16, 185, 129, 0.10)"
    textColor: "{colors.success}"
    rounded: "{rounded.full}"
  badge-completed:
    backgroundColor: "rgba(107, 114, 128, 0.10)"
    textColor: "{colors.muted}"
    rounded: "{rounded.full}"
---

# Workout Tracker — Design System

## Overview

Workout Tracker is a **fun-casual-sporty** fitness app for personal workout logging and goal tracking. The visual identity balances energy and approachability — it should feel motivating without being aggressive, structured without being clinical.

**Brand personality:** Energetic, friendly, organized, encouraging.
**Target audience:** Fitness-minded individuals who track workouts against personal goals. Not competitive athletes — everyday people building habits.
**Emotional response:** The UI should feel like a supportive coach's clipboard — clean, purposeful, and satisfying to check off.

**Design principles:**
- **Clarity first.** Data-heavy screens (calendars, trend charts, stat grids) stay readable through generous whitespace, monospace numerics, and restrained color use.
- **Warmth through accent.** The coral accent (#FF6B4A) is the single energetic voice — used sparingly for primary actions and key data. Everything else is neutral.
- **Airy, not empty.** Generous padding inside cards (16px) and between sections (24px) creates breathing room. Density is reserved for the calendar grid.
- **Motion is functional.** Transitions confirm actions (toast slide-up, sheet entry) but never decorate. 0.15s for micro-interactions, 0.25s for layout shifts.

## Colors

The palette is built on high-contrast cool neutrals with a single warm accent.

- **Primary (#FF6B4A):** A warm coral used exclusively for primary CTAs, active nav states, the FAB, and key interactive highlights. Never used for backgrounds or large surfaces.
- **Foreground (#111827):** Near-black ink for headlines, primary text, and high-emphasis content. Provides strong contrast against the light background.
- **Foreground Secondary (#374151):** Darker gray for secondary text, button labels, and supporting copy.
- **Muted (#6B7280):** Mid-gray for metadata, timestamps, captions, section headers, and placeholder text.
- **Background (#F8F9FB):** Cool-tinted off-white. The app's canvas color — slightly cooler than pure white to reduce glare.
- **Surface (#FFFFFF):** Pure white for cards, sheets, and elevated containers. Creates subtle lift against the background.
- **Surface Warm (#FFF5F3):** A faint peach tint used only for the calendar "today" cell and warm contextual highlights.
- **Border (#E5E7EB):** Standard border for cards, inputs, and structural dividers.
- **Border Soft (#F3F4F6):** Lighter separator for list item dividers and subtle internal boundaries.
- **Success (#10B981):** Green for "active" badges and positive trend deltas. Used semantically, never decoratively.
- **Warning (#F59E0B):** Amber for caution states. Currently reserved.
- **Error (#EF4444):** Red for delete buttons, validation errors, and negative trend deltas.

**Goal category colors** are a secondary palette used for per-goal identity. Each goal picks one color from this set:

| Token    | Hex       | Typical use         |
|----------|-----------|---------------------|
| teal     | #0D9488   | Running             |
| green    | #059669   | Walking             |
| blue     | #3B82F6   | Swimming            |
| indigo   | #6366F1   | General / default   |
| purple   | #7C3AED   | Strength            |
| pink     | #EC4899   | Yoga                |
| red      | #EF4444   | Cardio              |
| orange   | #F97316   | Cycling             |
| amber    | #F59E0B   | Reserved            |
| slate    | #64748B   | Neutral / misc      |

Goal colors appear at 14% opacity as icon backgrounds (`color-mix(in srgb, {color} 14%, transparent)`) and at full saturation for icon strokes and calendar entry text.

**Dark mode** inverts the neutral scale while keeping accent warmth. The coral shifts to #E2674A (slightly muted to avoid harshness on dark backgrounds). See `design/colors.md` for the full dark-mode token map.

## Typography

Two typefaces establish a clear hierarchy between display and body content.

- **Space Grotesk** (display): A geometric sans-serif for all headings, screen titles, and the brand logotype. Set at weight 700 with tight tracking (-0.018em). Its angular geometry evokes precision and energy — appropriate for a fitness context.
- **DM Sans** (body): A humanist sans-serif for all body text, labels, buttons, and form content. Weights 400–700. Its open apertures and generous x-height ensure readability at small sizes (12–14px) on mobile.
- **System monospace** (data): `ui-monospace, SF Mono, Menlo, Monaco` for numeric data — stat boxes, time displays, pace values, and the calendar grid. Weight 700 at display size, 400 at caption size. The monospace face gives tabular alignment and a "dashboard instrument" feel.

**Scale:** 12 · 14 · 16 · 18 · 22 · 28px. Six stops, no intermediate values. The jump from 18→22→28 is intentionally large to create clear visual hierarchy between body and headline tiers.

## Layout

The app uses a **mobile-first single-column layout** inside a 430px max-width shell (matching iPhone Pro Max). On desktop (≥1024px), a 220px sidebar appears and the max-width constraint is removed.

**Spacing scale** follows a 4px base unit: 4 · 8 · 12 · 16 · 24 · 32 · 48px. The 12px step provides a half-increment between 8 and 16 for tighter internal padding (e.g., stat boxes, badge padding).

**Content structure:**
- App bar: sticky at top, 1px bottom border, 16px horizontal padding
- Content area: scrollable, 16px padding on all sides
- Bottom nav (mobile): fixed at bottom, 8px vertical padding + safe-area-inset
- Sidebar (desktop): 220px wide, 32px padding, right border

**Cards** use 16px internal padding, 12px bottom margin between siblings, and a 1px solid border. No default box-shadow — a subtle 2px/8px shadow appears on hover only.

## Elevation & Depth

Depth is conveyed through **border layering and tonal contrast**, not shadows.

- **Level 0 (canvas):** Background color (#F8F9FB). No border.
- **Level 1 (cards, inputs):** White surface + 1px solid border (#E5E7EB). A 2px/8px shadow at 4% opacity appears on card hover as a subtle lift affordance.
- **Level 2 (sheets, modals):** Full-viewport overlay at 35% black. The sheet itself uses background color (not white) with a top border, creating a layered-paper effect.
- **Level 3 (toasts):** Foreground-colored (#111827) pill with 8px/24px shadow at 25% opacity. Sits above all other content.
- **Level 4 (FAB):** Accent-colored with a 4px/14px shadow tinted with the accent color at 28% opacity. The tinted shadow makes it feel like the button glows rather than floats.

Shadows are never used on static content. They are exclusively for floating/overlaid elements and hover states.

## Shapes

The shape language uses **soft rectangles with moderate rounding** — approachable but not bubbly.

- **Small (6px):** Inputs, parameter cards, calendar day cells, icon containers, date/time pickers.
- **Medium (10px):** Cards, buttons, sheets, toggle groups, sidebar nav items, workout entry chips.
- **Large (14px):** Bottom sheets, modal dialogs. Used for the outermost container of overlay surfaces.
- **Pill (999px):** Badges, chips (goal selector in log-workout), the FAB, toasts, color swatches. Reserved for small, self-contained elements.

The device bezel uses 48px corner radius (iOS standard). Internal UI never approaches this radius.

## Components

See `design/components.md` for the full component inventory with states, tokens, and implementation notes. Key components:

- **Buttons:** Four variants — primary (coral fill), secondary (white + border), ghost (transparent), danger (red fill). All share 10px/18px padding, 10px radius, 600 weight, 14px size. Active state adds 1px translateY.
- **Cards:** White surface, 1px border, 10px radius, 16px padding. Hover adds subtle shadow. Used for goal cards, stat boxes, parameter groups, and settings groups.
- **Bottom sheet:** Slides up from bottom on mobile (14px top radius), centered modal on desktop (14px all-round). Header is sticky with bottom border. Footer has top border with flex-spaced action buttons.
- **Toggle group (segmented control):** Inline-flex container with 2px padding, active segment gets background + subtle shadow. Used for list/calendar view toggle, parameter type selector, goal status control.
- **FAB:** 56px circle, accent fill, centered plus icon, accent-tinted shadow. Fixed position above bottom nav.
- **Form inputs:** Full-width, 6px radius, background-colored fill, 1px border. Focus state adds accent border + 3px accent-tinted ring.
- **List items:** Flex row with 40px icon box, info column, and trailing metadata. Bottom border-soft separator. Hover adds surface background.
- **Badges:** Pill-shaped, 12px uppercase text, 600 weight, 0.03em tracking. Two semantic variants: active (green) and completed (gray).
- **Toast:** Pill-shaped, foreground-colored, slide-up animation, 1800ms auto-dismiss. Shows check icon + message.
- **Switch (toggle):** 48×28px track, 22px knob, accent color when on. Custom implementation (not native checkbox).
- **Calendar grid:** 7-column CSS grid with 1px gap (border color creates the grid lines). Day headers are uppercase 12px. Day cells have minimum height 72px (110px desktop). Workout entries are color-coded chips inside each day.

## Do's and Don'ts

- **Do** use the coral accent only for the single primary action per screen and active navigation states.
- **Do** use monospace for all numeric data (stats, times, distances, counts) to maintain tabular alignment.
- **Do** maintain the 4px spacing grid — all measurements should be multiples of 4.
- **Do** use `color-mix(in srgb, {color} 14%, transparent)` for goal-colored backgrounds to keep them subtle.
- **Do** use the `--entry-color` CSS custom property pattern for dynamic per-goal theming in calendar entries.
- **Don't** use the accent color for large surface fills — it's strictly for interactive elements and small highlights.
- **Don't** add box-shadows to static cards — shadows are reserved for hover states and floating elements only.
- **Don't** use more than two font families in any single view (display + body). Monospace is additive for data only.
- **Don't** use emoji or decorative icons. All icons are 24px stroke-based SVGs at 1.8px stroke width.
- **Don't** introduce new colors outside the defined palette. Goal-specific colors come from the 10-color goal palette only.
- **Don't** use gradients anywhere in the UI. Surfaces are flat, single-color fills.
- **Don't** round corners beyond 14px on non-pill elements. The shape language is soft, not bubbly.
