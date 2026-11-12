# GitHub Releases: public test previews versus signed production

This repository contains two separate workflows.

**Publish Android Preview (test only)** runs when Android source or the preview workflow changes on `main`, or when manually dispatched. It runs unit tests, builds the **debug** Android APK, then automatically creates a GitHub **prerelease** with an APK and SHA-256 checksum. No signing secrets are needed because it intentionally uses Android's development signing key.

Its public tag starts with `v0.0.0-preview.`; the APK name includes **TEST-ONLY**. Do not install it on an everyday phone with personal SMS; use a dedicated device and test messages. It does not prove the receipt of funds, and must not be used to grant paid courses or collect real money.

**Release** builds a separate release variant and requires the owner-controlled keystore configured as the four GitHub Actions secrets described in [MANARA-RELEASES.md](MANARA-RELEASES.md). Only a versioned `v*` tag triggers publishing an official signed APK and SHA-256 to Releases. The workflow refuses to publish without the secrets.

A public debug prerelease is **not** a production release, even when it appears on GitHub's Releases page.

## Downloading an APK
Open https://github.com/nopoh28102/Manara-Wallet-Gateway/releases and expand **Assets** under the latest prerelease. Download the `TEST-ONLY.apk` and checksum file.

If the preview workflow failed, inspect the run at https://github.com/nopoh28102/Manara-Wallet-Gateway/actions/workflows/publish-preview.yml and its last job logs.
