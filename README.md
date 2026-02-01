 # Food-Planner

Food-Planner is an Android application that helps users plan meals, manage shopping lists, and track recipes. This repository contains the Android project (Gradle-based) and is intended to be opened with Android Studio or built from the command line using the included Gradle wrapper.

## Table of contents

- Project overview
- Features
- Tech stack
- Prerequisites
- Quick start (build & run)
- Running on device / emulator
- Configuration
- Testing
- Troubleshooting
- Contributing
- License

## Project overview

Food-Planner provides a user-friendly interface to create weekly meal plans, add recipes, and produce shopping lists. The app stores project configuration in the `app/` module and uses `google-services.json` for any Firebase configuration that may be included.

## Features

- Create, edit and remove meal plans
- Add and store recipes and associated ingredients
- Generate shopping lists from chosen recipes
- Simple local storage (and optional cloud sync if configured)
- Debug-friendly structure suitable for iterative development in Android Studio

## Tech stack

- Android (Kotlin/Java - project uses the standard Android Gradle project structure)
- Gradle (wrapper included)
- Optional: Firebase (if `google-services.json` is present and configured)

## Detected versions (from project files)

- compileSdkVersion: 36 (`app/build.gradle`)
- targetSdkVersion: 36 (`app/build.gradle`)
- minSdkVersion: 24 (`app/build.gradle`)
- Android Gradle Plugin (AGP): 8.13.2 (`gradle/libs.versions.toml`)
- Gradle wrapper: 8.13 (`gradle/wrapper/gradle-wrapper.properties`)
- Gradle JVM / build JDK configured: Java 21 (`org.gradle.java.home` in `gradle.properties`)
- Java source/target compatibility for the app module: Java 11 (`sourceCompatibility` / `targetCompatibility` in `app/build.gradle`)

Note: The project compiles Java source for Java 11 compatibility, but the Gradle build is configured to run with a Java 21 JVM (see `gradle.properties`). Use JDK 21 for running Gradle and Android Studio if you follow the repository settings. Android Studio may also supply a compatible JDK automatically.

## Prerequisites

Before building or running the app, make sure you have:

- Java JDK (version compatible with the project's Gradle configuration; commonly Java 11 or 17)
- Android SDK (with appropriate platform and build-tools)
- Android Studio (recommended) or PowerShell / terminal for command-line builds
- A device or emulator to run the app (or use Android Studio's emulator manager)

If you don't have Android Studio, install it from https://developer.android.com/studio. Install the SDK components requested by the project when opening it.

## Quick start (build & run)

Open in Android Studio (recommended):

1. Launch Android Studio.
2. Choose "Open an existing project" and select this repository root.
3. Let Gradle sync and download any required dependencies.
4. Select an emulator or connected device and press Run.

Command-line (Windows PowerShell):

1. Open PowerShell and cd to the repository root (where `gradlew.bat` is located).

```powershell
# Assemble a debug APK
.\gradlew.bat assembleDebug

# Install the APK to a connected device/emulator (debug build)
.\gradlew.bat installDebug
```

2. If you need just the APK file, find it at `app\build\outputs\apk\debug\app-debug.apk` after assemble.

## Running on device / emulator

- To run on an Android emulator: create an AVD in Android Studio (AVD Manager) and start it, then Run the app from Android Studio or use `installDebug` from the CLI.
- To run on a physical device: enable Developer Options and USB Debugging on the device, connect via USB, accept the prompt on device, then run from Android Studio or `installDebug`.

## Configuration

- `app/google-services.json`: If present, it contains Firebase project configuration. Keep it inside the `app/` folder.
- Build variants: check `app/build.gradle` for product flavors or build types if your workflow uses them.
- Secrets / API keys: Do NOT commit secrets. Use environment variables or a local gradle.properties (excluded from VCS) for private keys.

## Testing

- Unit tests are located under `app/src/test/`.
- Instrumented UI tests are under `app/src/androidTest/` and can be run on an emulator or device through Android Studio or with Gradle:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat connectedDebugAndroidTest
```

## Troubleshooting

- Gradle sync errors: Run a sync in Android Studio and check the SDK/platform versions. Install missing SDK components.
- Build fails due to Java version: ensure your `JAVA_HOME` points to a compatible JDK. On Windows, check `java -version` in PowerShell.
- Missing `google-services.json`: If the project expects Firebase, add the correct file to `app/` or disable Firebase features in the code.
- Device not detected: ensure USB drivers are installed (Windows) and `adb devices` shows your device.

## Contributing

1. Fork the repository and create a feature branch.
2. Make small, well-scoped commits.
3. Open a pull request describing changes and testing steps.

If you plan to add larger features (cloud sync, authentication), open an issue first to discuss the design.

## License

Add the appropriate license information here (for example MIT, Apache 2.0). If you don't have a license yet, consider adding one to clarify reuse terms.

---

If you'd like, I can also:

- detect the project's compile SDK / target SDK and JDK version and add them to the README,
- add sample screenshots or a short usage guide, or
- generate a CONTRIBUTING.md and a small checklist for reviewers.

Let me know which of these you'd like next.
