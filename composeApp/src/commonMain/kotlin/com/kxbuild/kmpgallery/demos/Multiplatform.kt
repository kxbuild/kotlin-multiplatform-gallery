@file:OptIn(ExperimentalLayoutApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.kxbuild.kmpgallery.demos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.kxbuild.kmpgallery.Caption
import com.kxbuild.kmpgallery.DemoColumn
import com.kxbuild.kmpgallery.DemoPalette
import com.kxbuild.kmpgallery.DemoSection
import com.kxbuild.kmpgallery.Haptic
import com.kxbuild.kmpgallery.KeyValueStore
import com.kxbuild.kmpgallery.PlatformViews
import com.kxbuild.kmpgallery.batteryLevel
import com.kxbuild.kmpgallery.deviceInfo
import com.kxbuild.kmpgallery.httpEngineName
import com.kxbuild.kmpgallery.openUrl
import com.kxbuild.kmpgallery.performHaptic
import com.kxbuild.kmpgallery.platformName
import com.kxbuild.kmpgallery.resources.Res
import com.kxbuild.kmpgallery.resources.apples
import com.kxbuild.kmpgallery.resources.greeting
import com.kxbuild.kmpgallery.resources.language_name
import com.kxbuild.kmpgallery.resources.multiplatform
import com.kxbuild.kmpgallery.resources.resources_title
import com.kxbuild.kmpgallery.resources.seasons
import com.kxbuild.kmpgallery.shareText
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random
import kotlin.time.TimeSource

// ---------------------------------------------------------------- Compose resources

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ResourcesDemo() = DemoColumn {
    DemoSection("Strings") {
        Text(stringResource(Res.string.resources_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(Res.string.greeting, "Kotlin", 3))
        Caption("Current resource language: ${stringResource(Res.string.language_name)}. Switch the phone to Chinese to see the zh strings.")
    }

    DemoSection("Plurals") {
        var count by remember { mutableIntStateOf(1) }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { if (count > 0) count-- }) { Text("−") }
            Text(pluralStringResource(Res.plurals.apples, count, count), style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = { count++ }) { Text("+") }
        }
    }

    DemoSection("String arrays") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            stringArrayResource(Res.array.seasons).forEach { AssistChip(onClick = {}, label = { Text(it) }) }
        }
    }

    DemoSection("Vector drawable") {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(Res.drawable.multiplatform), contentDescription = null, modifier = Modifier.size(96.dp))
            Image(painterResource(Res.drawable.multiplatform), contentDescription = null, modifier = Modifier.size(48.dp))
            Image(painterResource(Res.drawable.multiplatform), contentDescription = null, modifier = Modifier.size(24.dp))
        }
        Caption("One XML vector from composeResources/drawable, sharp at every size.")
    }

    DemoSection("Raw files") {
        val text by produceState("Loading…") {
            value = runCatching { Res.readBytes("files/planets.json").decodeToString() }.getOrElse { "Error: ${it.message}" }
        }
        val planets = remember(text) {
            runCatching { Json.decodeFromString<List<Planet>>(text) }.getOrNull()
        }
        if (planets == null) {
            Caption(text)
        } else {
            OutlinedCard {
                planets.forEachIndexed { i, p ->
                    ListItem(
                        headlineContent = { Text(p.name) },
                        supportingContent = { Text("${p.moons} moons · ${p.distanceAu} AU from the Sun") },
                    )
                    if (i < planets.lastIndex) HorizontalDivider()
                }
            }
            Caption("Parsed from files/planets.json with kotlinx.serialization.")
        }
    }
}

@Serializable
private data class Planet(val name: String, val moons: Int, @SerialName("distance_au") val distanceAu: Double)

// ---------------------------------------------------------------- Platform APIs

@Composable
fun DeviceDemo() = DemoColumn {
    DemoSection("expect / actual") {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)) {
            Text(
                "// commonMain\nexpect fun deviceInfo(): List<Pair<String, String>>\n\n" +
                    "// iosMain\nactual fun deviceInfo() = listOf(\n    \"Device\" to UIDevice.currentDevice.model, …)",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp),
            )
        }
    }

    DemoSection("This device") {
        val info = remember { deviceInfo() }
        OutlinedCard {
            info.forEachIndexed { i, (label, value) ->
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Text(label, Modifier.width(120.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(value, fontWeight = FontWeight.Medium)
                }
                if (i < info.lastIndex) HorizontalDivider()
            }
        }
        val battery = remember { batteryLevel() }
        if (battery != null) {
            Caption("Battery ${(battery * 100).toInt()}%")
            LinearProgressIndicator(progress = { battery }, modifier = Modifier.fillMaxWidth())
        }
    }

    DemoSection("Haptics") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Haptic.entries.forEach { kind ->
                FilledTonalButton(onClick = { performHaptic(kind) }) { Text(kind.name) }
            }
        }
        Caption("Calls UIImpactFeedbackGenerator, UISelectionFeedbackGenerator and UINotificationFeedbackGenerator from Kotlin.")
    }

    DemoSection("Share and open links") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { shareText("Built with Kotlin Multiplatform and Compose Multiplatform") }) {
                Icon(Icons.Filled.Share, contentDescription = null)
                Text("  Share")
            }
            OutlinedButton(onClick = { openUrl("https://kotlinlang.org/docs/multiplatform.html") }) { Text("Open kotlinlang.org") }
        }
    }
}

// ---------------------------------------------------------------- Native views

@Composable
fun NativeViewsDemo() = DemoColumn {
    Caption(
        "These views are real ${platformName().substringBefore(' ')} views placed in the Compose layout. " +
            "Compose state and native controls update each other.",
    )
    PlatformViews()
}

// ---------------------------------------------------------------- Networking

@Serializable
private data class Repo(
    @SerialName("full_name") val fullName: String,
    val description: String? = null,
    @SerialName("stargazers_count") val stars: Int = 0,
    @SerialName("forks_count") val forks: Int = 0,
    @SerialName("open_issues_count") val openIssues: Int = 0,
    val language: String? = null,
    val license: License? = null,
    @SerialName("html_url") val htmlUrl: String = "",
)

@Serializable
private data class License(val name: String? = null)

private val httpClient by lazy {
    HttpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        install(HttpTimeout) { requestTimeoutMillis = 15_000 }
        defaultRequest { header("User-Agent", "KMP-Gallery") }
    }
}

private sealed interface Load {
    data object Idle : Load
    data object Loading : Load
    data class Done(val repo: Repo, val millis: Long) : Load
    data class Failed(val message: String) : Load
}

@Composable
fun NetworkDemo() = DemoColumn {
    var query by remember { mutableStateOf("JetBrains/compose-multiplatform") }
    var state by remember { mutableStateOf<Load>(Load.Idle) }
    val scope = rememberCoroutineScope()
    val fetch: (String) -> Unit = { name ->
        query = name
        scope.launch {
            state = Load.Loading
            val mark = TimeSource.Monotonic.markNow()
            state = try {
                val response = httpClient.get("https://api.github.com/repos/${name.trim()}")
                if (response.status.isSuccess()) {
                    Load.Done(response.body<Repo>(), mark.elapsedNow().inWholeMilliseconds)
                } else {
                    Load.Failed("HTTP ${response.status.value} ${response.status.description}")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Platform errors (NSError, IOException) can be long; the first line says what happened.
                val message = (e.message ?: e.toString()).lineSequence().first().take(160)
                Load.Failed("Request failed: $message")
            }
        }
    }
    LaunchedEffect(Unit) { fetch(query) }

    DemoSection("GitHub repository lookup") {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("owner/repository") },
            singleLine = true,
            trailingIcon = { IconButton(onClick = { fetch(query) }) { Icon(Icons.Filled.Search, contentDescription = "Fetch") } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { fetch(query) }),
            modifier = Modifier.fillMaxWidth(),
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("JetBrains/kotlin", "JetBrains/compose-multiplatform", "ktorio/ktor", "Kotlin/kotlinx.coroutines")
                .forEach { AssistChip(onClick = { fetch(it) }, label = { Text(it.substringAfter('/')) }) }
        }
    }

    when (val s = state) {
        Load.Idle -> {}
        Load.Loading -> Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(Modifier.size(24.dp))
            Text("  GET api.github.com …")
        }
        is Load.Failed -> Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
            Text(s.message, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer)
        }
        is Load.Done -> OutlinedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(s.repo.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                s.repo.description?.let { Text(it) }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = DemoPalette[4])
                    Text(formatCount(s.repo.stars))
                    Text("Forks ${formatCount(s.repo.forks)}")
                    Text("Issues ${formatCount(s.repo.openIssues)}")
                }
                Caption(listOfNotNull(s.repo.language, s.repo.license?.name).joinToString(" · "))
                TextButton(onClick = { openUrl(s.repo.htmlUrl) }) { Text("Open on GitHub") }
            }
        }
    }
    Caption("Ktor engine: $httpEngineName. JSON decoded into @Serializable data classes.")
}

private fun formatCount(n: Int): String = if (n >= 1000) "${n / 1000}.${(n % 1000) / 100}k" else "$n"

// ---------------------------------------------------------------- Coroutines & Flow

@Composable
fun CoroutinesDemo() = DemoColumn {
    val scope = rememberCoroutineScope()

    DemoSection("A cancellable ticker") {
        var ticks by remember { mutableIntStateOf(0) }
        var job by remember { mutableStateOf<Job?>(null) }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("$ticks s", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Button(onClick = {
                job = if (job?.isActive == true) {
                    job?.cancel(); null
                } else {
                    scope.launch { while (true) { delay(1000); ticks++ } }
                }
            }) { Text(if (job?.isActive == true) "Stop" else "Start") }
            TextButton(onClick = { ticks = 0 }) { Text("Reset") }
        }
    }

    DemoSection("Parallel async / awaitAll") {
        val progress = remember { mutableStateListOf(0f, 0f, 0f) }
        var summary by remember { mutableStateOf("Three downloads of random length run at the same time.") }
        var running by remember { mutableStateOf(false) }
        listOf("profile.json", "avatar.png", "feed.json").forEachIndexed { i, name ->
            Text(name, style = MaterialTheme.typography.labelLarge)
            LinearProgressIndicator(progress = { progress[i] }, modifier = Modifier.fillMaxWidth(), color = DemoPalette[i * 2])
        }
        Button(enabled = !running, onClick = {
            running = true
            scope.launch {
                val mark = TimeSource.Monotonic.markNow()
                val durations = coroutineScope {
                    (0..2).map { i ->
                        async {
                            val steps = Random.nextInt(10, 30)
                            for (s in 1..steps) {
                                delay(60)
                                progress[i] = s.toFloat() / steps
                            }
                            steps * 60L
                        }
                    }.awaitAll()
                }
                summary = "Done in ${mark.elapsedNow().inWholeMilliseconds} ms; one after another would take ${durations.sum()} ms."
                running = false
            }
        }) { Text(if (running) "Running…" else "Run in parallel") }
        Caption(summary)
    }

    DemoSection("Debounced search with Flow") {
        var query by remember { mutableStateOf("") }
        var searches by remember { mutableIntStateOf(0) }
        val words = remember {
            listOf(
                "async", "await", "buffer", "callbackFlow", "channel", "collect", "combine", "conflate", "coroutineScope",
                "debounce", "delay", "dispatcher", "emit", "filter", "flatMapLatest", "flow", "job", "launch",
                "mapLatest", "mutex", "produce", "scope", "select", "sharedFlow", "stateFlow", "supervisor", "withContext", "zip",
            )
        }
        var results by remember { mutableStateOf(words) }
        LaunchedEffect(Unit) {
            snapshotFlow { query }
                .debounce(300)
                .distinctUntilChanged()
                .mapLatest { q -> searches++; delay(100); words.filter { it.contains(q, ignoreCase = true) } }
                .collect { results = it }
        }
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Filter coroutine APIs") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Caption("${results.size} matches · searches run: $searches (typing fast runs one search)")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            results.take(14).forEach { AssistChip(onClick = {}, label = { Text(it, fontFamily = FontFamily.Monospace) }) }
        }
    }

    DemoSection("CPU work on Dispatchers.Default") {
        var result by remember { mutableStateOf("Count the primes below 2,000,000 off the main thread.") }
        var busy by remember { mutableStateOf(false) }
        var job by remember { mutableStateOf<Job?>(null) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(enabled = !busy, onClick = {
                busy = true
                job = scope.launch {
                    val mark = TimeSource.Monotonic.markNow()
                    result = try {
                        val count = withContext(Dispatchers.Default) { countPrimes(2_000_000) }
                        "$count primes in ${mark.elapsedNow().inWholeMilliseconds} ms"
                    } catch (e: CancellationException) {
                        "Cancelled after ${mark.elapsedNow().inWholeMilliseconds} ms"
                    }
                    busy = false
                }
            }) { Text("Count primes") }
            OutlinedButton(enabled = busy, onClick = { job?.cancel() }) { Text("Cancel") }
        }
        if (busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        Caption(result)
    }
}

private suspend fun countPrimes(limit: Int): Int = coroutineScope {
    val sieve = BooleanArray(limit) { it >= 2 }
    var i = 2
    while (i.toLong() * i < limit) {
        ensureActive()
        if (sieve[i]) {
            var j = i * i
            while (j < limit) { sieve[j] = false; j += i }
        }
        i++
    }
    // Count in chunks so cancellation is noticed quickly.
    var count = 0
    for (start in 0 until limit step 100_000) {
        ensureActive()
        for (k in start until minOf(limit, start + 100_000)) if (sieve[k]) count++
    }
    count
}

// ---------------------------------------------------------------- Key-value storage

@Serializable
private data class Todo(val title: String, val done: Boolean = false)

private const val TodosKey = "todos"

@Composable
fun StorageDemo() = DemoColumn {
    DemoSection("Settings") {
        var name by remember { mutableStateOf(KeyValueStore.getString("name") ?: "") }
        var notifications by remember { mutableStateOf(KeyValueStore.getString("notifications") != "off") }
        var launches by remember { mutableIntStateOf(0) }
        LaunchedEffect(Unit) {
            val n = (KeyValueStore.getString("opened")?.toIntOrNull() ?: 0) + 1
            KeyValueStore.putString("opened", n.toString())
            launches = n
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; KeyValueStore.putString("name", it) },
            label = { Text("Display name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Notifications", Modifier.weight(1f))
            Switch(checked = notifications, onCheckedChange = {
                notifications = it
                KeyValueStore.putString("notifications", if (it) "on" else "off")
            })
        }
        Caption("You opened this demo ${if (launches == 1) "once" else "$launches times"}. Quit the app and come back: the values stay.")
    }

    DemoSection("To-do list stored as JSON") {
        val todos = remember {
            mutableStateListOf<Todo>().apply {
                val saved = KeyValueStore.getString(TodosKey)
                addAll(saved?.let { runCatching { Json.decodeFromString<List<Todo>>(it) }.getOrNull() } ?: listOf(
                    Todo("Write the shared Kotlin code", done = true),
                    Todo("Build the iOS app"),
                    Todo("Ship it"),
                ))
            }
        }
        fun save() = KeyValueStore.putString(TodosKey, Json.encodeToString(todos.toList()))
        var draft by remember { mutableStateOf("") }
        val add = {
            if (draft.isNotBlank()) { todos.add(Todo(draft.trim())); draft = ""; save() }
        }
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it },
            label = { Text("New task") },
            singleLine = true,
            trailingIcon = { IconButton(onClick = add) { Icon(Icons.Filled.Add, contentDescription = "Add") } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { add() }),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedCard {
            if (todos.isEmpty()) Text("Nothing to do", Modifier.padding(16.dp))
            todos.forEachIndexed { i, todo ->
                ListItem(
                    headlineContent = { Text(todo.title) },
                    leadingContent = {
                        Checkbox(checked = todo.done, onCheckedChange = { todos[i] = todo.copy(done = it); save() })
                    },
                    trailingContent = {
                        IconButton(onClick = { todos.removeAt(i); save() }) { Icon(Icons.Filled.Delete, contentDescription = "Delete") }
                    },
                )
            }
        }
        Caption("${todos.count { it.done }} of ${todos.size} done")
    }
}
