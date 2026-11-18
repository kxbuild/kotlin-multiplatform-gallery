@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)

package com.kxbuild.kmpgallery.demos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kxbuild.kmpgallery.Caption
import com.kxbuild.kmpgallery.DemoPalette
import com.kxbuild.kmpgallery.LocalNavigateBack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private val Names = listOf(
    "Aiden", "Alice", "Amara", "Bao", "Bella", "Carlos", "Chen Wei", "Chloe", "Daniel", "Diego",
    "Elena", "Emma", "Fatima", "Felix", "Grace", "Hana", "Hiroshi", "Isla", "Ivan", "Jade",
    "James", "Kai", "Lena", "Li Na", "Liam", "Maya", "Mei", "Noah", "Olivia", "Omar",
    "Priya", "Quinn", "Rosa", "Ryo", "Sofia", "Tariq", "Uma", "Victor", "Wang Fang", "Yuki", "Zoe",
)

// ---------------------------------------------------------------- Lazy lists

@Composable
fun ListsDemo() {
    val contacts = remember { mutableStateListOf<String>().apply { addAll(Names) } }
    var refreshing by remember { mutableStateOf(false) }
    var added by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val groups = contacts.groupBy { it.first() }.entries.sortedBy { it.key }

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            scope.launch {
                refreshing = true
                delay(1200)
                added++
                contacts.add(0, "New contact $added")
                refreshing = false
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Box(Modifier.padding(16.dp)) {
                    Caption("Pull down to add a contact, swipe a row left to delete it. ${contacts.size} contacts.")
                }
            }
            groups.forEach { (letter, names) ->
                stickyHeader(key = "header-$letter") {
                    Text(
                        letter.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                }
                items(names, key = { it }) { name ->
                    ContactRow(name, Modifier.animateItem(), onDelete = { contacts.remove(name) })
                }
            }
        }
    }
}

@Composable
private fun ContactRow(name: String, modifier: Modifier, onDelete: () -> Unit) {
    val state = rememberSwipeToDismissBoxState()
    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) onDelete()
    }
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        modifier = modifier,
        backgroundContent = {
            Box(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer).padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
    ) {
        ListItem(
            headlineContent = { Text(name) },
            supportingContent = { Text("${name.lowercase().replace(' ', '.')}@example.com") },
            leadingContent = { Avatar(name) },
        )
    }
}

@Composable
private fun Avatar(name: String) {
    val color = DemoPalette[(name.hashCode() and 0x7fffffff) % DemoPalette.size]
    Box(Modifier.size(40.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
        Text(name.first().toString(), color = Color.White, fontWeight = FontWeight.Bold)
    }
}

// ---------------------------------------------------------------- Grids & flow layouts

@Composable
fun GridsDemo() {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Lazy grid", "Staggered", "FlowRow")
    Column(Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = tab) {
            tabs.forEachIndexed { i, title -> Tab(selected = tab == i, onClick = { tab = i }, text = { Text(title) }) }
        }
        when (tab) {
            0 -> LazyVerticalGrid(
                columns = GridCells.Adaptive(96.dp),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                listOf("Recent", "Favorites").forEachIndexed { section, title ->
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 4.dp))
                    }
                    items((1..18).toList(), key = { "$section-$it" }) { n ->
                        val color = DemoPalette[(n + section * 3) % DemoPalette.size]
                        Box(
                            Modifier.aspectRatio(1f).clip(RoundedCornerShape(16.dp)).background(color.copy(alpha = 0.85f)),
                            contentAlignment = Alignment.Center,
                        ) { Text("$n", color = Color.White, style = MaterialTheme.typography.titleLarge) }
                    }
                }
            }
            1 -> LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.fillMaxSize(),
            ) {
                items((0 until 30).toList()) { i ->
                    val height = listOf(120, 200, 160, 240, 140)[i % 5]
                    Card(Modifier.fillMaxWidth().height(height.dp)) {
                        Box(
                            Modifier.fillMaxSize().background(DemoPalette[i % DemoPalette.size].copy(alpha = 0.25f)),
                            contentAlignment = Alignment.BottomStart,
                        ) { Text("Note ${i + 1}", modifier = Modifier.padding(12.dp)) }
                    }
                }
            }
            else -> {
                val tags = listOf(
                    "Kotlin", "Compose", "iOS", "Android", "Coroutines", "Flow", "Ktor", "Serialization",
                    "Gradle", "Swift interop", "UIKit", "Material 3", "Resources", "Navigation", "Animations",
                    "Canvas", "Gestures", "Lazy layouts", "State", "Effects",
                )
                val selected = remember { mutableStateListOf("Kotlin", "iOS") }
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Caption("FlowRow wraps chips onto as many lines as they need.")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        tags.forEach { tag ->
                            FilterChip(
                                selected = tag in selected,
                                onClick = { if (tag in selected) selected.remove(tag) else selected.add(tag) },
                                label = { Text(tag) },
                            )
                        }
                    }
                    Caption("Selected: ${selected.joinToString()}")
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Pager & tabs

private val Destinations = listOf(
    "Tokyo" to "Neon streets and quiet temples",
    "Reykjavik" to "Northern lights over black sand",
    "Cape Town" to "Mountains meeting two oceans",
    "Kyoto" to "Gardens, tea houses, maple leaves",
    "Lisbon" to "Trams climbing tiled hills",
    "Queenstown" to "Lakes and alpine adventure",
)

@Composable
fun PagerDemo() {
    val pager = rememberPagerState { Destinations.size }
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize()) {
        PrimaryScrollableTabRow(selectedTabIndex = pager.currentPage, edgePadding = 8.dp) {
            Destinations.forEachIndexed { i, (city, _) ->
                Tab(
                    selected = pager.currentPage == i,
                    onClick = { scope.launch { pager.animateScrollToPage(i) } },
                    text = { Text(city) },
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        HorizontalPager(
            state = pager,
            contentPadding = PaddingValues(horizontal = 48.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) { page -> DestinationPage(page, pager) }
        PageDots(pager, Modifier.align(Alignment.CenterHorizontally).padding(24.dp))
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(
                enabled = pager.currentPage > 0,
                onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage - 1) } },
            ) { Text("Previous") }
            TextButton(
                enabled = pager.currentPage < Destinations.lastIndex,
                onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } },
            ) { Text("Next") }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun DestinationPage(page: Int, pager: PagerState) {
    val (city, blurb) = Destinations[page]
    ElevatedCard(
        Modifier.fillMaxSize().graphicsLayer {
            // Scale and fade pages as they move away from the center.
            val offset = ((pager.currentPage - page) + pager.currentPageOffsetFraction).absoluteValue.coerceIn(0f, 1f)
            val scale = lerp(0.85f, 1f, 1f - offset)
            scaleX = scale
            scaleY = scale
            alpha = lerp(0.5f, 1f, 1f - offset)
        },
        shape = RoundedCornerShape(28.dp),
    ) {
        Box(
            Modifier.fillMaxSize().background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(DemoPalette[page % DemoPalette.size], DemoPalette[(page + 2) % DemoPalette.size]),
                ),
            ),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(city, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(blurb, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun PageDots(pager: PagerState, modifier: Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pager.pageCount) { i ->
            val active = pager.currentPage == i
            Box(
                Modifier.size(width = if (active) 24.dp else 8.dp, height = 8.dp).clip(CircleShape).background(
                    if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                ),
            )
        }
    }
}

// ---------------------------------------------------------------- App bars & navigation

@Composable
fun BarsDemo() {
    val back = LocalNavigateBack.current
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val tabs = listOf(Triple("Home", Icons.Filled.Home, 0), Triple("Inbox", Icons.Filled.Email, 4), Triple("Profile", Icons.Filled.Person, 0))
    var tab by remember { mutableIntStateOf(0) }
    var folder by remember { mutableStateOf("All") }

    ModalNavigationDrawer(
        drawerState = drawer,
        drawerContent = {
            ModalDrawerSheet {
                Text("Folders", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(24.dp))
                listOf("All" to Icons.Filled.Home, "Starred" to Icons.Filled.Star, "Favorites" to Icons.Filled.Favorite, "Settings" to Icons.Filled.Settings)
                    .forEach { (label, icon) ->
                        NavigationDrawerItem(
                            label = { Text(label) },
                            icon = { Icon(icon, contentDescription = null) },
                            selected = folder == label,
                            onClick = { folder = label; scope.launch { drawer.close() } },
                            modifier = Modifier.padding(horizontal = 12.dp),
                        )
                    }
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    title = { Text("${tabs[tab].first} · $folder") },
                    navigationIcon = {
                        IconButton(onClick = back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    },
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Filled.Search, contentDescription = "Search") }
                        IconButton(onClick = { scope.launch { drawer.open() } }) { Icon(Icons.Filled.Menu, contentDescription = "Folders") }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                NavigationBar {
                    tabs.forEachIndexed { i, (label, icon, badge) ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            label = { Text(label) },
                            icon = {
                                BadgedBox(badge = { if (badge > 0) Badge { Text("$badge") } }) {
                                    Icon(icon, contentDescription = label)
                                }
                            },
                        )
                    }
                }
            },
            floatingActionButton = {
                val expanded by remember { derivedStateOf { scrollBehavior.state.collapsedFraction < 0.5f } }
                ExtendedFloatingActionButton(
                    text = { Text("New") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    expanded = expanded,
                    onClick = {},
                )
            },
        ) { padding ->
            LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
                item {
                    Box(Modifier.padding(16.dp)) {
                        Caption("Scroll to collapse the top bar and shrink the FAB. The menu button opens a navigation drawer.")
                    }
                }
                items((1..40).toList()) { n ->
                    ListItem(
                        headlineContent = { Text("${tabs[tab].first} item $n") },
                        supportingContent = { Text("In $folder") },
                        leadingContent = { Avatar(Names[(n * 7) % Names.size]) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float = start + (stop - start) * fraction
