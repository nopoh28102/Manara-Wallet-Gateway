# Manara Wallet Gateway — legacy Android developer preview

This is a development branch of [Tahweel](https://github.com/ashrafeldawody/Tahweel). Original MIT license and notices remain intact.

## Scope of the first PR
- Android **5.0 / API 21** minimum (previously API 26). **Not yet verified on a real phone.**
- Separately installable Android package: `com.manara.walletgateway`; it does not overwrite Tahweel.
- API-aware notification channels (26+), immutable PendingIntent (23+), network status (21+), battery settings (23+), and notification settings (26+).
- HTTPS-only configuration for the wallet-SMS forwarding endpoint; the API token is never sent to HTTP URLs.
- **Fail-closed device SMS forwarding:** only exact, server-configured, trusted sender IDs are forwarded. Messages are not forwarded before rules are synchronized; disabling the filter does *not* open forwarding of all SMS in this fork.
- Test-message injection UI is hidden in non-debug APKs.
- A GitHub Actions job runs Android unit tests and attempts a debug APK build.

## Before running
1. Open your server administration UI and **turn off automatic matching** (`auto_match=false`) and enable manual verification. Incoming wallet SMS is not evidence of payment: sender IDs can be spoofed.
2. Keep the phone on a dedicated wallet SIM. Do not grant SMS access to unrelated apps.
3. Ensure `https://pay.mnaraty.com` is a working HTTPS reverse proxy with a certificate trusted by the exact Android versions you use. Never bypass TLS certificate checks.
4. Verify configured trusted sender IDs in server Settings *before* switching on your wallet phone. With the fail-closed rule, unlisted receipts stay on the phone and are not queued.
5. Use distinct long secret values for the ingest API, payment API, admin dashboard and webhook signature.
6. Manually check the transfer in the official wallet app or provider statements **before unlocking a paid course**. Reconcile daily.
7. Check wallet operator terms and legal/tax obligations for commercial collections. This is not an official payment processor or international card gateway.

## Android build
In `apps/listener`, run `./gradlew testDebugUnitTest assembleDebug` with JDK 21, Android SDK platform 35. Download the job artifact from the GitHub Actions workflow to test on your own device. This is a **debug APK, not a production release**.

## Known limitations
- Android 5 / 6 may not trust the TLS certificate used on your VPS. Fix the certificate chain, do **not** disable HTTPS validation.
- Background-process restrictions vary by manufacturer. Emulator compilation is insufficient; perform real device battery, reboot, network outage and multipart SMS tests.
- Client allowlists prevent accidental upload of other SMS; they do not authenticate the SMS sender.
- Incoming SMS may be missed before the first rules heartbeat. We intentionally prefer privacy to forwarding all messages before the rules arrive.
- The current server may still have `auto_match=true` (upstream default or saved setting). Check it explicitly before taking real payments.
- The application's Android package has changed, so previous Tahweel local settings are not migrated.
- No changes to the live VPS, course access integration or merchant account are included in this PR.

## Next phase
A server-enforced manual verification mode, locale-specific Arabic UX, safer QR device provisioning, delivery monitoring, and a signed release APK after device acceptance tests.
