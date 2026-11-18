@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.kxbuild.kmpgallery.demos

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kxbuild.kmpgallery.Caption
import com.kxbuild.kmpgallery.DemoColumn
import com.kxbuild.kmpgallery.DemoPalette
import com.kxbuild.kmpgallery.DemoSection
import com.kxbuild.kmpgallery.formatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ---------------------------------------------------------------- Buttons & Chips

@Composable
fun ButtonsDemo() = DemoColumn {
    var taps by remember { mutableIntStateOf(0) }
    DemoSection("Buttons") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { taps++ }) { Text("Filled") }
            FilledTonalButton(onClick = { taps++ }) { Text("Tonal") }
            OutlinedButton(onClick = { taps++ }) { Text("Outlined") }
            ElevatedButton(onClick = { taps++ }) { Text("Elevated") }
            TextButton(onClick = { taps++ }) { Text("Text") }
            Button(onClick = {}, enabled = false) { Text("Disabled") }
            Button(onClick = { taps++ }, contentPadding = ButtonDefaults.ButtonWithIconContentPadding) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("With icon")
            }
        }
        Caption("Tapped $taps times")
    }

    DemoSection("Icon buttons") {
        var liked by remember { mutableStateOf(false) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = {}) { Icon(Icons.Filled.Settings, contentDescription = "Settings") }
            FilledIconButton(onClick = {}) { Icon(Icons.Filled.Edit, contentDescription = "Edit") }
            FilledTonalIconButton(onClick = {}) { Icon(Icons.Filled.Share, contentDescription = "Share") }
            OutlinedIconButton(onClick = {}) { Icon(Icons.Filled.Delete, contentDescription = "Delete") }
            IconToggleButton(checked = liked, onCheckedChange = { liked = it }) {
                Icon(
                    if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (liked) DemoPalette[6] else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    DemoSection("Floating action buttons") {
        var expanded by remember { mutableStateOf(true) }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            SmallFloatingActionButton(onClick = {}) { Icon(Icons.Filled.Add, contentDescription = "Add") }
            FloatingActionButton(onClick = {}) { Icon(Icons.Filled.Edit, contentDescription = "Edit") }
            LargeFloatingActionButton(onClick = {}) { Icon(Icons.Filled.Star, contentDescription = "Star") }
        }
        ExtendedFloatingActionButton(
            text = { Text("Compose") },
            icon = { Icon(Icons.Filled.Email, contentDescription = null) },
            expanded = expanded,
            onClick = { expanded = !expanded },
        )
        Caption("Tap the extended FAB to collapse and expand it.")
    }

    DemoSection("Segmented buttons") {
        val sizes = listOf("Day", "Week", "Month")
        var selected by remember { mutableIntStateOf(1) }
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            sizes.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = index == selected,
                    onClick = { selected = index },
                    shape = SegmentedButtonDefaults.itemShape(index, sizes.size),
                ) { Text(label) }
            }
        }
        val toppings = listOf("Cheese", "Basil", "Olives")
        val checked = remember { mutableStateListOf(true, false, true) }
        MultiChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            toppings.forEachIndexed { index, label ->
                SegmentedButton(
                    checked = checked[index],
                    onCheckedChange = { checked[index] = it },
                    shape = SegmentedButtonDefaults.itemShape(index, toppings.size),
                ) { Text(label) }
            }
        }
    }

    DemoSection("Chips") {
        var filter by remember { mutableStateOf(true) }
        var input by remember { mutableStateOf(true) }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text("Directions") },
                leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null, Modifier.size(18.dp)) },
            )
            FilterChip(
                selected = filter,
                onClick = { filter = !filter },
                label = { Text("Open now") },
                leadingIcon = if (filter) {
                    { Icon(Icons.Filled.Done, contentDescription = null, Modifier.size(18.dp)) }
                } else null,
            )
            if (input) {
                InputChip(
                    selected = false,
                    onClick = { input = false },
                    label = { Text("Kotlin") },
                    trailingIcon = { Icon(Icons.Filled.Close, contentDescription = "Remove", Modifier.size(18.dp)) },
                )
            }
            SuggestionChip(onClick = { input = true }, label = { Text("Restore chip") })
        }
    }
}

// ---------------------------------------------------------------- Selection controls

@Composable
fun SelectionDemo() = DemoColumn {
    DemoSection("Checkboxes") {
        val children = remember { mutableStateListOf(true, false, false) }
        val parent = when {
            children.all { it } -> ToggleableState.On
            children.none { it } -> ToggleableState.Off
            else -> ToggleableState.Indeterminate
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TriStateCheckbox(state = parent, onClick = {
                val target = parent != ToggleableState.On
                for (i in children.indices) children[i] = target
            })
            Text("All notifications", style = MaterialTheme.typography.bodyLarge)
        }
        listOf("Messages", "Mentions", "Reminders").forEachIndexed { i, label ->
            Row(
                Modifier.padding(start = 32.dp).toggleable(
                    value = children[i], role = Role.Checkbox, onValueChange = { children[i] = it },
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = children[i], onCheckedChange = null)
                Spacer(Modifier.width(12.dp))
                Text(label)
            }
        }
    }

    DemoSection("Switches") {
        var wifi by remember { mutableStateOf(true) }
        var bluetooth by remember { mutableStateOf(false) }
        SwitchRow("Wi-Fi", wifi) { wifi = it }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Bluetooth (with icon)", Modifier.weight(1f))
            Switch(
                checked = bluetooth,
                onCheckedChange = { bluetooth = it },
                thumbContent = if (bluetooth) {
                    { Icon(Icons.Filled.Done, contentDescription = null, Modifier.size(SwitchDefaults.IconSize)) }
                } else null,
            )
        }
        SwitchRow("Disabled", checked = true, enabled = false) {}
    }

    DemoSection("Radio buttons") {
        val options = listOf("Standard delivery", "Express delivery", "Pick up in store")
        var selected by remember { mutableIntStateOf(0) }
        Column(Modifier.selectableGroup()) {
            options.forEachIndexed { i, label ->
                Row(
                    Modifier.fillMaxWidth().height(48.dp).selectable(
                        selected = i == selected, onClick = { selected = i }, role = Role.RadioButton,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = i == selected, onClick = null)
                    Spacer(Modifier.width(12.dp))
                    Text(label)
                }
            }
        }
    }

    DemoSection("Sliders") {
        var volume by remember { mutableFloatStateOf(0.4f) }
        Caption("Volume ${(volume * 100).toInt()}%")
        Slider(value = volume, onValueChange = { volume = it })
        var rating by remember { mutableFloatStateOf(3f) }
        Caption("Rating ${rating.toInt()} of 5 (stepped)")
        Slider(value = rating, onValueChange = { rating = it }, valueRange = 1f..5f, steps = 3)
        var range by remember { mutableStateOf(20f..80f) }
        Caption("Price ¥${range.start.toInt()} – ¥${range.endInclusive.toInt()}")
        RangeSlider(value = range, onValueChange = { range = it }, valueRange = 0f..100f)
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, enabled: Boolean = true, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange, enabled = enabled)
    }
}

// ---------------------------------------------------------------- Text fields

@Composable
fun TextFieldsDemo() = DemoColumn {
    val focus = LocalFocusManager.current
    DemoSection("Filled") {
        var name by remember { mutableStateOf("") }
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Ada Lovelace") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            trailingIcon = {
                if (name.isNotEmpty()) IconButton(onClick = { name = "" }) { Icon(Icons.Filled.Clear, "Clear") }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DemoSection("Outlined with validation") {
        var email by remember { mutableStateOf("") }
        val invalid = email.isNotEmpty() && !Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            isError = invalid,
            supportingText = { Text(if (invalid) "Enter a valid email address" else "We never share your email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        var password by remember { mutableStateOf("") }
        var visible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
            trailingIcon = { TextButton(onClick = { visible = !visible }) { Text(if (visible) "Hide" else "Show") } },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            supportingText = { Text("${password.length} characters") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DemoSection("Keyboards") {
        var phone by remember { mutableStateOf("") }
        OutlinedTextField(
            value = phone,
            onValueChange = { v -> phone = v.filter { it.isDigit() || it == '+' || it == ' ' } },
            label = { Text("Phone") },
            leadingIcon = { Icon(Icons.Filled.Call, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        var amount by remember { mutableStateOf("") }
        OutlinedTextField(
            value = amount,
            onValueChange = { v -> amount = v.filter { it.isDigit() || it == '.' } },
            label = { Text("Amount") },
            prefix = { Text("¥") },
            suffix = { Text("CNY") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DemoSection("Multi-line") {
        val max = 140
        var bio by remember { mutableStateOf("") }
        OutlinedTextField(
            value = bio,
            onValueChange = { if (it.length <= max) bio = it },
            label = { Text("About you") },
            supportingText = { Text("${bio.length} / $max", Modifier.fillMaxWidth()) },
            minLines = 3,
            maxLines = 6,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
            modifier = Modifier.fillMaxWidth(),
        )
        TextButton(onClick = { focus.clearFocus() }) { Text("Hide keyboard") }
    }
}

// ---------------------------------------------------------------- Text & typography

@Composable
fun TextDemo() = DemoColumn {
    DemoSection("Material type scale") {
        val type = MaterialTheme.typography
        listOf(
            "Display Small" to type.displaySmall,
            "Headline Medium" to type.headlineMedium,
            "Title Large" to type.titleLarge,
            "Title Medium" to type.titleMedium,
            "Body Large" to type.bodyLarge,
            "Body Medium" to type.bodyMedium,
            "Label Large" to type.labelLarge,
            "Label Small" to type.labelSmall,
        ).forEach { (name, style) -> Text(name, style = style) }
    }

    DemoSection("Styles") {
        Text("Light · Normal · Bold · Black", fontWeight = FontWeight.Light)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Bold", fontWeight = FontWeight.Bold)
            Text("Italic", fontStyle = FontStyle.Italic)
            Text("Underline", textDecoration = TextDecoration.Underline)
            Text("Strike", textDecoration = TextDecoration.LineThrough)
        }
        Text("fun main() = println(\"Hello\")", fontFamily = FontFamily.Monospace)
        Text("W I D E   S P A C I N G", letterSpacing = 4.sp, style = MaterialTheme.typography.labelLarge)
        Text(
            "Gradient text",
            style = TextStyle(
                brush = Brush.linearGradient(listOf(DemoPalette[0], DemoPalette[7], DemoPalette[5])),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
            ),
        )
    }

    DemoSection("Annotated string and links") {
        val primary = MaterialTheme.colorScheme.primary
        Text(
            buildAnnotatedString {
                append("One ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = primary)) { append("Text") }
                append(" can mix ")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, background = MaterialTheme.colorScheme.tertiaryContainer)) {
                    append("several styles")
                }
                append(" and open ")
                withLink(
                    LinkAnnotation.Url(
                        "https://kotlinlang.org/docs/multiplatform.html",
                        TextLinkStyles(SpanStyle(color = primary, textDecoration = TextDecoration.Underline)),
                    ),
                ) { append("the Kotlin Multiplatform docs") }
                append(".")
            },
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    DemoSection("Selection, overflow and auto-size") {
        SelectionContainer {
            Text("Long-press to select this text and copy it.", style = MaterialTheme.typography.bodyLarge)
        }
        Text(
            "This sentence is far too long to fit on one line, so it ends with an ellipsis instead of wrapping.",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        var long by remember { mutableStateOf(false) }
        Card(Modifier.fillMaxWidth()) {
            BasicText(
                if (long) "Auto-sized text shrinks to fit its box" else "Auto-size",
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(minFontSize = 12.sp, maxFontSize = 48.sp),
                style = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
        }
        TextButton(onClick = { long = !long }) { Text("Change text") }
    }
}

// ---------------------------------------------------------------- Dialogs & menus

@Composable
fun DialogsDemo() {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxSize()) {
        DemoColumn {
            var alert by remember { mutableStateOf(false) }
            var sheet by remember { mutableStateOf(false) }
            var result by remember { mutableStateOf("Nothing chosen yet") }

            DemoSection("Alert dialog") {
                Button(onClick = { alert = true }) { Text("Delete photos…") }
                Caption(result)
            }
            if (alert) {
                AlertDialog(
                    onDismissRequest = { alert = false },
                    icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
                    title = { Text("Delete 3 photos?") },
                    text = { Text("They will be removed from all your devices.") },
                    confirmButton = { TextButton(onClick = { alert = false; result = "Deleted" }) { Text("Delete") } },
                    dismissButton = { TextButton(onClick = { alert = false; result = "Cancelled" }) { Text("Cancel") } },
                )
            }

            DemoSection("Bottom sheet") {
                Button(onClick = { sheet = true }) { Text("Show bottom sheet") }
            }
            if (sheet) {
                val state = rememberModalBottomSheetState()
                ModalBottomSheet(onDismissRequest = { sheet = false }, sheetState = state) {
                    Text("Share to", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 24.dp))
                    listOf(
                        Icons.Filled.Email to "Mail", Icons.Filled.Share to "Messages",
                        Icons.Filled.Person to "Contacts", Icons.Filled.Info to "Copy link",
                    ).forEach { (icon, label) ->
                        ListItem(
                            headlineContent = { Text(label) },
                            leadingContent = { Icon(icon, contentDescription = null) },
                            modifier = Modifier.selectable(selected = false, onClick = {
                                scope.launch { state.hide() }.invokeOnCompletion { sheet = false; result = "Shared via $label" }
                            }),
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }

            DemoSection("Menu") {
                var open by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { open = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Options")
                    }
                    DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                        DropdownMenuItem(text = { Text("Edit") }, leadingIcon = { Icon(Icons.Filled.Edit, null) }, onClick = { open = false; result = "Edit" })
                        DropdownMenuItem(text = { Text("Share") }, leadingIcon = { Icon(Icons.Filled.Share, null) }, onClick = { open = false; result = "Share" })
                        HorizontalDivider()
                        DropdownMenuItem(text = { Text("Delete") }, leadingIcon = { Icon(Icons.Filled.Delete, null) }, onClick = { open = false; result = "Delete" })
                    }
                }
            }

            DemoSection("Snackbar") {
                Button(onClick = {
                    scope.launch {
                        val r = snackbar.showSnackbar("Message archived", actionLabel = "Undo", withDismissAction = true, duration = SnackbarDuration.Long)
                        result = if (r == SnackbarResult.ActionPerformed) "Archive undone" else "Archived"
                    }
                }) { Text("Archive message") }
            }

            DemoSection("Tooltip") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = { PlainTooltip { Text("Add to favorites") } },
                        state = rememberTooltipState(),
                    ) {
                        FilledTonalIconButton(onClick = {}) { Icon(Icons.Filled.Favorite, contentDescription = "Favorite") }
                    }
                    Spacer(Modifier.width(12.dp))
                    Caption("Long-press the button")
                }
            }
            Spacer(Modifier.height(72.dp))
        }
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
    }
}

// ---------------------------------------------------------------- Date & time pickers

@Composable
fun PickersDemo() = DemoColumn {
    var dateDialog by remember { mutableStateOf(false) }
    var rangeDialog by remember { mutableStateOf(false) }
    var timeDialog by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState()
    val rangeState = rememberDateRangePickerState()
    val timeState = rememberTimePickerState(initialHour = 9, initialMinute = 30, is24Hour = true)

    DemoSection("Date picker dialog") {
        Button(onClick = { dateDialog = true }) { Text("Pick a date") }
        Caption(dateState.selectedDateMillis?.let { "Selected ${formatDate(it)}" } ?: "No date selected")
    }
    DemoSection("Date range") {
        Button(onClick = { rangeDialog = true }) { Text("Pick a trip") }
        val start = rangeState.selectedStartDateMillis
        val end = rangeState.selectedEndDateMillis
        Caption(if (start != null && end != null) "${formatDate(start)} → ${formatDate(end)}" else "No range selected")
    }
    DemoSection("Time picker") {
        Button(onClick = { timeDialog = true }) { Text("Pick a time") }
        Caption("Alarm at ${timeState.hour.toString().padStart(2, '0')}:${timeState.minute.toString().padStart(2, '0')}")
    }
    DemoSection("Inline date picker") {
        DatePicker(state = rememberDatePickerState(), showModeToggle = false, title = null, headline = null)
    }

    if (dateDialog) {
        DatePickerDialog(
            onDismissRequest = { dateDialog = false },
            confirmButton = { TextButton(onClick = { dateDialog = false }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { dateDialog = false }) { Text("Cancel") } },
        ) { DatePicker(state = dateState) }
    }
    if (rangeDialog) {
        DatePickerDialog(
            onDismissRequest = { rangeDialog = false },
            confirmButton = { TextButton(onClick = { rangeDialog = false }) { Text("OK") } },
        ) {
            DateRangePicker(state = rangeState, modifier = Modifier.height(500.dp), title = {
                Text("Trip dates", modifier = Modifier.padding(start = 24.dp, top = 16.dp))
            })
        }
    }
    if (timeDialog) {
        var typing by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { timeDialog = false },
            confirmButton = { TextButton(onClick = { timeDialog = false }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { typing = !typing }) { Text(if (typing) "Dial" else "Keyboard") } },
            text = { if (typing) TimeInput(state = timeState) else TimePicker(state = timeState) },
        )
    }
}

// ---------------------------------------------------------------- Progress & badges

@Composable
fun ProgressDemo() = DemoColumn {
    var target by remember { mutableFloatStateOf(0f) }
    val progress by animateFloatAsState(target, animationSpec = tween(1500), label = "progress")
    val scope = rememberCoroutineScope()
    var downloading by remember { mutableStateOf(false) }

    DemoSection("Indeterminate") {
        LinearProgressIndicator(Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator()
            CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 3.dp)
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        }
    }

    DemoSection("Determinate") {
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(progress = { progress }, modifier = Modifier.size(64.dp), strokeWidth = 6.dp)
                Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium)
            }
            Button(
                enabled = !downloading,
                onClick = {
                    downloading = true
                    scope.launch {
                        target = 0f
                        for (step in 1..5) {
                            delay(500)
                            target = step / 5f
                        }
                        delay(1500)
                        downloading = false
                    }
                },
            ) { Text(if (downloading) "Downloading…" else "Start download") }
        }
    }

    DemoSection("Badges") {
        var count by remember { mutableIntStateOf(3) }
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
            BadgedBox(badge = { if (count > 0) Badge { Text(if (count > 99) "99+" else "$count") } }) {
                Icon(Icons.Filled.Email, contentDescription = "Mail")
            }
            BadgedBox(badge = { Badge() }) { Icon(Icons.Filled.Notifications, contentDescription = "Alerts") }
            BadgedBox(badge = { Badge(containerColor = DemoPalette[3]) { Text("New") } }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { count++ }) { Text("+1") }
            OutlinedButton(onClick = { count += 50 }) { Text("+50") }
            OutlinedButton(onClick = { count = 0 }) { Text("Clear") }
        }
    }
}

// ---------------------------------------------------------------- Cards & carousel

@Composable
fun CardsDemo() = DemoColumn {
    DemoSection("Card styles") {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(Modifier.weight(1f).height(100.dp)) { CardLabel("Filled") }
            ElevatedCard(Modifier.weight(1f).height(100.dp)) { CardLabel("Elevated") }
            OutlinedCard(Modifier.weight(1f).height(100.dp)) { CardLabel("Outlined") }
        }
        var clicks by remember { mutableIntStateOf(0) }
        Card(
            onClick = { clicks++ },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Clickable card", style = MaterialTheme.typography.titleMedium)
                Text("Tapped $clicks times", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    DemoSection("List items") {
        OutlinedCard {
            ListItem(headlineContent = { Text("One line") }, leadingContent = { Icon(Icons.Filled.Person, null) })
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Two lines") },
                supportingContent = { Text("Supporting text") },
                leadingContent = { Icon(Icons.Filled.Email, null) },
                trailingContent = { Text("9:41") },
            )
            HorizontalDivider()
            var checked by remember { mutableStateOf(true) }
            ListItem(
                overlineContent = { Text("OVERLINE") },
                headlineContent = { Text("Three lines with a checkbox") },
                supportingContent = { Text("Longer supporting text that wraps onto a second line when it needs to.") },
                trailingContent = { Checkbox(checked = checked, onCheckedChange = { checked = it }) },
            )
        }
    }

    DemoSection("Carousel") {
        val labels = listOf("Lakes", "Forest", "Desert", "Coast", "Glacier", "Canyon", "Meadow", "Island")
        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { labels.size },
            preferredItemWidth = 180.dp,
            itemSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth().height(200.dp),
        ) { i ->
            Box(
                Modifier.fillMaxSize().maskClip(MaterialTheme.shapes.extraLarge).background(
                    Brush.verticalGradient(listOf(DemoPalette[i % DemoPalette.size], DemoPalette[(i + 3) % DemoPalette.size])),
                ),
                contentAlignment = Alignment.BottomStart,
            ) {
                Text(
                    labels[i],
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
        Caption("Swipe the carousel: items grow and shrink as they move.")
    }
}

@Composable
private fun CardLabel(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text) }
}
