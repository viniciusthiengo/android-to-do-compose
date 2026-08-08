# ToDoCompose

A single-module Android to-do list app built entirely in Jetpack Compose (Material 2), MVVM,
Room, Hilt, DataStore Preferences, and Navigation-Compose. Package: `com.example.to_docompose`.
compileSdk/targetSdk 35, minSdk 24, Java/Kotlin target 17, Gradle wrapper 8.9, AGP 8.7.2.

## ✅ RESOLVED as of 2026-08-08 ~19:36 — Gradle sync issue fixed, real root cause below

The "Gradle sync breaks" issue documented below is now fixed and verified: sync succeeded in
Android Studio, and the only live Gradle daemon (`ps aux | grep GradleDaemon`) is running on
`~/Library/Java/JavaVirtualMachines/corretto-17.0.9/Contents/Home/bin/java`, actively polled by
Studio. The section below has been rewritten because **the original diagnosis in this file was
wrong** — worth reading even if you think you already know this issue.

**What was actually wrong (not what an earlier version of this file claimed):** at one point
this file asserted "`.idea/gradle.xml` has `gradleJvm` set to `jbr-21`, confirmed via `cat`" —
but re-checking with `git diff` showed the working copy actually had `gradleJvm="jbr-25"` at
that time. The claimed on-disk state and the real on-disk state had diverged. Don't trust a
previous session's "confirmed" claim about file contents without re-checking; `cat`/`git diff`
it yourself first.

**The deeper root cause (this is the part that matters):** `jbr-21` — and every other
`jbr-*`-named entry in `~/Library/Application Support/Google/AndroidStudio<version>/options/jdk.table.xml`
(`jbr-17`, `jbr-21`, `jbr-25`, plus bare-numbered legacy entries like `"11"` and `"25"`) — is
**not a distinct pinned JDK**. They all resolve to the same `homePath`:
`$APPLICATION_HOME_DIR$/jbr/Contents/Home`, i.e. *whatever JBR is currently bundled inside the
running Android Studio.app*. The `<version value="...">` string next to each name is just a
stale label left over from whichever Studio version first registered that name — it does not
reflect what the path currently resolves to. So setting `gradleJvm` to **any** `jbr-*` name,
including `jbr-21`, does not pin anything — it silently follows whatever JBR Studio's last
auto-update bundled. That's why the fix kept appearing to "not take" even after edits and
restarts, and why it will keep recurring if a `jbr-*` name is used again.

**The fix that's actually pinned and durable:** point Android Studio at `corretto-17` — a real,
separate JDK registered in `jdk.table.xml` at an absolute path
(`~/Library/Java/JavaVirtualMachines/corretto-17.0.9/Contents/Home`) that is **not** inside the
Studio.app bundle, so it can't drift when Studio auto-updates. This was set in two places, and
Android Studio's own sync machinery ended up owning both:

1. **Project SDK** — `.idea/misc.xml`:
   ```xml
   <component name="ProjectRootManager" version="2" languageLevel="JDK_17" project-jdk-name="corretto-17" project-jdk-type="JavaSDK">
   ```
2. **Gradle JVM** — `.idea/gradle.xml`. We initially edited this to `<option name="gradleJvm" value="corretto-17" />` directly, but **Android Studio rewrote it back to
   `<option name="gradleJvm" value="#GRADLE_LOCAL_JAVA_HOME" />` on its own during/after the
   successful sync.** That's expected, not a regression — leave it alone. With the Project SDK
   pinned to `corretto-17` in `misc.xml`, the macro now resolves through that pin rather than
   falling through to Studio's bundled runtime. Don't manually force `gradleJvm` back to an
   explicit `corretto-17` value; Studio owns this file's `gradleJvm` line now and will just
   re-normalize it again.

**If sync breaks again after a future Studio auto-update:**
1. Check `.idea/misc.xml` — is `project-jdk-name` still `corretto-17`? If Studio reset it to a
   `jbr-*` name, that's the actual regression to fix (via Settings → Project Structure →
   Project → SDK, or **Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK**
   dropdown — pick "corretto-17" / "Corretto 17.0.9", not any `jbr-*` entry).
2. Confirm no live Gradle daemon is running from `/Applications/Android Studio.app/Contents/jbr/...`:
   ```bash
   ps aux | grep GradleDaemon | grep -v grep
   ```
   If one is, kill it (`kill <pid>`) so Studio can't reattach to it, then re-sync.
3. **Never** just re-register a new `jbr-N` name and assume it stays pinned — confirm via
   `jdk.table.xml` that the name's `homePath` is an absolute path outside
   `$APPLICATION_HOME_DIR$`, otherwise it's an alias that will silently follow the next
   Studio update.

**Do not delete `.idea/` files to "fix" sync issues** — that has previously caused Android
Studio to close the project entirely. Targeted single-value edits are safe; wholesale deletions
are not.

**For plain command-line builds** (`./gradlew ...` outside the IDE, unaffected by any of the
above since it doesn't read `.idea/*.xml`), keep a JDK 17 pin in the *global*
`~/.gradle/gradle.properties` (machine-local, not part of the repo — the project's own
`gradle.properties` is committed/shared and shouldn't hardcode a machine-specific path):

```properties
org.gradle.java.home=/Users/viniciusthiengo/Library/Java/JavaVirtualMachines/corretto-17.0.9/Contents/Home
```

## Running on the emulator (verified working)

```bash
export ANDROID_HOME=~/Library/Android/sdk
export PATH=$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH

emulator -list-avds          # e.g. Pixel_10_Pro
adb devices                  # confirm an emulator/device is attached
./gradlew installDebug       # build + install
adb shell am start -n com.example.to_docompose/.MainActivity
```
