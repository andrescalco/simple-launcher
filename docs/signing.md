# Release signing

CI signs both release APKs (`launcher` and `caretaker`) with one key on every push to `main`. Android only installs an update over the old version when it's signed with the same key, so this key must never change. Losing it means uninstalling the Launcher, and its settings with it, before a new one can go on.

## One-time setup

1. Create the key (keep the `.jks` file and both passwords somewhere safe, outside the repo):

   ```sh
   keytool -genkeypair -v -keystore release.jks -alias simple-launcher \
     -keyalg RSA -keysize 4096 -validity 36500
   ```

2. Add four repository secrets under **Settings → Secrets and variables → Actions**:

   | Secret | Value |
   | --- | --- |
   | `SIGNING_KEYSTORE_BASE64` | output of `base64 -w0 release.jks` |
   | `SIGNING_KEYSTORE_PASSWORD` | the keystore password |
   | `SIGNING_KEY_ALIAS` | `simple-launcher` |
   | `SIGNING_KEY_PASSWORD` | the key password |

Until the secrets exist, builds on `main` fail at the signing step with a message pointing here. Other branches build and test without them.

The signed APKs are attached to each `main` run as the `apks` artifact.
