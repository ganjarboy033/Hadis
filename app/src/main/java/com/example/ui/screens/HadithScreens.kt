package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hadith
import com.example.data.model.TranslationLanguage
import com.example.data.model.UserRole
import com.example.data.model.WordTranslation
import com.example.ui.components.DeveloperHadithEditorDialog
import com.example.ui.components.EducationalAdAndDonationBanner
import com.example.ui.components.WordByWordTable
import com.example.ui.localization.getUiStrings
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HadithListScreen(
    viewModel: MainViewModel,
    onNavigateToDonation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hadiths by viewModel.hadithsWithProgress.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCat by viewModel.selectedCategory.collectAsState()
    val onlyFav by viewModel.onlyFavorites.collectAsState()
    val activeDetail by viewModel.selectedHadithDetail.collectAsState()
    val transLang by viewModel.translationLanguage.collectAsState()
    val appLang by viewModel.appLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val strings = getUiStrings(appLang)
    var devEditorHadithId by remember { mutableStateOf<Int?>(null) }

    // If detail is active, show detail screen with BackHandler
    if (activeDetail != null) {
        HadithDetailScreen(
            hadith = activeDetail!!,
            viewModel = viewModel,
            onBack = { viewModel.closeHadithDetail() },
            onOpenChat = { hadithId ->
                viewModel.closeHadithDetail()
                viewModel.setSlideIndex(1)
                viewModel.openChatRoom("hadith_$hadithId")
            }
        )
        return
    }

    val categories = listOf(
        "Semua",
        "Niat & Keikhlasan",
        "Rukun Agama & Ihsan",
        "Rukun Islam",
        "Akidah & Takdir",
        "Syariat & Sunnah",
        "Wara' & Pensucian Hati",
        "Ukhuwah & Cinta Sesama",
        "Adab Sosial & Lisan",
        "Zuhud & Ketenangan Hati",
        "Solidaritas & Majelis Ilmu"
    )

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("hadith_list_screen")
    ) {
        // Translation Language quick selector bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = strings.translationLanguageLabel + ":",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TranslationLanguage.values().forEach { lang ->
                    val isSel = transLang == lang
                    Surface(
                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable {
                            viewModel.translationLanguage.value = lang
                            Toast.makeText(context, "${strings.translationLanguageLabel}: ${lang.label}", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(
                            text = when (lang) {
                                TranslationLanguage.INDONESIA -> "🇮🇩 ID"
                                TranslationLanguage.SUNDA -> "🌾 Sunda"
                                TranslationLanguage.ENGLISH -> "🇬🇧 EN"
                            },
                            color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            fontSize = 10.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Developer Mode Quick Banner
        if (currentUser.role == UserRole.DEVELOPER) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6366F1).copy(alpha = 0.12f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = strings.textEditorDevTitle, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF4338CA))
                            Text(text = "Ubah naskah Arab & terjemahan 3 bahasa (ID, Sunda, EN)", style = MaterialTheme.typography.labelSmall, color = Color(0xFF4338CA).copy(alpha = 0.8f))
                        }
                    }
                    Button(
                        onClick = { devEditorHadithId = 1 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp).testTag("open_dev_editor_top_list_button")
                    ) {
                        Text("Buka Editor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Search and filter bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchQuery.value = it },
            placeholder = { Text(strings.searchHadithPlaceholder) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("hadith_search_input"),
            shape = RoundedCornerShape(12.dp)
        )

        // Filter chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = onlyFav,
                onClick = { viewModel.onlyFavorites.value = !onlyFav },
                label = { Text("⭐ Favorit") },
                modifier = Modifier.testTag("favorite_filter_chip")
            )

            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCat == cat,
                    onClick = { viewModel.selectedCategory.value = cat },
                    label = { Text(cat) }
                )
            }
        }

        // Educational / Infaq banner
        EducationalAdAndDonationBanner(
            onDonationClick = onNavigateToDonation,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Hadith list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total: ${hadiths.size} Hadits Arbain",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Real-time Live Sync Aktif",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            items(hadiths, key = { it.id }) { hadith ->
                HadithListItemCard(
                    hadith = hadith,
                    transLang = transLang,
                    onItemClick = { viewModel.openHadithDetail(hadith) },
                    onToggleFavorite = { viewModel.toggleFavorite(hadith.id, hadith.isFavorite) },
                    onOpenChat = {
                        viewModel.setSlideIndex(1)
                        viewModel.openChatRoom("hadith_${hadith.id}")
                    },
                    onEditDev = if (currentUser.role == UserRole.DEVELOPER) {
                        { devEditorHadithId = hadith.id }
                    } else null
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (devEditorHadithId != null) {
            DeveloperHadithEditorDialog(
                initialHadithId = devEditorHadithId!!,
                viewModel = viewModel,
                onDismiss = { devEditorHadithId = null }
            )
        }
    }
}

@Composable
fun HadithListItemCard(
    hadith: Hadith,
    transLang: TranslationLanguage = TranslationLanguage.INDONESIA,
    onItemClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenChat: () -> Unit,
    onEditDev: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .testTag("hadith_item_${hadith.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hadith Number circle
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${hadith.number}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hadith.getTitle(transLang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = hadith.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (onEditDev != null) {
                    IconButton(
                        onClick = onEditDev,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Teks (Developer)",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (hadith.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favorit",
                        tint = if (hadith.isFavorite) Color(0xFFE5A100) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Arabic Preview
            Text(
                text = hadith.arabicText,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Short Translation preview according to transLang
            Text(
                text = hadith.getTranslation(transLang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Memorization status chip
                val (memText, memColor) = when (hadith.memorizationLevel) {
                    2 -> Pair("Hafal ✓", Color(0xFF10B981))
                    1 -> Pair("Sedang Menghafal...", Color(0xFFF59E0B))
                    else -> Pair("Belum Hafal", Color.Gray)
                }
                Surface(
                    color = memColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = memText,
                        color = memColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Chat block shortcut button
                Button(
                    onClick = onOpenChat,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Blok Chat",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithDetailScreen(
    hadith: Hadith,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onOpenChat: (Int) -> Unit
) {
    // Intercept back button to return cleanly
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    var fontSize by remember { mutableStateOf(24f) }
    var isPlayingAudio by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableStateOf("1.0x") }
    var repeatCount by remember { mutableStateOf(3) }
    var showQuizDialog by remember { mutableStateOf(false) }
    val transLang by viewModel.translationLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var showDevEditor by remember { mutableStateOf(false) }

    val activeTitle = hadith.getTitle(transLang)
    val activeTranslation = hadith.getTranslation(transLang)
    val activeSyarah = hadith.getSyarah(transLang)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("hadith_detail_screen")
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Hadits ke-${hadith.number}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                }
            },
            actions = {
                if (currentUser.role == UserRole.DEVELOPER) {
                    IconButton(
                        onClick = { showDevEditor = true },
                        modifier = Modifier.testTag("dev_editor_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Teks Hadis (Developer)",
                            tint = Color(0xFF6366F1)
                        )
                    }
                }
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Hadits ${hadith.number}", "${hadith.arabicText}\n\n$activeTranslation")
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Teks hadis berhasil disalin!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Salin")
                }
                IconButton(
                    onClick = { viewModel.toggleFavorite(hadith.id, hadith.isFavorite) }
                ) {
                    Icon(
                        imageVector = if (hadith.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favorit",
                        tint = if (hadith.isFavorite) Color(0xFFE5A100) else MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Translation Language Switcher (Indonesia, Sunda, English)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bahasa Terjemahan:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        TranslationLanguage.values().forEach { lang ->
                            val isSelected = transLang == lang
                            Surface(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable {
                                    viewModel.translationLanguage.value = lang
                                    Toast.makeText(context, "Terjemahan: ${lang.label}", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(
                                    text = when (lang) {
                                        TranslationLanguage.INDONESIA -> "🇮🇩 ID"
                                        TranslationLanguage.SUNDA -> "🌾 Sunda"
                                        TranslationLanguage.ENGLISH -> "🇬🇧 EN"
                                    },
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Developer Mode Quick Access Editor Banner
            if (currentUser.role == UserRole.DEVELOPER) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6366F1).copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color(0xFF6366F1),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Mode Developer: Editor Teks Hadis",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4338CA)
                                )
                                Text(
                                    text = "Ubah naskah Arab, terjemahan Sunda/ID/EN & syarah",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4338CA).copy(alpha = 0.8f)
                                )
                            }
                        }
                        Button(
                            onClick = { showDevEditor = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp).testTag("open_developer_editor_button")
                        ) {
                            Text("Edit Teks", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Rawi
            Text(
                text = activeTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = hadith.rawi,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Font size slider & Audio control
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Ukuran Huruf",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ukuran (${fontSize.toInt()}sp)",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = fontSize,
                            onValueChange = { fontSize = it },
                            valueRange = 18f..36f,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Murottal Audio Player bar with Muroja'ah repeat & speed
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            isPlayingAudio = !isPlayingAudio
                                            Toast.makeText(
                                                context,
                                                if (isPlayingAudio) "▶️ Murottal Hadits #${hadith.number} diputar (Kecepatan $playbackSpeed, Ulangi ${repeatCount}x)" else "⏹️ Audio dijeda",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(
                                            imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = "Putar Audio",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (isPlayingAudio) "Memutar Murottal Hadis ${hadith.number}" else "Murottal Audio Hadits",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPlayingAudio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Metode Muroja'ah Penghafal Qur'an & Hadis",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                // Quick Quiz Button
                                OutlinedButton(
                                    onClick = { showQuizDialog = true },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp).testTag("hadith_quiz_button")
                                ) {
                                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Uji Hafalan", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Repeat and speed options
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Repeat, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ulangi:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    listOf(1, 3, 5, 10).forEach { r ->
                                        Surface(
                                            color = if (repeatCount == r) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .clickable {
                                                    repeatCount = r
                                                    Toast.makeText(context, "Muroja'ah: diulang $r kali", Toast.LENGTH_SHORT).show()
                                                }
                                        ) {
                                            Text(
                                                text = "${r}x",
                                                fontSize = 10.sp,
                                                color = if (repeatCount == r) Color.White else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    listOf("1.0x", "1.25x", "1.5x").forEach { sp ->
                                        Surface(
                                            color = if (playbackSpeed == sp) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .clickable { playbackSpeed = sp }
                                        ) {
                                            Text(
                                                text = sp,
                                                fontSize = 9.sp,
                                                color = if (playbackSpeed == sp) Color.White else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Arabic Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = hadith.arabicText,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = fontSize.sp,
                            lineHeight = (fontSize * 1.8f).sp
                        ),
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Translation (Indonesia / Sunda / English)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = when (transLang) {
                            TranslationLanguage.INDONESIA -> "Terjemahan Bahasa Indonesia"
                            TranslationLanguage.SUNDA -> "Tarjamahan Basa Sunda"
                            TranslationLanguage.ENGLISH -> "English Translation"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeTranslation,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Word-by-word Translation Table (Per-Kata)
            WordByWordTable(words = hadith.words, transLang = transLang)

            Spacer(modifier = Modifier.height(16.dp))

            // Syarah & Makna Mendalam
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = when (transLang) {
                            TranslationLanguage.INDONESIA -> "Syarah & Makna Mendalam (Kontekstual)"
                            TranslationLanguage.SUNDA -> "Syarah & Hartos Jero (Kontekstual)"
                            TranslationLanguage.ENGLISH -> "In-Depth Contextual Explanation (Syarah)"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeSyarah,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Memorization Tracker Selector
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Status Hafalan Santri",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple(0, "Belum", Color.Gray),
                            Triple(1, "Menghafal", Color(0xFFF59E0B)),
                            Triple(2, "Sudah Hafal", Color(0xFF10B981))
                        ).forEach { (lvl, label, color) ->
                            val isSelected = hadith.memorizationLevel == lvl
                            Surface(
                                color = if (isSelected) color.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.setMemorization(hadith.id, lvl)
                                        Toast.makeText(context, "Status hafalan: $label", Toast.LENGTH_SHORT).show()
                                    }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) color else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action: Go to Chat Room for this Hadith
            Button(
                onClick = { onOpenChat(hadith.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("open_hadith_chat_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Chat, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buka Blok Chat Hadis ke-${hadith.number} (E2EE)",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        if (showDevEditor) {
            DeveloperHadithEditorDialog(
                initialHadithId = hadith.id,
                viewModel = viewModel,
                onDismiss = { showDevEditor = false }
            )
        }

        if (showQuizDialog) {
            HadithTasmiQuizDialog(
                hadith = hadith,
                onPassed = {
                    viewModel.setMemorization(hadith.id, 2)
                    Toast.makeText(context, "🎉 Masya Allah! Anda dinyatakan LULUS Tasmi' Hadits #${hadith.number}", Toast.LENGTH_LONG).show()
                },
                onDismiss = { showQuizDialog = false }
            )
        }
    }
}

@Composable
fun HadithTasmiQuizDialog(
    hadith: Hadith,
    onPassed: () -> Unit,
    onDismiss: () -> Unit
) {
    var quizStep by remember { mutableStateOf(0) } // 0: Sambung Kata, 1: Makna Kata
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    // Prepare quiz question based on hadith words
    val targetWord = hadith.words.firstOrNull() ?: WordTranslation("إِنَّمَا", "Innamā", "Sesungguhnya")
    val correctWord = targetWord.arabic
    val dummyWords = listOf("سُبْحَانَ اللهِ", "الْحَمْدُ لِلَّهِ", "لاَ تَغْضَبْ", "كُتِبَ عَلَيْكُمْ", "بُنِيَ الإِسْلاَمُ")
        .filter { it != correctWord }
        .take(3)
    val options = remember(hadith.id) { (listOf(correctWord) + dummyWords).shuffled() }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Uji Hafalan & Tasmi' Hadits #${hadith.number}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Soal: Lengkapi kata pembuka dari Hadits #${hadith.number} (${hadith.titleId}):",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "[ ..... ] " + hadith.arabicText.split(" ").drop(1).take(5).joinToString(" ") + " ...",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Text(
                    text = "Pilih kata yang tepat:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                options.forEach { opt ->
                    val isChosen = selectedOption == opt
                    Surface(
                        color = when {
                            isSubmitted && opt == correctWord -> Color(0xFFD1FAE5)
                            isSubmitted && isChosen && opt != correctWord -> Color(0xFFFEE2E2)
                            isChosen -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isSubmitted) { selectedOption = opt }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = FontFamily.Serif,
                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSubmitted && opt == correctWord) {
                                Text("✓ Benar", color = Color(0xFF059669), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                            } else if (isSubmitted && isChosen && opt != correctWord) {
                                Text("✗ Salah", color = Color.Red, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                if (isSubmitted) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isCorrect) "🌟 Mumtaz! Nilai 100. Hafalan Anda sangat mantap!" else "Semangat! Terus ulangi tilawah dan mutqin-kan lagi hafalan.",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect) Color(0xFF059669) else Color(0xFFDC2626)
                    )
                }
            }
        },
        confirmButton = {
            if (!isSubmitted) {
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            isSubmitted = true
                            isCorrect = selectedOption == correctWord
                            if (isCorrect) {
                                onPassed()
                            }
                        }
                    },
                    enabled = selectedOption != null
                ) {
                    Text("Periksa Jawaban")
                }
            } else {
                Button(onClick = onDismiss) {
                    Text("Selesai")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
