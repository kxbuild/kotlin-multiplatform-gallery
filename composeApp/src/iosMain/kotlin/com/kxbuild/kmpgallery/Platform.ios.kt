@file:OptIn(ExperimentalForeignApi::class)

package com.kxbuild.kmpgallery

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.useContents
import platform.Foundation.NSLocale
import platform.Foundation.NSProcessInfo
import platform.Foundation.NSTimeZone
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.localTimeZone
import platform.Foundation.localeIdentifier
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType
import platform.UIKit.UIScreen
import platform.UIKit.UISelectionFeedbackGenerator
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController
import platform.posix.uname
import platform.posix.utsname
import kotlin.math.roundToInt

actual fun platformName(): String = "iOS ${UIDevice.currentDevice.systemVersion}"

actual fun deviceInfo(): List<Pair<String, String>> {
    val device = UIDevice.currentDevice
    val screen = UIScreen.mainScreen
    val (width, height) = screen.bounds.useContents { size.width to size.height }
    val process = NSProcessInfo.processInfo
    val memoryGb = (process.physicalMemory.toDouble() / (1L shl 30) * 10).roundToInt() / 10.0
    val machine = memScoped {
        val info = alloc<utsname>()
        uname(info.ptr)
        info.machine.toKString()
    }
    return listOf(
        "System" to "${device.systemName} ${device.systemVersion}",
        "Device" to device.model,
        "Hardware" to machine,
        "Screen" to "${width.toInt()} × ${height.toInt()} pt @${screen.scale.toInt()}x",
        "CPU cores" to process.activeProcessorCount.toString(),
        "Memory" to "$memoryGb GB",
        "Locale" to NSLocale.currentLocale.localeIdentifier,
        "Time zone" to NSTimeZone.localTimeZone.name,
        "Kotlin" to KotlinVersion.CURRENT.toString(),
    )
}

actual fun batteryLevel(): Float? {
    val device = UIDevice.currentDevice
    device.batteryMonitoringEnabled = true
    val level = device.batteryLevel
    return if (level < 0f) null else level
}

actual fun performHaptic(kind: Haptic) {
    fun impact(style: UIImpactFeedbackStyle) = UIImpactFeedbackGenerator(style).impactOccurred()
    fun notify(type: UINotificationFeedbackType) = UINotificationFeedbackGenerator().notificationOccurred(type)
    when (kind) {
        Haptic.Light -> impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
        Haptic.Medium -> impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
        Haptic.Heavy -> impact(UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)
        Haptic.Selection -> UISelectionFeedbackGenerator().selectionChanged()
        Haptic.Success -> notify(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
        Haptic.Warning -> notify(UINotificationFeedbackType.UINotificationFeedbackTypeWarning)
        Haptic.Error -> notify(UINotificationFeedbackType.UINotificationFeedbackTypeError)
    }
}

actual fun shareText(text: String) {
    val top = topViewController() ?: return
    val sheet = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
    sheet.popoverPresentationController?.sourceView = top.view // required on iPad
    top.presentViewController(sheet, animated = true, completion = null)
}

actual fun openUrl(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(nsUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
}

actual val httpEngineName: String = "Darwin (NSURLSession)"

actual object KeyValueStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String): String? = defaults.stringForKey(key)

    actual fun putString(key: String, value: String?) {
        if (value == null) defaults.removeObjectForKey(key) else defaults.setObject(value, forKey = key)
    }
}

private fun topViewController(): UIViewController? {
    val window = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
        .firstOrNull { it.isKeyWindow() }
    var top = window?.rootViewController
    while (top?.presentedViewController != null) top = top.presentedViewController
    return top
}
