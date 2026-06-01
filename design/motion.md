# Motion & Animation — Deep Technical Reference

This document specifies the motion system: timing tokens, easing curves, animation patterns, and per-interaction choreography.

---

## Timing Tokens

Two timing values are defined as CSS custom properties on `:root`.

| Token        | CSS Variable      | Value  | Use case                                              |
|--------------|--------------------|--------|-------------------------------------------------------|
| fast         | `--motion-fast`    | 0.15s  | Micro-interactions: hover, focus, toggle, color shifts |
| base         | `--motion-base`    | 0.25s  | Layout transitions: sheet entry, toast, fade-in        |

### Timing Principles

1. **No animation exceeds 0.25s.** The app should feel snappy and responsive, never sluggish.
2. **Hover/focus transitions use 0.15s.** Users expect near-instant feedback on pointer interactions.
3. **Entry animations use 0.25s.** Sheets, toasts, and overlays need enough time to be perceived but not enough to feel slow.
4. **No exit animations.** Dismissals are instantaneous — sheets close immediately, toasts disappear without fade-out. This follows the principle that users initiate exits intentionally and shouldn't wait.
5. **Button press is faster than hover.** The `transform: translateY(1px)` press effect uses `0.05s ease` — essentially instant — to feel physically responsive.

---

## Easing

One easing curve is defined globally:

| Token         | CSS Variable        | Value                           | Character               |
|---------------|----------------------|---------------------------------|-------------------------|
| standard      | `--ease-standard`    | `cubic-bezier(0.4, 0, 0.2, 1)` | Material Design standard|

This is the Material Design "standard" curve — starts fast, decelerates smoothly. It's used for all transitions except the button press (which uses `ease` for a simpler, snappier feel).

### Curve Behavior

```
cubic-bezier(0.4, 0, 0.2, 1)

  1.0 ─────────────────────────╮
                              ╱│
                            ╱  │
                          ╱    │
                        ╱      │
                      ╱        │
  0.0 ──────────╱──────────────│
       0.0                   1.0
```

The curve spends ~60% of its duration in the first 40% of progress, creating a "fast in, slow out" feel. Objects appear to arrive quickly and settle gently.

---

## Keyframe Animations

### `slideUp` — Bottom Sheet Entry (Mobile)

```css
@keyframes slideUp {
  from { transform: translateY(40px); opacity: 0; }
  to   { transform: translateY(0); opacity: 1; }
}
```

- **Duration:** `var(--motion-base)` (0.25s)
- **Easing:** `var(--ease-standard)`
- **Used by:** `.sheet` on mobile, `Toast` component
- **Direction:** Bottom → position (40px travel distance)

The 40px offset is intentionally less than the sheet height. A full-height slide would take too long or feel too fast. 40px provides a "peek" entrance — the sheet appears to slide up from just below its final position.

### `fadeIn` — Sheet/Modal Entry (Desktop)

```css
@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.98); }
  to   { opacity: 1; transform: scale(1); }
}
```

- **Duration:** `var(--motion-base)` (0.25s)
- **Easing:** `var(--ease-standard)`
- **Used by:** `.sheet` on desktop (≥ 1024px)
- **Effect:** 2% scale-up + fade. Creates a subtle "expand into view" without vertical movement.

Desktop uses `fadeIn` instead of `slideUp` because the sheet is centered (not bottom-aligned), so vertical sliding doesn't make spatial sense.

---

## Per-Component Motion Map

### Buttons

| Interaction | Property    | From → To         | Duration | Easing             |
|-------------|-------------|---------------------|----------|---------------------|
| Hover       | background  | default → hover     | 0.15s    | `--ease-standard`   |
| Hover       | border-color| default → hover     | 0.15s    | `--ease-standard`   |
| Press       | transform   | none → translateY(1px) | 0.05s | `ease`             |
| Release     | transform   | translateY(1px) → none | 0.05s | `ease`             |

### Cards

| Interaction | Property    | From → To                      | Duration | Easing            |
|-------------|-------------|----------------------------------|----------|-------------------|
| Hover       | box-shadow  | none → `0 2px 8px rgba(…,0.04)` | 0.15s    | `--ease-standard` |

### Toggle Group Buttons

| Interaction | Property    | From → To                | Duration | Easing            |
|-------------|-------------|---------------------------|----------|-------------------|
| Activate    | background  | transparent → `--bg`      | 0.15s    | `--ease-standard` |
| Activate    | color       | `--muted` → `--fg`        | 0.15s    | `--ease-standard` |

### Bottom Nav Links

| Interaction | Property    | From → To                | Duration | Easing    |
|-------------|-------------|---------------------------|----------|-----------|
| Activate    | color       | `--muted` → `--accent`    | 0.15s    | linear    |

### FAB

| Interaction | Property    | From → To                | Duration | Easing    |
|-------------|-------------|---------------------------|----------|-----------|
| Hover       | background  | `--accent` → `--accent-hover` | 0.15s | `--ease-standard` |
| Press       | transform   | none → `scale(0.96)`      | 0.1s     | `ease`    |

### Form Inputs

| Interaction | Property     | From → To                              | Duration | Easing            |
|-------------|--------------|------------------------------------------|----------|-------------------|
| Focus       | border-color | `--border` → `--accent`                  | 0.15s    | `--ease-standard` |
| Focus       | box-shadow   | none → `0 0 0 3px rgba(255,107,74,0.14)` | 0.15s    | `--ease-standard` |

### List Items

| Interaction | Property   | From → To                | Duration | Easing            |
|-------------|------------|---------------------------|----------|-------------------|
| Hover       | background | transparent → `--surface`  | 0.15s    | `--ease-standard` |

### Toast

| Lifecycle | Animation  | Duration | Behavior                            |
|-----------|------------|----------|--------------------------------------|
| Enter     | `slideUp`  | 0.25s    | Slides in from 40px below            |
| Visible   | —          | 1800ms   | Static, no pulse or attention anim   |
| Exit      | —          | Instant  | React unmounts the component directly |

### Switch (Toggle)

| Interaction | Property    | From → To                           | Duration |
|-------------|-------------|---------------------------------------|----------|
| Toggle      | background  | `--border` ↔ `--accent` (track)      | 0.15s    |
| Toggle      | transform   | `translateX(0)` ↔ `translateX(20px)` | 0.15s    |

### Icon Color Picker Swatches

| Interaction | Property  | From → To               | Duration | Easing            |
|-------------|-----------|---------------------------|----------|-------------------|
| Select      | transform | none → `scale(1.12)`      | 0.15s    | `--ease-standard` |

---

## Motion Principles

1. **Motion confirms, never decorates.** Every animated property has a functional purpose — confirming a state change, guiding attention, or communicating spatial relationships.

2. **No loading spinners.** The prototype uses seed data and `localStorage` — there are no async states. If async operations are added in production, use skeleton screens or progress bars, not spinners.

3. **No page transitions.** Screen changes (tab switches, push/pop navigation) are instant. React re-renders the new screen synchronously. This keeps the app feeling snappy and avoids the overhead of route-transition choreography.

4. **No scroll-linked animations.** Headers don't collapse, parallax doesn't exist, cards don't fade in on scroll. The app is dense and functional — scroll animations would conflict with the rapid scanning behavior of workout logs.

5. **Consistent timing per interaction type.** All hovers are 0.15s. All entries are 0.25s. All presses are 0.05s. This consistency builds subconscious trust — the app feels predictable and responsive.

6. **Respect reduced motion.** In production, wrap all animations in a `prefers-reduced-motion` media query. The current prototype does not implement this, but all animations are non-essential and can be disabled without losing functionality.

---

## Animation Implementation Notes

- All CSS transitions are defined in `css/app.css` on the component classes.
- The `slideUp` and `fadeIn` keyframe animations are defined in `css/app.css` within the modal/sheet section.
- The Toast animation reuses the `slideUp` keyframe via inline style in `App.jsx`.
- No JavaScript animation libraries are used. All motion is CSS-only.
- The `transition` property always uses specific properties (not `all`) to avoid animating unintended properties like `width` or `height` during layout shifts.
