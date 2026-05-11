# Connect 4

Connect 4 is a browser-based two-player game built with Kotlin Multiplatform and Compose HTML.

## Background

This project was created as part of a JetBrains internship assignment. I continued improving it after the initial submission to work on the feedback I received, especially around UI structure, styling, and overall project organization.

## Features

- Configurable board dimensions
- Configurable win condition
- Gravity-based piece dropping
- Win detection
- Draw detection
- Game state persistence with `localStorage`
- Unit tests for core game logic

## Tech Stack

- Kotlin Multiplatform
- Compose HTML
- Gradle

## Running the Project

Run the development server:

```bash
./gradlew jsBrowserDevelopmentRun
```

Create a production bundle:

```bash
./gradlew jsBrowserProductionWebpack
```

The production output is generated in `build/dist`.

## Running Tests

Run the JavaScript test suite:

```bash
./gradlew jsTest
```

## Project Structure

- `src/main/kotlin/game` - core game logic
- `src/main/kotlin/ui` - Compose HTML screens and app state
- `src/main/kotlin/persistence` - save/load logic using browser storage
- `src/main/resources` - static resources such as `index.html` and `style.css`
- `src/jsTest/kotlin/game` - unit tests for game logic

## Notes

The UI is implemented with Compose HTML and styled through a linked CSS file in `src/main/resources/style.css`.

## Project Pictures
Menu:
<img width="1920" height="959" alt="screencapture-localhost-8080-2026-05-11-09_37_05" src="https://github.com/user-attachments/assets/8fce4f6d-8364-477d-b6dd-b5cba2f1808c" />
Game:
<img width="1920" height="959" alt="screencapture-localhost-8080-2026-05-11-09_40_00 (2)" src="https://github.com/user-attachments/assets/3ad80338-3c53-45f6-a988-78549a9c8da3" />




