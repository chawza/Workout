#!/usr/bin/env bash
# Run the Maestro suite against an Android emulator/device.
#
# Usage:
#   ./.maestro/run-android.sh                 # run all flows
#   ./.maestro/run-android.sh --include-tags smoke   # run a subset
#
# Prereqs: an emulator/device is running and the debug APK is installed
#   (./gradlew :androidApp:installDebug).
#
# IMPORTANT: boot the emulator with hardware GPU or the Compose UI renders blank
# and taps hit nothing:  emulator -avd <name> -gpu host
set -euo pipefail

export PATH="$HOME/.maestro/bin:$HOME/Library/Android/sdk/platform-tools:$PATH"
APP_ID="com.nabeelkm.workout"

# NOTE: Maestro does not read shell env for ${...} in flows — pass it via -e.
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
maestro test -e APP_ID="${APP_ID}" "${DIR}/flows" "$@"
