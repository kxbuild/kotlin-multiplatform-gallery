package com.kxbuild.kmpgallery

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIViewController

/**
 * Entry point used by the Swift app (`MainViewControllerKt.MainViewController()`).
 * Launch arguments land in NSUserDefaults, so `-demo canvas` opens that demo directly.
 */
@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController = ComposeUIViewController {
    App(initialDemo = NSUserDefaults.standardUserDefaults.stringForKey("demo"))
}
