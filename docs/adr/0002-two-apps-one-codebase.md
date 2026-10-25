# Two apps from one codebase: Launcher and Caretaker App

The Owner's phone and the Caretakers' phones need different apps: the Launcher registers as a home screen and asks for overlay and accessibility permissions, which a Caretaker's phone must never be offered. We build two Android product flavors from one Gradle project instead of one APK with a role switch, and the configuration screens are shared so the local Caretaker mode on the Owner's phone and the Caretaker App edit configuration the same way.
