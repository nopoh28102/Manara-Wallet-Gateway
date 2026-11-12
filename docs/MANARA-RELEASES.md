# Manara Wallet Gateway — Signed APK Releases

## How the GitHub Actions release pipeline works

- **Tag** `v1.1.0-manara-beta.1` on the code you want to release: GitHub Actions runs full CI, builds and verifies a *signed* release-variant APK, uploads the APK plus its SHA-256 checksum as an Actions artifact, and creates (or updates) an entry under **GitHub Releases**. Tags with a hyphen are marked as **pre-releases**.
- **Manual workflow_dispatch**: Builds the same signed APK as an Actions artifact only, without creating a public Release or modifying the repository. The workflow must be present on the repository default branch before GitHub permits manual dispatch.
- **No signing secrets means no Release.** The workflow will fail clearly and will not silently fall back to signing a release with Android's debug certificate.
- The existing Docker publication job runs only on tag pushes.

## 1. Create your release signing key privately

Run this on a **trusted local computer**, not on a shared machine or a public VPS:

```bash
keytool -genkeypair -v -keystore manara-release.jks -alias manara-wallet -keyalg RSA -keysize 3072 -validity 10000
```

Choose strong passwords and keep the `manara-release.jks` file and passwords offline in secure backups. Losing the key will prevent seamless app upgrades. Do not commit the key or passwords to GitHub. Do not send them in chat.

Encode the complete keystore to a *single-line* base64 string:

Linux:

```bash
base64 -w 0 manara-release.jks
```

macOS:

```bash
base64 < manara-release.jks | tr -d '\n'
```

PowerShell:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("manara-release.jks"))
```

## 2. Add four repository secrets

GitHub repo → **Settings → Secrets and variables → Actions → New repository secret**:

| Secret | Meaning |
| --- | --- |
| `ANDROID_KEYSTORE_BASE64` | Base64 keystore content |
| `ANDROID_KEYSTORE_PASSWORD` | Password used for the keystore |
| `ANDROID_KEY_ALIAS` | Alias, e.g. `manara-wallet` |
| `ANDROID_KEY_PASSWORD` | Private-key password |

Use regular password characters without newlines or Java `.properties` escape sequences. Do not copy any secret into a GitHub issue, PR, log or application source file.

## 3. Test without publishing

After the workflow is merged into default branch, use **Actions → Release → Run workflow**, choose the desired branch and version (for example `1.1.0-manara-beta.1`). After it succeeds, download **manara-wallet-gateway-signed-release-N** from the workflow artifacts.

## 4. Publish the release

To publish the feature branch before merging, create tag **`v1.1.0-manara-beta.1`** pointing to the desired commit on `feature/manara-legacy-android` (use **Releases → Draft a new release** and select this target branch for a new tag). The tag push starts the Release workflow. The action then publishes the APK on the Releases page; a tag with `-beta` becomes a prerelease.

For later releases, create a new version tag. Do not re-use tags that already point at an older commit; published release assets should remain reproducible.

## 5. Verify before installing

Download both the APK and `.sha256` file, and verify locally using `sha256sum -c filename.apk.sha256` in the same directory. Check the GitHub Actions outcome.

**Safety:** A signed APK is not a certified payment gateway. Do not enable automatic course access based solely on SMS. Sender IDs can be spoofed, and the project requires independent transaction review, mobile-device acceptance tests (including Android 5/6), TLS certificate checks and operator/legal review before production use.
