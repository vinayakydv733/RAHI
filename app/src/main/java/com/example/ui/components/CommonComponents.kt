package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NavigationTab
import com.example.data.model.NetworkMode
import com.example.ui.theme.*

@Composable
fun AarohanTopHeader(
    title: String,
    networkMode: NetworkMode,
    onNetworkModeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    showBackButton: Boolean = false,
    isDarkTheme: Boolean = false,
    onToggleTheme: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (showBackButton && onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Aarohan",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (trailingContent != null) {
                        trailingContent()
                    } else {
                        // Dark / Light Theme Toggle Button
                        if (onToggleTheme != null) {
                            IconButton(
                                onClick = onToggleTheme,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            ) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = if (isDarkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
                                    tint = if (isDarkTheme) Color(0xFFFBBF24) else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Tri-state indicator pill
                        NetworkPill(
                            mode = networkMode,
                            onClick = onNetworkModeClick
                        )

                        // Avatar
                        IconButton(
                            onClick = onProfileClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkPill(
    mode: NetworkMode,
    onClick: () -> Unit
) {
    val (bgColor, textColor, dotColor, label) = when (mode) {
        NetworkMode.ONLINE -> Quadruple(
            OnlineGreenBg,
            OnlineGreen,
            OnlineGreen,
            "ONLINE"
        )
        NetworkMode.LOW_DATA -> Quadruple(
            LowDataAmberBg,
            LowDataAmber,
            LowDataAmber,
            "LOW DATA"
        )
        NetworkMode.OFFLINE -> Quadruple(
            OfflineSlateBg,
            OfflineSlate,
            OfflineSlate,
            "OFFLINE"
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = if (mode == NetworkMode.ONLINE) alpha else 1.0f))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                ),
                color = textColor
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun AarohanBottomNavigationBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.95f),
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationTab.values().forEach { tab ->
                val selected = currentTab == tab
                val color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                val icon = when (tab) {
                    NavigationTab.HOME -> Icons.Default.Bolt
                    NavigationTab.LEARN -> Icons.Default.MenuBook
                    NavigationTab.PRACTICE -> Icons.Default.FitnessCenter
                    NavigationTab.EXPLORE -> Icons.Default.Stars
                    NavigationTab.PROFILE -> Icons.Default.Person
                }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab.title,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressGauge(
    progressPercent: Int,
    modifier: Modifier = Modifier,
    sizeDp: Int = 44,
    strokeWidthDp: Float = 3.5f,
    activeColor: Color = MaterialTheme.colorScheme.secondary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest
) {
    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = strokeWidthDp.dp.toPx()
            val diameter = size.minDimension - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)
            val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)

            // Background circle
            drawArc(
                color = inactiveColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = activeColor,
                startAngle = -90f,
                sweepAngle = 360f * (progressPercent / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$progressPercent%",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = if (sizeDp > 40) 11.sp else 9.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MountainPathSearchDiagram(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            ) {
                val w = size.width
                val h = size.height

                // Mountain Peaks in background
                val peak1 = Path().apply {
                    moveTo(w * 0.05f, h * 0.85f)
                    lineTo(w * 0.22f, h * 0.35f)
                    lineTo(w * 0.40f, h * 0.85f)
                    close()
                }
                drawPath(peak1, Color(0xFFD3E4FE).copy(alpha = 0.6f))

                val peakCenter = Path().apply {
                    moveTo(w * 0.25f, h * 0.85f)
                    lineTo(w * 0.50f, h * 0.15f)
                    lineTo(w * 0.75f, h * 0.85f)
                    close()
                }
                drawPath(peakCenter, Color(0xFFBEC6E0).copy(alpha = 0.5f))

                val peakRight = Path().apply {
                    moveTo(w * 0.60f, h * 0.85f)
                    lineTo(w * 0.82f, h * 0.40f)
                    lineTo(w * 0.95f, h * 0.85f)
                    close()
                }
                drawPath(peakRight, Color(0xFFD3E4FE).copy(alpha = 0.6f))

                // Base search horizon line
                drawLine(
                    color = Color(0xFF76777D),
                    start = Offset(w * 0.08f, h * 0.85f),
                    end = Offset(w * 0.92f, h * 0.85f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Discarded left half (soft lavender/blue block)
                val discardedWidth = w * 0.38f
                drawRoundRect(
                    color = Color(0xFFCBDBF5).copy(alpha = 0.55f),
                    topLeft = Offset(w * 0.10f, h * 0.78f),
                    size = androidx.compose.ui.geometry.Size(discardedWidth, 14.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                )

                // Target region right half (emerald mint)
                val targetStart = w * 0.52f
                val targetWidth = w * 0.38f
                drawRoundRect(
                    color = Color(0xFF6CF8BB).copy(alpha = 0.45f),
                    topLeft = Offset(targetStart, h * 0.78f),
                    size = androidx.compose.ui.geometry.Size(targetWidth, 14.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                )

                // Dashed vertical Midpoint divider
                val midX = w * 0.50f
                drawLine(
                    color = Color(0xFF006C49),
                    start = Offset(midX, h * 0.28f),
                    end = Offset(midX, h * 0.85f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Midpoint circle anchor
                drawCircle(
                    color = Color(0xFF006C49),
                    radius = 5.dp.toPx(),
                    center = Offset(midX, h * 0.85f)
                )
            }

            // Labels under canvas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "DISCARDED HALF",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "MIDPOINT",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "TARGET REGION",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Each check cuts the remaining climb exactly in half.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CodeSnippetBlock(
    title: String,
    code: String,
    language: String = "C++ / Java / Python"
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = language,
                    style = MaterialTheme.typography.labelSmall,
                    color = AarohanSecondaryFixed
                )

                Surface(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Code", code)
                        clipboard.setPrimaryClip(clip)
                        copied = true
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (copied) "Copied" else "Copy",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .padding(10.dp)
        ) {
            Text(
                text = code,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surfaceBright
            )
        }
    }
}
