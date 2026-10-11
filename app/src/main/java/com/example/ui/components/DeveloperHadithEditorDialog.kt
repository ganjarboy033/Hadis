package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Hadith
import com.example.data.model.TranslationLanguage
import com.example.data.repository.HadithRepository
import com.example.ui.localization.getUiStrings
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperHadithEditorDialog(
    initialHadithId: Int = 1,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val allHadiths = remember { HadithRepository.allHadithsList }
    var selectedHadithId by remember { mutableStateOf(initialHadithId) }

    // Load initial values for selected hadith
    val currentHadith = allHadiths.find { it.id == selectedHadithId } ?: allHadiths.first()

    var arabicInput by remember(selectedHadithId) { mutableStateOf(currentHadith.arabicText) }
    var titleIdInput by remember(selectedHadithId) { mutableStateOf(currentHadith.titleId) }
    var titleSuInput by remember(selectedHadithId) { mutableStateOf(currentHadith.titleSu.ifBlank { "Hadis ${currentHadith.number} (Basa Sunda)" }) }
    var titleEnInput by remember(selectedHadithId) { mutableStateOf(currentHadith.titleEn.ifBlank { "Hadith ${currentHadith.number} (English)" }) }

    var transIdInput by remember(selectedHadithId) { mutableStateOf(currentHadith.translationId) }
    var transSuInput by remember(selectedHadithId) {
        mutableStateOf(
            if (currentHadith.translationSu.isNotBlank()) currentHadith.translationSu
            else "Saenyana hadis ka-${currentHadith.number} netelakeun perkawis ${currentHadith.titleId}..."
        )
    }
    var transEnInput by remember(selectedHadithId) {
        mutableStateOf(
            if (currentHadith.translationEn.isNotBlank()) currentHadith.translationEn
            else "Indeed, Hadith #${currentHadith.number} explains concerning ${currentHadith.titleId}..."
        )
    }

    var syarahIdInput by remember(selectedHadithId) { mutableStateOf(currentHadith.syarahExplanation) }
    var syarahSuInput by remember(selectedHadithId) {
        mutableStateOf(
            if (currentHadith.syarahSu.isNotBlank()) currentHadith.syarahSu
            else "Katerangan jero perkawis hadis ieu pikeun pangajaran para santri..."
        )
    }
    var syarahEnInput by remember(selectedHadithId) {
        mutableStateOf(
            if (currentHadith.syarahEn.isNotBlank()) currentHadith.syarahEn
            else "Comprehensive commentary and lessons derived from this hadith..."
        )
    }

    var editorSubTab by remember { mutableStateOf(0) } // 0: Arab, 1: Terjemahan, 2: Syarah, 3: Pratinjau
    var previewLang by remember { mutableStateOf(TranslationLanguage.INDONESIA) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .testTag("developer_hadith_editor_dialog"),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF6366F1).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Color(0xFF6366F1),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Editor Teks Hadis (Mode Developer)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ubah naskah Arab & terjemahan 3 bahasa (ID, SU, EN)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Selector for 42 Hadith chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (1..42).forEach { num ->
                        val isSelected = num == selectedHadithId
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedHadithId = num },
                            label = { Text("#$num", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sub-tabs: 0: Aksara Arab, 1: Terjemahan (ID/SU/EN), 2: Syarah, 3: Pratinjau
                TabRow(
                    selectedTabIndex = editorSubTab,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(
                        selected = editorSubTab == 0,
                        onClick = { editorSubTab = 0 },
                        text = { Text("Teks Arab") }
                    )
                    Tab(
                        selected = editorSubTab == 1,
                        onClick = { editorSubTab = 1 },
                        text = { Text("Terjemahan (3 Bahasa)") }
                    )
                    Tab(
                        selected = editorSubTab == 2,
                        onClick = { editorSubTab = 2 },
                        text = { Text("Syarah & Makna") }
                    )
                    Tab(
                        selected = editorSubTab == 3,
                        onClick = { editorSubTab = 3 },
                        text = { Text("Pratinjau Live") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tab content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (editorSubTab) {
                        0 -> {
                            // Arabic editor
                            Text(
                                text = "Naskah Aksara Arab & Harakat Lengkap (Hadits #$selectedHadithId):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = arabicInput,
                                onValueChange = { arabicInput = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .testTag("dev_editor_arabic_input"),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 20.sp,
                                    lineHeight = 36.sp,
                                    textAlign = TextAlign.End
                                ),
                                placeholder = { Text("Ketik atau tempel teks Arab berharakat...") }
                            )
                        }

                        1 -> {
                            // 3 Language Translation Editor
                            Text(
                                text = "Judul & Terjemahan Multibahasa (Hadits #$selectedHadithId):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Indonesian Title & Translation
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "🇮🇩 Bahasa Indonesia", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = titleIdInput,
                                        onValueChange = { titleIdInput = it },
                                        label = { Text("Judul Indonesia") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = transIdInput,
                                        onValueChange = { transIdInput = it },
                                        label = { Text("Terjemahan Indonesia") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Sundanese Title & Translation
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "🌾 Basa Sunda", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = titleSuInput,
                                        onValueChange = { titleSuInput = it },
                                        label = { Text("Judul Basa Sunda") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = transSuInput,
                                        onValueChange = { transSuInput = it },
                                        label = { Text("Tarjamahan Basa Sunda") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // English Title & Translation
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "🇬🇧 English", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = titleEnInput,
                                        onValueChange = { titleEnInput = it },
                                        label = { Text("English Title") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    OutlinedTextField(
                                        value = transEnInput,
                                        onValueChange = { transEnInput = it },
                                        label = { Text("English Translation") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        2 -> {
                            // Syarah Editor in 3 Languages
                            Text(
                                text = "Syarah & Makna Kontekstual (Hadits #$selectedHadithId):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = syarahIdInput,
                                onValueChange = { syarahIdInput = it },
                                label = { Text("Syarah Indonesia") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = syarahSuInput,
                                onValueChange = { syarahSuInput = it },
                                label = { Text("Syarah Basa Sunda") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = syarahEnInput,
                                onValueChange = { syarahEnInput = it },
                                label = { Text("English Commentary (Syarah)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        3 -> {
                            // Live Preview
                            Text(
                                text = "Pratinjau Tampilan Pengguna:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TranslationLanguage.values().forEach { lang ->
                                    FilterChip(
                                        selected = previewLang == lang,
                                        onClick = { previewLang = lang },
                                        label = { Text(lang.label, fontSize = 11.sp) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    val title = when (previewLang) {
                                        TranslationLanguage.SUNDA -> titleSuInput
                                        TranslationLanguage.ENGLISH -> titleEnInput
                                        TranslationLanguage.INDONESIA -> titleIdInput
                                    }
                                    val trans = when (previewLang) {
                                        TranslationLanguage.SUNDA -> transSuInput
                                        TranslationLanguage.ENGLISH -> transEnInput
                                        TranslationLanguage.INDONESIA -> transIdInput
                                    }
                                    val syarah = when (previewLang) {
                                        TranslationLanguage.SUNDA -> syarahSuInput
                                        TranslationLanguage.ENGLISH -> syarahEnInput
                                        TranslationLanguage.INDONESIA -> syarahIdInput
                                    }

                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = arabicInput,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontFamily = FontFamily.Serif,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = trans,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Syarah: $syarah",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom actions: Reset to default & Save changes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.resetHadithToDefault(selectedHadithId)
                            // reload initial text
                            arabicInput = currentHadith.arabicText
                            titleIdInput = currentHadith.titleId
                            transIdInput = currentHadith.translationId
                            syarahIdInput = currentHadith.syarahExplanation
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset Asal", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.saveHadithOverride(
                                hadithId = selectedHadithId,
                                arabicText = arabicInput,
                                titleId = titleIdInput,
                                titleSu = titleSuInput,
                                titleEn = titleEnInput,
                                translationId = transIdInput,
                                translationSu = transSuInput,
                                translationEn = transEnInput,
                                syarahExplanation = syarahIdInput,
                                syarahSu = syarahSuInput,
                                syarahEn = syarahEnInput
                            )
                            onDismiss()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("save_hadith_override_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simpan Perubahan")
                    }
                }
            }
        }
    }
}
