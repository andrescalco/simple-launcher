# Firebase as the backend for remote configuration

Caretakers configure the Owner's launcher remotely, and more than one Caretaker (including one on iOS, later) needs to reach the same configuration. We use Firebase — Auth for Caretaker sign-in, Firestore for the configuration, Cloud Messaging for pushing changes — rather than a self-hosted server or a config file in a Git repository, because it is free at one-phone scale, works offline on the device, and can be used as-is by a future iOS app or web page.

## Consequences

- Data is stored per Owner even though there is only one, so a second Owner phone needs no migration.
- The Owner's phone never signs in with a Google account; it is linked to the Caretakers once by Pairing: the Launcher shows a 6-digit code that expires after 10 minutes, and a Caretaker types it into the Caretaker App, possibly from far away.
- The Owner's WhatsApp contacts (names, numbers, photos) are uploaded so Caretakers can pick Contacts remotely.
