@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalMaterial3Api::class)

package com.kxbuild.kmpgallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapTypeHybrid
import platform.MapKit.MKMapTypeSatellite
import platform.MapKit.MKMapTypeStandard
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIControl
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UISlider
import platform.UIKit.UISwitch
import platform.darwin.NSObject
import platform.objc.sel_registerName

/** Receives UIKit target-action callbacks and forwards them to Kotlin. */
private class ControlTarget(private val onChange: (UIControl) -> Unit) : NSObject() {
    @ObjCAction
    fun valueChanged(sender: UIControl) = onChange(sender)
}

private fun UIControl.onValueChanged(target: ControlTarget) =
    addTarget(target, sel_registerName("valueChanged:"), UIControlEventValueChanged)

@Composable
actual fun PlatformViews() {
    DemoSection("MKMapView") {
        var mapType by remember { mutableIntStateOf(0) }
        UIKitView(
            factory = {
                MKMapView().apply {
                    val harbour = CLLocationCoordinate2DMake(22.2930, 114.1694)
                    setRegion(MKCoordinateRegionMakeWithDistance(harbour, 5000.0, 5000.0), animated = false)
                    addAnnotation(MKPointAnnotation().apply {
                        setCoordinate(harbour)
                        setTitle("Victoria Harbour")
                    })
                }
            },
            update = { map ->
                map.mapType = when (mapType) {
                    0 -> MKMapTypeStandard
                    1 -> MKMapTypeSatellite
                    else -> MKMapTypeHybrid
                }
            },
            modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(16.dp)),
        )
        val types = listOf("Standard", "Satellite", "Hybrid")
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            types.forEachIndexed { i, label ->
                SegmentedButton(
                    selected = mapType == i,
                    onClick = { mapType = i },
                    shape = SegmentedButtonDefaults.itemShape(i, types.size),
                ) { Text(label) }
            }
        }
        Caption("Compose segmented buttons switch the native map type.")
    }

    DemoSection("UISwitch") {
        var on by remember { mutableStateOf(true) }
        val target = remember { ControlTarget { on = (it as UISwitch).isOn() } }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UIKitView(
                factory = { UISwitch().apply { onValueChanged(target) } },
                update = { it.setOn(on, animated = true) },
                modifier = Modifier.size(width = 51.dp, height = 31.dp),
            )
            Text("UIKit", Modifier.width(56.dp))
            Switch(checked = on, onCheckedChange = { on = it })
            Text("Compose")
        }
    }

    DemoSection("UISlider") {
        var value by remember { mutableFloatStateOf(0.3f) }
        val target = remember { ControlTarget { value = (it as UISlider).value } }
        UIKitView(
            factory = { UISlider().apply { onValueChanged(target) } },
            update = { if (it.value != value) it.setValue(value, animated = false) },
            modifier = Modifier.fillMaxWidth().height(36.dp),
        )
        Slider(value = value, onValueChange = { value = it })
        Caption("Value ${(value * 100).toInt()} — the top slider is UISlider, the bottom one is Material.")
    }

    DemoSection("UIDatePicker") {
        var date by remember { mutableStateOf(NSDate()) }
        val target = remember { ControlTarget { date = (it as UIDatePicker).date } }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UIKitView(
                factory = {
                    UIDatePicker().apply {
                        datePickerMode = UIDatePickerMode.UIDatePickerModeDateAndTime
                        preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleCompact
                        onValueChanged(target)
                    }
                },
                update = { if (it.date.timeIntervalSince1970 != date.timeIntervalSince1970) it.setDate(date, animated = true) },
                modifier = Modifier.size(width = 240.dp, height = 40.dp),
            )
            OutlinedButton(onClick = { date = NSDate() }) { Text("Now") }
        }
        Caption("Picked: ${formatNative(date)}")
    }
}

private fun formatNative(date: NSDate): String =
    NSDateFormatter().apply { dateFormat = "yyyy-MM-dd HH:mm" }.stringFromDate(date)
