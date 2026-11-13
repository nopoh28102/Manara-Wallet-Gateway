# Manara Wallet Listener: battery and reliability

This change reduces idle CPU activity and avoids keeping a partial wake lock for the entire life of the foreground service.

## Changes

- No unbounded partial wake lock during idle periods.
- A 60-second bounded partial wake lock is used only during upload/heartbeat I/O, and is released in `finally`.
- Idle loop wakeups change from every 15 seconds to every 60 seconds; incoming SMS still wakes the service immediately via its channel.
- Heartbeats move from every minute to every 5 minutes. Offline detection is consequently slower; adjust server alert thresholds to avoid false positives.
- SQLite queue and 15-minute WorkManager recovery remain in place.

## Limitations and operational checks

- Android/OEM battery restrictions, force-stop, device shutdown and no cellular service can prevent or delay delivery. A 24/7 guarantee cannot be provided by a third-party app.
- Ensure RECEIVE_SMS permission, background usage set to Unrestricted, auto-start enabled on supported devices, notifications allowed, network access, and no vendor sleep-list entry.
- Test with the screen off for several hours, airplane mode/reconnect, reboot, low battery, and network outage. Verify queued messages are delivered exactly once (deduplicated server-side).
- Do not disable Play Protect or screen lock to improve uptime. Keep the device secure and use a stable network and power source.
- Review the app's actual logs and server-side device heartbeat before attributing an outage to Android.

This branch is a proposed change, not yet verified by Android build or device tests.