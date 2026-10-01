package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.model.LearnerProfile
import com.example.data.model.NavigationTab
import com.example.data.model.ScreenDestination
import com.example.ui.theme.*

/**
 * 4 Core Modes in the Practice Arena Mode Selector
 */
enum class PracticeMode(val label: String, val icon: ImageVector) {
    DAILY_CHALLENGE("Daily Challenge", Icons.Default.Bolt),
    PRACTICE("Practice", Icons.Default.FitnessCenter),
    REVISION("Revision", Icons.Default.Autorenew),
    OFFLINE_QUIZ("Offline Quiz", Icons.Default.CloudOff)
}

data class PracticeTrack(
    val id: String,
    val title: String,
    val category: String,
    val questionCount: Int,
    val durationMinutes: Int,
    val difficulty: String,
    val isOfflineReady: Boolean,
    val solvedPercent: Int = 0,
    val xpReward: Int = 20
)

/**
 * Practice Arena Home Screen
 * Adheres strictly to the RAHI design system:
 * - Deep Navy (RahiDeepNavy / RahiNavySurface)
 * - Off-White (RahiOffWhite / RahiOffWhiteCard)
 * - Muted Green (RahiMutedGreen / RahiMutedGreenContainer)
 * - Soft Blue (RahiSoftBlue / RahiSoftBlueContainer)
 * - Warm Orange (RahiWarmOrange / RahiWarmOrangeContainer)
 */
@Composable
fun PracticeArenaScreen(
    profile: LearnerProfile,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    onToggleDarkTheme: (() -> Unit)? = null,
    initialMode: PracticeMode = PracticeMode.DAILY_CHALLENGE,
    onStartQuiz: (String) -> Unit,
    onSelectTab: (NavigationTab) -> Unit = {},
    onNavigateToScreen: (ScreenDestination) -> Unit = {}
) {
    var selectedMode by remember(initialMode) { mutableStateOf(initialMode) }
    val isDark = isDarkTheme

    val practiceTracks = remember {
        listOf(
            PracticeTrack(
                id = "bs_bounds",
                title = "Binary Search Invariants & Bounds",
                category = "DSA",
                questionCount = 10,
                durationMinutes = 15,
                difficulty = "Beginner",
                isOfflineReady = true,
                solvedPercent = 70,
                xpReward = 30
            ),
            PracticeTrack(
                id = "arrays_pointers",
                title = "Array Memory Pointers & Dynamic Lists",
                category = "DSA",
                questionCount = 12,
                durationMinutes = 20,
                difficulty = "Intermediate",
                isOfflineReady = true,
                solvedPercent = 25,
                xpReward = 35
            ),
            PracticeTrack(
                id = "linked_lists_fast_slow",
                title = "Linked List Fast-Slow Pointer Traversal",
                category = "DSA",
                questionCount = 8,
                durationMinutes = 12,
                difficulty = "Beginner",
                isOfflineReady = false,
                solvedPercent = 0,
                xpReward = 25
            ),
            PracticeTrack(
                id = "math_cs_bits",
                title = "Bit Manipulation & Modular Arithmetic",
                category = "Mathematics",
                questionCount = 10,
                durationMinutes = 15,
                difficulty = "Foundation",
                isOfflineReady = true,
                solvedPercent = 0,
                xpReward = 30
            ),
            PracticeTrack(
                id = "big_o_blitz",
                title = "Asymptotic Complexity & Big-O Blitz",
                category = "Aptitude",
                questionCount = 15,
                durationMinutes = 10,
                difficulty = "Essential",
                isOfflineReady = true,
                solvedPercent = 40,
                xpReward = 25
            )
        )
    }

    val offlineQuizPacks = remember {
        listOf(
            PracticeTrack(
                id = "offline_mock_1",
                title = "Full Offline STEM Mock Test #1",
                category = "Offline Pack",
                questionCount = 25,
                durationMinutes = 30,
                difficulty = "Comprehensive",
                isOfflineReady = true,
                solvedPercent = 0,
                xpReward = 50
            ),
            PracticeTrack(
                id = "offline_reasoning",
                title = "Class 11-12 & First Year Algorithmic Reasoning",
                category = "Offline Pack",
                questionCount = 20,
                durationMinutes = 25,
                difficulty = "Medium",
                isOfflineReady = true,
                solvedPercent = 60,
                xpReward = 40
            ),
            PracticeTrack(
                id = "offline_math_blitz",
                title = "Speed & Accuracy Math Blitz Paper",
                category = "Offline Pack",
                questionCount = 15,
                durationMinutes = 15,
                difficulty = "Speed Drill",
                isOfflineReady = true,
                solvedPercent = 100,
                xpReward = 30
            )
        )
    }

    // Canvas background adapting to light/dark
    val screenBg = if (isDark) FocusDarkBg else RahiOffWhite

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .testTag("practice_arena_screen"),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Header & Theme Mode Switcher
            Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Practice Arena",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    ),
                    color = if (isDark) Color.White else RahiDeepNavy
                )
                Text(
                    text = "Daily algorithmic challenges & revision",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                )
            }

            if (onToggleDarkTheme != null) {
                Surface(
                    color = if (isDark) RahiNavySurface else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, if (isDark) RahiNavyBorder else RahiOffWhiteBorder),
                    modifier = Modifier
                        .clickable { onToggleDarkTheme() }
                        .testTag("theme_toggle_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDark) "Switch to Light Theme" else "Switch to Dark Theme",
                            tint = if (isDark) Color(0xFFFBBF24) else RahiDeepNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isDark) "Light Mode" else "Dark Mode",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 1. 12-Day Streak Counter Card
        // -------------------------------------------------------------
        StreakCounterCard(
            streakDays = if (profile.streakDays > 0) profile.streakDays else 12,
            accuracyPercent = if (profile.quizAccuracyPercent > 0) profile.quizAccuracyPercent else 84,
            isDark = isDark
        )

        // -------------------------------------------------------------
        // 2. Featured Daily Challenge Card with 10-Question/10-Minute Indicator
        // -------------------------------------------------------------
        DailyChallengeCard(
            questionCount = 10,
            durationMinutes = 10,
            onStart = { onStartQuiz("daily_challenge") },
            isDark = isDark
        )

        // -------------------------------------------------------------
        // 3. Mode Selector (Daily Challenge, Practice, Revision, Offline Quiz)
        // -------------------------------------------------------------
        ModeSelectorBar(
            selectedMode = selectedMode,
            onSelectMode = { selectedMode = it },
            isDark = isDark
        )

        // -------------------------------------------------------------
        // 4. Selected Mode Content Section
        // -------------------------------------------------------------
        AnimatedContent(
            targetState = selectedMode,
            label = "PracticeArenaModeTransition"
        ) { mode ->
            when (mode) {
                PracticeMode.DAILY_CHALLENGE -> {
                    DailyChallengeSection(
                        onStartQuiz = onStartQuiz,
                        isDark = isDark
                    )
                }
                PracticeMode.PRACTICE -> {
                    PracticeTracksSection(
                        tracks = practiceTracks,
                        onStartQuiz = onStartQuiz,
                        isDark = isDark
                    )
                }
                PracticeMode.REVISION -> {
                    RevisionSection(
                        onStartQuiz = onStartQuiz,
                        isDark = isDark
                    )
                }
                PracticeMode.OFFLINE_QUIZ -> {
                    OfflineQuizSection(
                        packs = offlineQuizPacks,
                        onStartQuiz = onStartQuiz,
                        isDark = isDark
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 5. Explore RAHI Learning Hub (Fast website-style navigation)
        // -------------------------------------------------------------
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("explore_learning_hub_section")
        ) {
            Text(
                text = "Explore Learning Hub",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = if (isDark) Color.White else RahiDeepNavy
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = if (isDark) RahiNavySurface else RahiSoftBlueContainer,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isDark) RahiNavyBorder else RahiSoftBlue.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectTab(NavigationTab.LEARN) }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) Color(0xFF1E3A5F) else Color(0xFFDBEAFE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = RahiSoftBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Course Catalog",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "DSA, Math, Python lessons",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }

                Surface(
                    color = if (isDark) RahiNavySurface else RahiWarmOrangeContainer,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isDark) RahiNavyBorder else RahiWarmOrange.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectTab(NavigationTab.EXPLORE) }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) Color(0xFF45220A) else Color(0xFFFFEDD5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = RahiWarmOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Opportunities",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "Scholarships & Internships",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = if (isDark) RahiNavySurface else RahiMutedGreenContainer,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isDark) RahiNavyBorder else RahiMutedGreen.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToScreen(ScreenDestination.AskDoubt) }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) Color(0xFF063323) else Color(0xFFD1FAE5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = RahiMutedGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Ask Doubt",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "Instant STEM mentor chat",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }

                Surface(
                    color = if (isDark) RahiNavySurface else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isDark) RahiNavyBorder else Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToScreen(ScreenDestination.Downloads) }
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) Color(0xFF27354A) else Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Downloads",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "Offline study packs",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// =============================================================
// COMPONENT 1: 12-Day Streak Counter Card
// =============================================================
@Composable
private fun StreakCounterCard(
    streakDays: Int,
    accuracyPercent: Int,
    isDark: Boolean
) {
    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiNavyBorder else RahiOffWhiteBorder

    Surface(
        color = cardBg,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, cardBorder),
        shadowElevation = if (isDark) 0.dp else 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("streak_counter_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Fire Icon + Streak text + Accuracy Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fire Icon Badge with Warm Orange accent
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDark) Color(0xFF332014) else RahiWarmOrangeContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak Fire Icon",
                            tint = RahiWarmOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "$streakDays Day Streak",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = if (isDark) Color.White else RahiDeepNavy
                            )

                            // "On Fire" Pill in Warm Orange
                            Surface(
                                color = if (isDark) Color(0xFF45220A) else RahiWarmOrangeContainer,
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(0.5.dp, RahiWarmOrange.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "On Fire 🔥",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = RahiWarmOrange,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "2 days until 14-day Problem Solver badge (+50 XP)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }

                // Accuracy Badge in Muted Green
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = if (isDark) Color(0xFF063323) else RahiMutedGreenContainer,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.5.dp, RahiMutedGreen.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = RahiMutedGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "$accuracyPercent%",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isDark) Color(0xFF6EE7B7) else RahiOnMutedGreenContainer
                            )
                        }
                    }
                    Text(
                        text = "Accuracy",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // 7-Day Rhythm Circles (Mon - Sun)
            val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                daysOfWeek.forEachIndexed { index, day ->
                    val isPastCompleted = index < 6 // Mon through Sat completed
                    val isToday = index == 6        // Today is active streak day

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isPastCompleted -> RahiMutedGreen
                                        isToday -> RahiWarmOrange
                                        else -> if (isDark) Color(0xFF27354A) else Color(0xFFE2E8F0)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isPastCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completed $day",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else if (isToday) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Today Active",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)
                                )
                            }
                        }

                        Text(
                            text = day,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isToday) RahiWarmOrange else if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// COMPONENT 2: Daily Challenge Card (with 10-Question/10-Minute Indicator)
// =============================================================
@Composable
private fun DailyChallengeCard(
    questionCount: Int = 10,
    durationMinutes: Int = 10,
    onStart: () -> Unit,
    isDark: Boolean
) {
    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiWarmOrange.copy(alpha = 0.5f) else RahiWarmOrange.copy(alpha = 0.35f)

    Surface(
        color = cardBg,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, cardBorder),
        shadowElevation = if (isDark) 0.dp else 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_challenge_hero_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Row: Category Tag + Reset Countdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isDark) Color(0xFF45220A) else RahiWarmOrangeContainer,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(0.5.dp, RahiWarmOrange.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = RahiWarmOrange,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "DAILY CHALLENGE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            ),
                            color = if (isDark) Color(0xFFFFD8B2) else RahiOnWarmOrangeContainer
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Timer",
                        tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Resets in 04h 28m",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                }
            }

            // Challenge Title & Synopsis
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Binary Search Boundary Invariant",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = if (isDark) Color.White else RahiDeepNavy
                )
                Text(
                    text = "Given a sorted array with duplicate values, pinpoint the exact first occurrence index in O(log n) without a linear scan trap.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                )
            }

            // ---------------------------------------------------------
            // MANDATORY 10-QUESTION / 10-MINUTE PROMINENT INDICATOR
            // ---------------------------------------------------------
            Surface(
                color = if (isDark) Color(0xFF132034) else RahiSoftBlueContainer,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, RahiSoftBlue.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ten_question_ten_minute_indicator")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 10 Questions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "Questions",
                            tint = RahiSoftBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "$questionCount Questions",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isDark) Color(0xFF93C5FD) else RahiOnSoftBlueContainer
                            )
                            Text(
                                text = "Standard Format",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (isDark) Color(0xFF64748B) else Color(0xFF60A5FA)
                            )
                        }
                    }

                    // Divider dot
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(RahiSoftBlue.copy(alpha = 0.5f))
                    )

                    // 10 Minutes
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Duration",
                            tint = RahiSoftBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "$durationMinutes Minutes",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isDark) Color(0xFF93C5FD) else RahiOnSoftBlueContainer
                            )
                            Text(
                                text = "Timed Drill",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (isDark) Color(0xFF64748B) else Color(0xFF60A5FA)
                            )
                        }
                    }

                    // Divider dot
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(RahiSoftBlue.copy(alpha = 0.5f))
                    )

                    // Reward XP
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "XP Reward",
                            tint = RahiWarmOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "+25 XP",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = RahiWarmOrange
                        )
                    }
                }
            }

            // Stats Sub-bar: Attempted & Accuracy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = RahiSoftBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "1.4k students attempted",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155)
                        )
                    }
                }

                Surface(
                    color = if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = RahiMutedGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "78% avg accuracy",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155)
                        )
                    }
                }
            }

            // Primary Action Button (Deep Navy in Light Mode, Soft Blue in Dark Mode)
            Button(
                onClick = onStart,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) RahiSoftBlue else RahiDeepNavy,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_daily_challenge_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Start Daily Challenge",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

// =============================================================
// COMPONENT 3: Mode Selector Bar (Daily Challenge, Practice, Revision, Offline Quiz)
// =============================================================
@Composable
private fun ModeSelectorBar(
    selectedMode: PracticeMode,
    onSelectMode: (PracticeMode) -> Unit,
    isDark: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.testTag("mode_selector_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Practice Modes",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = if (isDark) Color.White else RahiDeepNavy
            )

            Text(
                text = "Select arena",
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(PracticeMode.values()) { mode ->
                val isSelected = mode == selectedMode

                val activeBg = if (isDark) RahiSoftBlue else RahiDeepNavy
                val activeContent = Color.White
                val inactiveBg = if (isDark) RahiNavySurface else Color(0xFFF1F5F9)
                val inactiveContent = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                val inactiveBorder = if (isDark) RahiNavyBorder else Color(0xFFE2E8F0)

                Surface(
                    color = if (isSelected) activeBg else inactiveBg,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) activeBg else inactiveBorder
                    ),
                    modifier = Modifier
                        .clickable { onSelectMode(mode) }
                        .testTag("mode_pill_${mode.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = mode.icon,
                            contentDescription = mode.label,
                            tint = if (isSelected) activeContent else when (mode) {
                                PracticeMode.DAILY_CHALLENGE -> RahiWarmOrange
                                PracticeMode.PRACTICE -> RahiSoftBlue
                                PracticeMode.REVISION -> RahiMutedGreen
                                PracticeMode.OFFLINE_QUIZ -> if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = if (isSelected) activeContent else inactiveContent
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// COMPONENT 4: Mode Section 1 - Daily Challenge
// =============================================================
@Composable
private fun DailyChallengeSection(
    onStartQuiz: (String) -> Unit,
    isDark: Boolean
) {
    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiNavyBorder else RahiOffWhiteBorder

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Recent Daily Challenges",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            ),
            color = if (isDark) Color.White else RahiDeepNavy
        )

        val pastChallenges = listOf(
            Triple("Yesterday", "Midpoint Integer Overflow Trap", "Solved • 100% Score"),
            Triple("Tuesday", "Two-Pointer Sorted Array Convergence", "Solved • 85% Score"),
            Triple("Monday", "Bitwise XOR Single Non-Duplicate Element", "Solved • 90% Score")
        )

        pastChallenges.forEach { (day, title, result) ->
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, cardBorder),
                shadowElevation = if (isDark) 0.dp else 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartQuiz(title) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isDark) Color(0xFF063323) else RahiMutedGreenContainer
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = RahiMutedGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                ),
                                color = if (isDark) Color.White else RahiDeepNavy
                            )
                            Text(
                                text = "$day • $result",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Review",
                        tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// =============================================================
// COMPONENT 5: Mode Section 2 - Practice Tracks
// =============================================================
@Composable
private fun PracticeTracksSection(
    tracks: List<PracticeTrack>,
    onStartQuiz: (String) -> Unit,
    isDark: Boolean
) {
    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiNavyBorder else RahiOffWhiteBorder

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Topic-wise Practice Banks",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = if (isDark) Color.White else RahiDeepNavy
            )
            Text(
                text = "${tracks.size} sets available",
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
            )
        }

        tracks.forEach { track ->
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, cardBorder),
                shadowElevation = if (isDark) 0.dp else 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartQuiz(track.id) }
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                color = if (isDark) Color(0xFF132034) else RahiSoftBlueContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = track.category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    ),
                                    color = if (isDark) Color(0xFF93C5FD) else RahiOnSoftBlueContainer,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }

                            if (track.isOfflineReady) {
                                Surface(
                                    color = if (isDark) Color(0xFF063323) else RahiMutedGreenContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudDone,
                                            contentDescription = null,
                                            tint = RahiMutedGreen,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Text(
                                            text = "Offline",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = if (isDark) Color(0xFF6EE7B7) else RahiOnMutedGreenContainer
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = "+${track.xpReward} XP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            color = RahiWarmOrange
                        )
                    }

                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = if (isDark) Color.White else RahiDeepNavy
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = null,
                                    tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${track.questionCount} Qs",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${track.durationMinutes}m",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }

                            Text(
                                text = "• ${track.difficulty}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }

                        Button(
                            onClick = { onStartQuiz(track.id) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) RahiSoftBlue else RahiDeepNavy,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (track.solvedPercent > 0) "Resume" else "Start",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    if (track.solvedPercent > 0) {
                        LinearProgressIndicator(
                            progress = { track.solvedPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = RahiMutedGreen,
                            trackColor = if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// COMPONENT 6: Mode Section 3 - Revision
// =============================================================
@Composable
private fun RevisionSection(
    onStartQuiz: (String) -> Unit,
    isDark: Boolean
) {
    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiNavyBorder else RahiOffWhiteBorder

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Adaptive Learning Insight Banner
        Surface(
            color = if (isDark) Color(0xFF132034) else RahiSoftBlueContainer,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, RahiSoftBlue.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDark) Color(0xFF1E3A5F) else Color(0xFFDBEAFE)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = RahiSoftBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Adaptive Learning Insight",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = if (isDark) Color.White else RahiDeepNavy
                    )
                    Text(
                        text = "You are strong in Arrays (92%), but Binary Search boundary logic needs 2 more practice runs to solidify.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                        color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155)
                    )
                }
            }
        }

        Text(
            text = "Targeted Revision Sets",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            ),
            color = if (isDark) Color.White else RahiDeepNavy
        )

        val revisionItems = listOf(
            Pair("Binary Search Off-by-One Boundaries", "3 incorrect answers saved from past tests"),
            Pair("Integer Overflow in Mid Formula Trap", "2 common exam pitfalls"),
            Pair("14 Spaced Repetition Recall Cards", "Scheduled for review today")
        )

        revisionItems.forEach { (title, subtitle) ->
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, cardBorder),
                shadowElevation = if (isDark) 0.dp else 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartQuiz(title) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }

                    OutlinedButton(
                        onClick = { onStartQuiz(title) },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (isDark) RahiSoftBlue else RahiDeepNavy),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Review",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) RahiSoftBlue else RahiDeepNavy
                            )
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// COMPONENT 7: Mode Section 4 - Offline Quiz
// =============================================================
@Composable
private fun OfflineQuizSection(
    packs: List<PracticeTrack>,
    onStartQuiz: (String) -> Unit,
    isDark: Boolean
) {
    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiNavyBorder else RahiOffWhiteBorder

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Offline Banner with Muted Green accent
        Surface(
            color = if (isDark) Color(0xFF063323) else RahiMutedGreenContainer,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, RahiMutedGreen.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = RahiMutedGreen,
                    modifier = Modifier.size(26.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "100% Offline Ready",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = if (isDark) Color(0xFF6EE7B7) else RahiOnMutedGreenContainer
                    )
                    Text(
                        text = "These papers run fully on your device without internet. Your scores will queue and sync when you reconnect.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                        color = if (isDark) Color(0xFFA7F3D0) else Color(0xFF064E3B)
                    )
                }
            }
        }

        Text(
            text = "Downloaded Test Papers",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            ),
            color = if (isDark) Color.White else RahiDeepNavy
        )

        packs.forEach { pack ->
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, cardBorder),
                shadowElevation = if (isDark) 0.dp else 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartQuiz(pack.id) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = pack.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = if (isDark) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "${pack.questionCount} Questions • ${pack.durationMinutes} mins • Saved Locally",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }

                    Button(
                        onClick = { onStartQuiz(pack.id) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) RahiSoftBlue else RahiDeepNavy,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Start Test",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
