package com.kxbuild.kmpgallery

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.kxbuild.kmpgallery.demos.AnimationDemo
import com.kxbuild.kmpgallery.demos.BarsDemo
import com.kxbuild.kmpgallery.demos.ButtonsDemo
import com.kxbuild.kmpgallery.demos.CanvasDemo
import com.kxbuild.kmpgallery.demos.CardsDemo
import com.kxbuild.kmpgallery.demos.CoroutinesDemo
import com.kxbuild.kmpgallery.demos.DeviceDemo
import com.kxbuild.kmpgallery.demos.DialogsDemo
import com.kxbuild.kmpgallery.demos.GesturesDemo
import com.kxbuild.kmpgallery.demos.GridsDemo
import com.kxbuild.kmpgallery.demos.ListsDemo
import com.kxbuild.kmpgallery.demos.NativeViewsDemo
import com.kxbuild.kmpgallery.demos.NetworkDemo
import com.kxbuild.kmpgallery.demos.PagerDemo
import com.kxbuild.kmpgallery.demos.PickersDemo
import com.kxbuild.kmpgallery.demos.ProgressDemo
import com.kxbuild.kmpgallery.demos.ResourcesDemo
import com.kxbuild.kmpgallery.demos.SelectionDemo
import com.kxbuild.kmpgallery.demos.StorageDemo
import com.kxbuild.kmpgallery.demos.TextDemo
import com.kxbuild.kmpgallery.demos.TextFieldsDemo

enum class Section(val title: String) {
    Components("Components"),
    Layout("Layout & Navigation"),
    Graphics("Graphics & Motion"),
    Multiplatform("Kotlin Multiplatform"),
}

/**
 * Every screen of the gallery. [id] is what the `-demo <id>` launch argument opens directly.
 * A demo with [ownsTopBar] draws its own app bar.
 */
enum class Demo(
    val id: String,
    val title: String,
    val subtitle: String,
    val section: Section,
    val icon: ImageVector,
    val ownsTopBar: Boolean = false,
    val content: @Composable () -> Unit,
) {
    Buttons("buttons", "Buttons & Chips", "Buttons, FABs, segmented buttons, chips", Section.Components, Icons.Filled.ThumbUp, content = { ButtonsDemo() }),
    Selection("selection", "Selection Controls", "Checkbox, switch, radio buttons, sliders", Section.Components, Icons.Filled.CheckCircle, content = { SelectionDemo() }),
    TextFields("textfields", "Text Fields", "Filled and outlined fields, validation, keyboards", Section.Components, Icons.Filled.Edit, content = { TextFieldsDemo() }),
    Text("text", "Text & Typography", "Type scale, styled spans, links, auto-size", Section.Components, Icons.Filled.MailOutline, content = { TextDemo() }),
    Dialogs("dialogs", "Dialogs & Menus", "Alert, bottom sheet, menu, snackbar, tooltip", Section.Components, Icons.Filled.Notifications, content = { DialogsDemo() }),
    Pickers("pickers", "Date & Time Pickers", "Date picker, date range, time picker", Section.Components, Icons.Filled.DateRange, content = { PickersDemo() }),
    Progress("progress", "Progress & Badges", "Linear and circular indicators, badges", Section.Components, Icons.Filled.Refresh, content = { ProgressDemo() }),
    Cards("cards", "Cards & Carousel", "Card styles, list items, carousel", Section.Components, Icons.Filled.AccountBox, content = { CardsDemo() }),

    Lists("lists", "Lazy Lists", "Sticky headers, swipe to delete, pull to refresh", Section.Layout, Icons.AutoMirrored.Filled.List, content = { ListsDemo() }),
    Grids("grids", "Grids & Flow Layouts", "Lazy grid, staggered grid, FlowRow", Section.Layout, Icons.Filled.Star, content = { GridsDemo() }),
    Pager("pager", "Pager & Tabs", "HorizontalPager, tabs synced with pages, page transforms", Section.Layout, Icons.AutoMirrored.Filled.ArrowForward, content = { PagerDemo() }),
    Bars("bars", "App Bars & Navigation", "Collapsing top bar, navigation bar, drawer, FAB", Section.Layout, Icons.Filled.Menu, ownsTopBar = true, content = { BarsDemo() }),

    Animation("animation", "Animation", "animate*AsState, AnimatedVisibility, AnimatedContent", Section.Graphics, Icons.Filled.PlayArrow, content = { AnimationDemo() }),
    Canvas("canvas", "Canvas & Drawing", "Shapes, paths, gradients, finger painting", Section.Graphics, Icons.Filled.Face, content = { CanvasDemo() }),
    Gestures("gestures", "Gestures", "Tap, long press, drag, pinch, rotate", Section.Graphics, Icons.Filled.Favorite, content = { GesturesDemo() }),

    Resources("resources", "Compose Resources", "Localized strings, plurals, vector images, raw files", Section.Multiplatform, Icons.Filled.Info, content = { ResourcesDemo() }),
    Device("device", "Platform APIs", "expect / actual: device info, haptics, share sheet", Section.Multiplatform, Icons.Filled.Phone, content = { DeviceDemo() }),
    NativeViews("native", "Native Views", "MapKit and UIKit controls inside Compose", Section.Multiplatform, Icons.Filled.LocationOn, content = { NativeViewsDemo() }),
    Network("network", "Networking", "Ktor client with kotlinx.serialization", Section.Multiplatform, Icons.Filled.Search, content = { NetworkDemo() }),
    Coroutines("coroutines", "Coroutines & Flow", "Flows, parallel async, debounce, cancellation", Section.Multiplatform, Icons.Filled.Build, content = { CoroutinesDemo() }),
    Storage("storage", "Key-Value Storage", "Settings and a to-do list that survive restarts", Section.Multiplatform, Icons.Filled.Settings, content = { StorageDemo() });

    companion object {
        fun byId(id: String?): Demo? = entries.firstOrNull { it.id == id }
    }
}
