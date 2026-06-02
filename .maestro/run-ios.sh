#!/usr/bin/env bash
# Run the Maestro suite against an iOS simulator.
#
# Usage:
#   ./.maestro/run-ios.sh                     # run all flows
#   ./.maestro/run-ios.sh --include-tags smoke
#
# Prereqs: a booted simulator with the app installed. Build the app from
# Xcode (iosApp scheme) targeting a simulator, or install the built .app via
#   xcrun simctl install booted <path-to-Workout.app>
#
# NOTE: the iOS bundle id is derived as com.nabeelkm.workout.Workout<TEAM_ID>.
# With an empty TEAM_ID (default Config.xcconfig) it is the value below; if you
# set TEAM_ID, append it here or override APP_ID inline.
set -euo pipefail

export PATH="$HOME/.maestro/bin:$PATH"
APP_ID="${APP_ID:-com.nabeelkm.workout.Workout}"

# NOTE: Maestro does not read shell env for ${...} in flows — pass it via -e.
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
maestro test -e APP_ID="${APP_ID}" "${DIR}/flows" "$@"
