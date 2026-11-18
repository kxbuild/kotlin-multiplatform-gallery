@file:OptIn(ExperimentalLayoutApi::class)

package com.kxbuild.kmpgallery.demos

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kxbuild.kmpgallery.Caption
import com.kxbuild.kmpgallery.DemoColumn
import com.kxbuild.kmpgallery.DemoPalette
import com.kxbuild.kmpgallery.DemoSection
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// ---------------------------------------------------------------- Animation

@Composable
fun AnimationDemo() = DemoColumn {
    DemoSection("animate*AsState") {
        var big by remember { mutableStateOf(false) }
        val size by animateDpAsState(
            if (big) 140.dp else 72.dp,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
            label = "size",
        )
        val color by animateColorAsState(if (big) DemoPalette[7] else DemoPalette[0], tween(600), label = "color")
        val corner by animateDpAsState(if (big) 70.dp else 16.dp, label = "corner")
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Box(Modifier.size(size).clip(RoundedCornerShape(corner)).background(color))
            Button(onClick = { big = !big }) { Text(if (big) "Shrink" else "Grow") }
        }
        Caption("Size uses a bouncy spring, color a 600 ms tween.")
    }

    DemoSection("AnimatedVisibility") {
        var visible by remember { mutableStateOf(true) }
        FilledTonalButton(onClick = { visible = !visible }) { Text(if (visible) "Hide card" else "Show card") }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Card(Modifier.fillMaxWidth()) {
                Text("Hello from AnimatedVisibility", modifier = Modifier.padding(24.dp))
            }
        }
    }

    DemoSection("AnimatedContent") {
        var count by remember { mutableIntStateOf(0) }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { count-- }) { Text("−") }
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                    } else {
                        slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                    }
                },
                label = "counter",
            ) { value ->
                Text("$value", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = { count++ }) { Text("+") }
        }
    }

    DemoSection("Crossfade and animateContentSize") {
        var star by remember { mutableStateOf(true) }
        var expanded by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Crossfade(targetState = star, animationSpec = tween(500), label = "icon") { s ->
                Icon(
                    if (s) Icons.Filled.Star else Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = if (s) DemoPalette[4] else DemoPalette[6],
                    modifier = Modifier.size(48.dp),
                )
            }
            OutlinedButton(onClick = { star = !star }) { Text("Crossfade") }
        }
        Card(Modifier.fillMaxWidth().animateContentSize()) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("What is Compose Multiplatform?", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    androidx.compose.material3.IconButton(onClick = { expanded = !expanded }) {
                        Icon(if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, contentDescription = "Expand")
                    }
                }
                if (expanded) {
                    Text(
                        "A declarative UI framework that shares Kotlin UI code across Android, iOS, desktop and web. " +
                            "On iOS it renders with Skia into a Metal layer and integrates with UIKit.",
                    )
                }
            }
        }
    }

    DemoSection("Infinite transition") {
        val transition = rememberInfiniteTransition(label = "infinite")
        val angle by transition.animateFloat(0f, 360f, infiniteRepeatable(tween(2400, easing = LinearEasing)), label = "angle")
        val pulse by transition.animateFloat(0.8f, 1.15f, infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "pulse")
        Row(horizontalArrangement = Arrangement.spacedBy(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(64.dp).rotate(angle).clip(RoundedCornerShape(16.dp))
                    .background(Brush.sweepGradient(DemoPalette + DemoPalette.first())),
            )
            Icon(
                Icons.Filled.Favorite, contentDescription = null, tint = DemoPalette[6],
                modifier = Modifier.size(56.dp).scale(pulse),
            )
        }
    }

    DemoSection("Animatable") {
        val offset = remember { Animatable(0f) }
        val scope = rememberCoroutineScope()
        BoxWithConstraints(Modifier.fillMaxWidth().height(56.dp)) {
            val travel = with(LocalDensity.current) { (maxWidth - 56.dp).toPx() }
            Box(
                Modifier.offset { IntOffset((offset.value * travel).roundToInt(), 0) }.size(56.dp)
                    .clip(CircleShape).background(MaterialTheme.colorScheme.tertiary),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                scope.launch {
                    offset.animateTo(if (offset.targetValue < 0.5f) 1f else 0f, spring(Spring.DampingRatioHighBouncy, Spring.StiffnessVeryLow))
                }
            }) { Text("Bounce across") }
            OutlinedButton(onClick = { scope.launch { offset.snapTo(0f) } }) { Text("Reset") }
        }
    }
}

// ---------------------------------------------------------------- Canvas & drawing

@Composable
fun CanvasDemo() = DemoColumn {
    val measurer = rememberTextMeasurer()
    val onSurface = MaterialTheme.colorScheme.onSurface

    DemoSection("Shapes and gradients") {
        Canvas(Modifier.fillMaxWidth().height(180.dp)) {
            val w = size.width / 4
            drawCircle(Brush.radialGradient(listOf(DemoPalette[4], DemoPalette[5]), center = Offset(w / 2, 60.dp.toPx()), radius = 50.dp.toPx()), 44.dp.toPx(), Offset(w / 2, 60.dp.toPx()))
            drawRoundRect(
                Brush.linearGradient(listOf(DemoPalette[0], DemoPalette[1])),
                topLeft = Offset(w + 8.dp.toPx(), 16.dp.toPx()),
                size = Size(w - 16.dp.toPx(), 88.dp.toPx()),
                cornerRadius = CornerRadius(20.dp.toPx()),
            )
            drawArc(
                Brush.sweepGradient(listOf(DemoPalette[2], DemoPalette[3], DemoPalette[2])),
                startAngle = -90f, sweepAngle = 270f, useCenter = false,
                topLeft = Offset(2 * w + 12.dp.toPx(), 20.dp.toPx()), size = Size(80.dp.toPx(), 80.dp.toPx()),
                style = Stroke(14.dp.toPx(), cap = StrokeCap.Round),
            )
            drawPath(star(Offset(3.5f * w, 60.dp.toPx()), 46.dp.toPx(), 20.dp.toPx()), DemoPalette[7])
            drawLine(
                onSurface.copy(alpha = 0.5f), Offset(0f, 140.dp.toPx()), Offset(size.width, 140.dp.toPx()),
                strokeWidth = 3.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f)),
            )
            drawText(measurer, "drawText on a Canvas", topLeft = Offset(0f, 150.dp.toPx()), style = androidx.compose.ui.text.TextStyle(color = onSurface, fontSize = 16.sp))
        }
    }

    DemoSection("Animated drawing") {
        val transition = rememberInfiniteTransition(label = "wave")
        val phase by transition.animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(2000, easing = LinearEasing)), label = "phase")
        Canvas(Modifier.fillMaxWidth().height(140.dp)) {
            val mid = size.height / 2
            listOf(0 to 1f, 1 to 0.6f, 2 to 0.35f).forEach { (i, amp) ->
                val path = Path()
                var x = 0f
                while (x <= size.width) {
                    val y = mid + sin(x / size.width * 4 * PI + phase + i).toFloat() * mid * 0.8f * amp
                    if (x == 0f) path.moveTo(x, y) else path.lineTo(x, y)
                    x += 4f
                }
                drawPath(path, DemoPalette[i * 2], style = Stroke(4.dp.toPx(), cap = StrokeCap.Round))
            }
            rotate(phase * 180 / PI.toFloat() * 2, pivot = Offset(size.width - 40.dp.toPx(), mid)) {
                drawCircle(DemoPalette[5], 8.dp.toPx(), Offset(size.width - 40.dp.toPx() + 28.dp.toPx(), mid))
            }
            drawCircle(DemoPalette[4], 12.dp.toPx(), Offset(size.width - 40.dp.toPx(), mid))
        }
    }

    DemoSection("Finger painting") {
        val strokes = remember { mutableStateListOf<List<Offset>>() }
        var current by remember { mutableStateOf<List<Offset>>(emptyList()) }
        var color by remember { mutableIntStateOf(0) }
        var width by remember { mutableFloatStateOf(8f) }
        val strokeColors = remember { mutableStateListOf<Pair<Int, Float>>() }
        Canvas(
            Modifier.fillMaxWidth().height(260.dp).clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { current = listOf(it) },
                        onDrag = { change, _ -> change.consume(); current = current + change.position },
                        onDragEnd = {
                            strokes.add(current); strokeColors.add(color to width); current = emptyList()
                        },
                    )
                },
        ) {
            fun draw(points: List<Offset>, c: Int, w: Float) {
                if (points.size < 2) return
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach { lineTo(it.x, it.y) }
                }
                drawPath(path, DemoPalette[c], style = Stroke(w.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
            strokes.forEachIndexed { i, s -> draw(s, strokeColors[i].first, strokeColors[i].second) }
            draw(current, color, width)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DemoPalette.forEachIndexed { i, c ->
                Box(
                    Modifier.size(32.dp).clip(CircleShape).background(c)
                        .border(3.dp, if (i == color) onSurface else Color.Transparent, CircleShape)
                        .pointerInput(i) { detectTapGestures { color = i } },
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(4f, 8f, 16f).forEach { w ->
                FilterChip(selected = width == w, onClick = { width = w }, label = { Text("${w.toInt()} dp") })
            }
            OutlinedButton(onClick = { strokes.clear(); strokeColors.clear() }) { Text("Clear") }
        }
    }
}

private fun star(center: Offset, outer: Float, inner: Float): Path = Path().apply {
    for (i in 0 until 10) {
        val r = if (i % 2 == 0) outer else inner
        val a = -PI / 2 + i * PI / 5
        val p = Offset(center.x + (r * cos(a)).toFloat(), center.y + (r * sin(a)).toFloat())
        if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
    }
    close()
}

// ---------------------------------------------------------------- Gestures

@Composable
fun GesturesDemo() = DemoColumn {
    DemoSection("Tap, double tap, long press") {
        var event by remember { mutableStateOf("Try a gesture") }
        var hits by remember { mutableIntStateOf(0) }
        Box(
            Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { event = "Tap at (${it.x.roundToInt()}, ${it.y.roundToInt()})"; hits++ },
                        onDoubleTap = { event = "Double tap"; hits++ },
                        onLongPress = { event = "Long press"; hits++ },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Text("$event · $hits", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }

    DemoSection("Drag") {
        var offset by remember { mutableStateOf(Offset.Zero) }
        Box(
            Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            Box(
                Modifier.offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    .padding(16.dp).size(72.dp).clip(RoundedCornerShape(18.dp)).background(DemoPalette[1])
                    .pointerInput(Unit) {
                        detectDragGestures { change, amount ->
                            change.consume()
                            offset += amount
                        }
                    },
                contentAlignment = Alignment.Center,
            ) { Text("Drag", color = Color.White) }
        }
        OutlinedButton(onClick = { offset = Offset.Zero }) { Text("Reset position") }
    }

    DemoSection("Pinch, rotate and pan") {
        var scale by remember { mutableFloatStateOf(1f) }
        var rotation by remember { mutableFloatStateOf(0f) }
        var pan by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { _, zoom, move, rotate ->
            scale = (scale * zoom).coerceIn(0.5f, 4f)
            rotation += rotate
            pan += move
        }
        Box(
            Modifier.fillMaxWidth().height(260.dp).clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .transformable(state),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier.graphicsLayer {
                    scaleX = scale; scaleY = scale; rotationZ = rotation; translationX = pan.x; translationY = pan.y
                }.size(120.dp).clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(DemoPalette[0], DemoPalette[7]))),
                contentAlignment = Alignment.Center,
            ) { Text("Pinch me", color = Color.White, fontWeight = FontWeight.Bold) }
        }
        Caption("Scale ${(scale * 100).roundToInt()}% · rotation ${rotation.roundToInt()}°")
        OutlinedButton(onClick = { scale = 1f; rotation = 0f; pan = Offset.Zero }) { Text("Reset") }
    }
}
