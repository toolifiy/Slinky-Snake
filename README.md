# Slinky Snake Adventures 2D (Android)

A vibrant, cartoonish 2D arcade Snake game built with modern Android, Kotlin, and Jetpack Compose.

## Features
- **18 Unique Snake Skins**: Custom primary and secondary colors, glowing effects, and accessories (Crowns, Sunglasses, Bandanas, Mustaches).
- **25 Adventure Levels**: Progressive stages with custom obstacles, distinct level themes, and target score thresholds.
- **Classic Endless Mode**: Retro endless snake arcade with screen wrap-around and high score tracking.
- **55+ Foods & Super Powers**:
  - ⚡ *Hyper Speed Booster*
  - 👻 *Ghost Immortality*
  - 💎 *Double Points Deal*
  - 🧲 *Magnetic Force Pull*
  - 🍄 *Shrink Shroom*
  - 🧪 *Blue Magic Potion*
  - 🔥 *Spicy Fire Chili*
  - 🍇 *Chill Grape Mode*
- **23 Star Achievements**: Persistent local achievement system tracking milestones, streaks, combos, and collector trophies.
- **Smooth 60 FPS Canvas**: Interpolated snake movement, particle explosion effects, combo badges, screen shake, and eat animations.
- **Dynamic Sound Effects**: Low-latency PCM sound synthesizer for arcade eats, crash thuds, level up fanfares, and achievement chimes.
- **Touch & D-Pad Controls**: On-screen D-Pad, swipe gestures, and hardware keyboard support (Arrow keys / WASD / Space).

## Architecture & Tech Stack
- **UI & Animation**: Jetpack Compose & Material Design 3
- **Language**: Kotlin 2.1
- **Target SDK**: Android 36
- **Architecture**: MVVM with `StateFlow`
- **Persistence**: Android `SharedPreferences`
