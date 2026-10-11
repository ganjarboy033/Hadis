package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.data.model.AppLanguage
import com.example.data.model.TranslationLanguage
import com.example.data.model.UserRole
import com.example.ui.components.DeveloperHadithEditorDialog
import com.example.ui.components.E2EESecurityShield
import com.example.ui.components.OnlineStatusBadge
import com.example.ui.components.UserRoleBadge
import com.example.ui.localization.getUiStrings
import com.example.ui.viewmodel.MainViewModel

data class SlideTabItem(
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentSlideIndex by viewModel.currentSlideIndex.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val urgentAnnouncement by viewModel.urgentBannerAnnouncement.collectAsState()
    val activeChatRoom by viewModel.activeChatRoomId.collectAsState()
    val activeHadithDetail by viewModel.selectedHadithDetail.collectAsState()
    val appLang by viewModel.appLanguage.collectAsState()
    val transLang by viewModel.translationLanguage.collectAsState()
    val strings = getUiStrings(appLang)
    val context = LocalContext.current

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showDevEditorGlobal by remember { mutableStateOf(false) }

    // Hide top app bar & navigation bar when full-screen conversation or hadith detail is shown
    val isChildScreenOpen = activeChatRoom != null || activeHadithDetail != null

    val tabs = listOf(
        SlideTabItem(strings.tabHadith, Icons.Default.MenuBook, "nav_tab_hadith"),
        SlideTabItem(strings.tabChat, Icons.Default.Chat, "nav_tab_chat"),
        SlideTabItem(strings.tabCommunity, Icons.Default.Group, "nav_tab_community"),
        SlideTabItem(strings.tabDashboard, Icons.Default.Analytics, "nav_tab_dashboard"),
        SlideTabItem(strings.tabProfile, Icons.Default.Person, "nav_tab_profile")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (!isChildScreenOpen) {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = strings.appTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                UserRoleBadge(role = currentUser.role)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OnlineStatusBadge(
                                    isOnline = currentUser.isOnline,
                                    lastSeenText = "Terakhir aktif baru saja"
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ID: ${currentUser.id}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    actions = {
                        if (currentUser.role == UserRole.DEVELOPER) {
                            IconButton(
                                onClick = { showDevEditorGlobal = true },
                                modifier = Modifier.testTag("top_bar_dev_editor_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = strings.textEditorDevTitle,
                                    tint = Color(0xFF6366F1)
                                )
                            }
                        }
                        IconButton(
                            onClick = { showLanguageDialog = true },
                            modifier = Modifier.testTag("top_bar_language_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = strings.appLanguageLabel,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        E2EESecurityShield(modifier = Modifier.padding(end = 6.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if (!isChildScreenOpen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val selected = currentSlideIndex == index
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.setSlideIndex(index) },
                            icon = {
                                if (index == 1) {
                                    // Chat tab badge
                                    BadgedBox(
                                        badge = {
                                            Badge { Text("42") }
                                        }
                                    ) {
                                        Icon(tab.icon, contentDescription = tab.title)
                                    }
                                } else {
                                    Icon(tab.icon, contentDescription = tab.title)
                                }
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Instant Teacher Announcement Urgent Banner
            AnimatedVisibility(
                visible = urgentAnnouncement != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                if (urgentAnnouncement != null) {
                    Card(
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDC2626)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "PENGUMUMAN INSTAN DARI GURU (${urgentAnnouncement!!.guruName})",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${urgentAnnouncement!!.title}: ${urgentAnnouncement!!.content}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    maxLines = 2
                                )
                            }
                            IconButton(onClick = { viewModel.dismissUrgentBanner() }) {
                                Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // Main Slide Views
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentSlideIndex) {
                    0 -> HadithListScreen(
                        viewModel = viewModel,
                        onNavigateToDonation = { viewModel.setSlideIndex(4) }
                    )
                    1 -> ChatListScreen(viewModel = viewModel)
                    2 -> CommunityNearbyScreen(viewModel = viewModel)
                    3 -> AnalyticsDashboardScreen(viewModel = viewModel)
                    4 -> ProfileSettingsScreen(viewModel = viewModel)
                }
            }

            if (showDevEditorGlobal) {
                DeveloperHadithEditorDialog(
                    initialHadithId = 1,
                    viewModel = viewModel,
                    onDismiss = { showDevEditorGlobal = false }
                )
            }

            if (showLanguageDialog) {
                AlertDialog(
                    onDismissRequest = { showLanguageDialog = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.appLanguageLabel + " & " + strings.translationLanguageLabel,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            // Section 1: Bahasa Aplikasi
                            Column {
                                Text(
                                    text = "1. " + strings.appLanguageLabel + ":",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    AppLanguage.values().forEach { lang ->
                                        val isSel = appLang == lang
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.appLanguage.value = lang
                                                Toast.makeText(context, "${strings.appLanguageLabel}: ${lang.label}", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = if (isSel) ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else ButtonDefaults.outlinedButtonColors(),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = when (lang) {
                                                    AppLanguage.INDONESIA -> "🇮🇩 ID"
                                                    AppLanguage.SUNDA -> "🌾 Sunda"
                                                    AppLanguage.ENGLISH -> "🇬🇧 EN"
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }

                            // Section 2: Bahasa Terjemahan Hadis
                            Column {
                                Text(
                                    text = "2. " + strings.translationLanguageLabel + ":",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    TranslationLanguage.values().forEach { lang ->
                                        val isSel = transLang == lang
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.translationLanguage.value = lang
                                                Toast.makeText(context, "${strings.translationLanguageLabel}: ${lang.label}", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = if (isSel) ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) else ButtonDefaults.outlinedButtonColors(),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = when (lang) {
                                                    TranslationLanguage.INDONESIA -> "🇮🇩 ID"
                                                    TranslationLanguage.SUNDA -> "🌾 Sunda"
                                                    TranslationLanguage.ENGLISH -> "🇬🇧 EN"
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showLanguageDialog = false }) {
                            Text("Selesai")
                        }
                    }
                )
            }
        }
    }
}
