package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FocusDarkBg
import com.example.ui.theme.FocusHighlightGold
import com.example.ui.theme.FocusSecondaryMint

@Composable
fun FocusModeScreen(
    onExitFocusMode: () -> Unit
) {
    var textScale by remember { mutableStateOf(22) }
    var activeAction by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "audioWave")
    val waveAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FocusDarkBg)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onExitFocusMode,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .testTag("exit_focus_mode_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit Focus Mode",
                        tint = Color.White
                    )
                }

                Surface(
                    color = FocusSecondaryMint.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(FocusSecondaryMint)
                        )
                        Text(
                            text = "Focus Mode",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = FocusSecondaryMint
                        )
                    }
                }

                // Aa Text Size Control
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.clickable {
                        textScale = if (textScale >= 28) 20 else textScale + 4
                    }
                ) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aa",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Thin progress indicator
            LinearProgressIndicator(
                progress = { 0.25f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = FocusSecondaryMint,
                trackColor = Color.White.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "1 / 68",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Center High Legibility Reading Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Binary Search is an",
                fontSize = textScale.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                lineHeight = (textScale * 1.5).sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = FocusHighlightGold.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "efficient",
                        fontSize = (textScale + 2).sp,
                        fontWeight = FontWeight.Bold,
                        color = FocusHighlightGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "algorithm",
                    fontSize = textScale.sp,
                    color = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = FocusHighlightGold.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "used",
                        fontSize = (textScale + 2).sp,
                        fontWeight = FontWeight.Bold,
                        color = FocusHighlightGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "to find an",
                    fontSize = textScale.sp,
                    color = Color.White
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "element in a",
                    fontSize = textScale.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = FocusHighlightGold.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "sorted",
                        fontSize = (textScale + 2).sp,
                        fontWeight = FontWeight.Bold,
                        color = FocusHighlightGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "array.",
                fontSize = textScale.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "It repeatedly divides the search interval in half.",
                fontSize = (textScale - 2).sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = (textScale * 1.4).sp
            )

            if (activeAction != null) {
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (activeAction) {
                            "simplify" -> "💡 In short: Look at center, throw away wrong half, repeat."
                            "explain" -> "🧒 Analogous to guessing a number between 1 and 100 with 'higher/lower' hints."
                            "example" -> "💻 Finding 23 in [2, 5, 8, 12, 16, 23, 38]: Check 12 -> 23 is larger -> search [16, 23, 38] -> Check 23 -> Found!"
                            else -> "🔊 Replaying audio explanation..."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = FocusSecondaryMint,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Bottom Audio Visualizer & Actions
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Audio wave bar
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(30.dp)
            ) {
                val count = 28
                val step = size.width / count
                for (i in 0 until count) {
                    val factor = ((i % 5) + 1) / 5f * waveAnim
                    val barH = size.height * factor
                    val x = i * step
                    drawLine(
                        color = FocusHighlightGold.copy(alpha = 0.75f),
                        start = Offset(x, (size.height - barH) / 2),
                        end = Offset(x, (size.height + barH) / 2),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }

            // Quick actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FocusActionItem(
                    icon = Icons.Default.Headphones,
                    label = "Repeat",
                    onClick = { activeAction = "repeat" }
                )
                FocusActionItem(
                    icon = Icons.Default.AutoAwesome,
                    label = "Simplify",
                    onClick = { activeAction = "simplify" }
                )
                FocusActionItem(
                    icon = Icons.Default.Lightbulb,
                    label = "Explain",
                    onClick = { activeAction = "explain" }
                )
                FocusActionItem(
                    icon = Icons.Default.Terminal,
                    label = "Example",
                    onClick = { activeAction = "example" }
                )

                IconButton(
                    onClick = onExitFocusMode,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = FocusDarkBg
                    )
                }
            }
        }
    }
}

@Composable
fun FocusActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}
