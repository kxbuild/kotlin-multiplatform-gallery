@file:OptIn(ExperimentalMaterial3Api::class)

package com.kxbuild.kmpgallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

private data object HomeRoute
private data class DemoRoute(val demo: Demo)

/** Back navigation for demos that draw their own top app bar. */
val LocalNavigateBack = staticCompositionLocalOf<() -> Unit> { {} }

@Composable
fun App(initialDemo: String? = null) {
    GalleryTheme {
        val backStack = remember {
            mutableStateListOf<Any>(HomeRoute).apply {
                Demo.byId(initialDemo)?.let { add(DemoRoute(it)) }
            }
        }
        val goBack: () -> Unit = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
        CompositionLocalProvider(LocalNavigateBack provides goBack) {
            NavDisplay(
                backStack = backStack,
                onBack = goBack,
                entryProvider = entryProvider {
                    entry<HomeRoute> { HomeScreen(onOpen = { backStack.add(DemoRoute(it)) }) }
                    entry<DemoRoute> { route -> DemoScreen(route.demo, onBack = goBack) }
                },
            )
        }
    }
}

@Composable
private fun HomeScreen(onOpen: (Demo) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val matches = Demo.entries.filter {
        query.isBlank() || it.title.contains(query, ignoreCase = true) || it.subtitle.contains(query, ignoreCase = true)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("KMP Gallery", fontWeight = FontWeight.SemiBold) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding() + 24.dp),
        ) {
            item {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search demos") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) { Icon(Icons.Filled.Clear, contentDescription = "Clear") }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(28.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            Section.entries.forEach { section ->
                val demos = matches.filter { it.section == section }
                if (demos.isNotEmpty()) {
                    item(key = section.name) {
                        Text(
                            section.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp),
                        )
                    }
                    items(demos, key = { it.id }) { demo -> DemoRow(demo, onClick = { onOpen(demo) }) }
                }
            }
            if (matches.isEmpty()) {
                item {
                    Text(
                        "No demo matches \"$query\"",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp),
                    )
                }
            }
            item {
                Text(
                    "Shared Compose UI running on ${platformName()} · Kotlin ${KotlinVersion.CURRENT}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                )
            }
        }
    }
}

@Composable
private fun DemoRow(demo: Demo, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(demo.title) },
        supportingContent = { Text(demo.subtitle, maxLines = 1) },
        leadingContent = {
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(demo.icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        },
        modifier = Modifier.clickableRow(onClick),
    )
}

@Composable
private fun DemoScreen(demo: Demo, onBack: () -> Unit) {
    if (demo.ownsTopBar) {
        demo.content()
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(demo.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            demo.content()
        }
    }
}
