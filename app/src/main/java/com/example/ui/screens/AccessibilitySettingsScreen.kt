package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccessibilityMode

@Composable
fun AccessibilitySettingsScreen(
    currentMode: AccessibilityMode,
    onModeSelected: (AccessibilityMode) -> Unit,
    onBack: () -> Unit,
    isDarkTheme: Boolean = false,
    onToggleDarkTheme: () -> Unit = {}
) {
    var voiceModeEnabled by remember { mutableStateOf(false) }
    var captionsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Accessibility",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Quick theme toggle icon button
                IconButton(
                    onClick = onToggleDarkTheme,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = if (isDarkTheme) Color(0xFFFBBF24) else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Row 1: Standard Mode (Blue) & Focus Reading (Purple)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PastelAccessTile(
                    modifier = Modifier.weight(1f),
                    title = "Standard Mode",
                    subtitle = "Default reading view",
                    icon = Icons.Default.MenuBook,
                    bgColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFE8F1FF),
                    iconTint = Color(0xFF2563EB),
                    isSelected = currentMode == AccessibilityMode.STANDARD,
                    onClick = { onModeSelected(AccessibilityMode.STANDARD) }
                )

                PastelAccessTile(
                    modifier = Modifier.weight(1f),
                    title = "Focus Reading",
                    subtitle = "Distraction-free view",
                    icon = Icons.Default.CenterFocusStrong,
                    bgColor = if (isDarkTheme) Color(0xFF281C38) else Color(0xFFF3E8FF),
                    iconTint = Color(0xFF9333EA),
                    isSelected = false,
                    onClick = {}
                )
            }

            // Row 2: Dyslexia-Friendly (Orange) & Voice Mode (Cyan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PastelAccessTile(
                    modifier = Modifier.weight(1f),
                    title = "Dyslexia-Friendly",
                    subtitle = "Simplified & spaced text",
                    badgeText = "Aa",
                    bgColor = if (isDarkTheme) Color(0xFF332014) else Color(0xFFFFEDD5),
                    iconTint = Color(0xFFEA580C),
                    isSelected = currentMode == AccessibilityMode.DYSLEXIA_FRIENDLY,
                    onClick = { onModeSelected(AccessibilityMode.DYSLEXIA_FRIENDLY) }
                )

                PastelAccessTile(
                    modifier = Modifier.weight(1f),
                    title = "Voice Mode",
                    subtitle = "Text-to-speech",
                    icon = Icons.Default.Headphones,
                    bgColor = if (isDarkTheme) Color(0xFF142735) else Color(0xFFE0F2FE),
                    iconTint = Color(0xFF0284C7),
                    isSelected = voiceModeEnabled,
                    onClick = { voiceModeEnabled = !voiceModeEnabled }
                )
            }

            // Row 3: Captions (Pink) & Dark & Light Theme (Navy / Theme Toggle)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PastelAccessTile(
                    modifier = Modifier.weight(1f),
                    title = "Captions",
                    subtitle = "For videos & audio",
                    badgeText = "CC",
                    bgColor = if (isDarkTheme) Color(0xFF331825) else Color(0xFFFCE7F3),
                    iconTint = Color(0xFFDB2777),
                    isSelected = captionsEnabled,
                    onClick = { captionsEnabled = !captionsEnabled }
                )

                PastelAccessTile(
                    modifier = Modifier.weight(1f),
                    title = if (isDarkTheme) "Dark Theme ON" else "Light Theme",
                    subtitle = "Tap to switch theme",
                    icon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    bgColor = if (isDarkTheme) Color(0xFF1F242F) else Color(0xFFEDE9FE),
                    iconTint = if (isDarkTheme) Color(0xFFFBBF24) else Color(0xFF4F46E5),
                    isSelected = isDarkTheme,
                    onClick = onToggleDarkTheme
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dignified note
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessibilityNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Preferences persist locally in offline database for instant responsiveness across all light and dark environments.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PastelAccessTile(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector? = null,
    badgeText: String? = null,
    bgColor: Color,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) BorderStroke(1.5.dp, iconTint) else null,
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                } else if (badgeText != null) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = iconTint
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
