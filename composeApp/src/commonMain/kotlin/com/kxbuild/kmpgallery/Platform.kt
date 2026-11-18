package com.kxbuild.kmpgallery

import androidx.compose.runtime.Composable

// Platform-specific APIs used by the demos. Each `expect` declaration has its `actual`
// implementation in iosMain (UIKit, Foundation, MapKit). Another target adds its own actuals.

enum class Haptic { Light, Medium, Heavy, Selection, Success, Warning, Error }

/** "iOS 18.5". */
expect fun platformName(): String

/** Label / value pairs describing this device. */
expect fun deviceInfo(): List<Pair<String, String>>

/** Battery charge from 0 to 1, or null when the platform does not report it. */
expect fun batteryLevel(): Float?

expect fun performHaptic(kind: Haptic)

/** Opens the system share sheet with [text]. */
expect fun shareText(text: String)

expect fun openUrl(url: String)

/** Name of the Ktor engine this platform uses. */
expect val httpEngineName: String

/** Persistent string storage (NSUserDefaults on iOS). */
expect object KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String?)
}

/** Native UI toolkit views embedded in Compose (UIKitView on iOS). */
@Composable
expect fun PlatformViews()
