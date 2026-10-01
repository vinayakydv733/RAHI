package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizQuestion
import com.example.ui.theme.*

@Composable
fun QuizScreen(
    questions: List<QuizQuestion>,
    quizTitle: String = "Binary Search Boundary Invariant",
    onQuizCompleted: (score: Int, total: Int, xp: Int) -> Unit = { _, _, _ -> },
    onBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // 10 Full Fallback Questions ensuring zero crashes and complete 10-Question challenge
    val defaultQuestions = remember {
        listOf(
            QuizQuestion(
                id = 1,
                questionNumber = 1,
                totalQuestions = 10,
                questionText = "What is the fundamental prerequisite before applying Binary Search on a collection?",
                options = listOf(
                    "Elements must all be positive integers",
                    "The collection must already be sorted in monotonic order",
                    "Collection size must be an exact power of two",
                    "Elements must all be prime numbers"
                ),
                correctIndex = 1,
                explanation = "Binary Search relies on sorted monotonicity to determine whether the target lies in the left or right partition."
            ),
            QuizQuestion(
                id = 2,
                questionNumber = 2,
                totalQuestions = 10,
                questionText = "In a sorted array of 1,024 elements, what is the maximum number of comparisons Binary Search takes in the worst case?",
                options = listOf(
                    "10 comparisons",
                    "512 comparisons",
                    "1,024 comparisons",
                    "20 comparisons"
                ),
                correctIndex = 0,
                explanation = "Because each step discards half: log₂(1024) = 10 comparisons maximum."
            ),
            QuizQuestion(
                id = 3,
                questionNumber = 3,
                totalQuestions = 10,
                questionText = "What is the worst-case asymptotic time complexity of Binary Search?",
                options = listOf(
                    "O(n)",
                    "O(log n)",
                    "O(n log n)",
                    "O(1)"
                ),
                correctIndex = 1,
                explanation = "Dividing search space by 2 each iteration gives T(n) = T(n/2) + O(1) => O(log n)."
            ),
            QuizQuestion(
                id = 4,
                questionNumber = 4,
                totalQuestions = 10,
                questionText = "Why is 'mid = low + ((high - low) / 2)' preferred over '(low + high) / 2'?",
                options = listOf(
                    "It runs faster in CPU hardware cache",
                    "It works automatically with floating point floats",
                    "It prevents signed 32-bit integer overflow when low + high > 2³¹ - 1",
                    "It allows binary search on unsorted arrays"
                ),
                correctIndex = 2,
                explanation = "When low + high exceeds INT_MAX, standard addition wraps to negative values causing index out-of-bounds."
            ),
            QuizQuestion(
                id = 5,
                questionNumber = 5,
                totalQuestions = 10,
                questionText = "What is the termination condition for the standard Binary Search loop?",
                options = listOf(
                    "while (low < high)",
                    "while (low <= high)",
                    "while (low != high)",
                    "while (high > 0)"
                ),
                correctIndex = 1,
                explanation = "When low == high, exactly one element remains to be inspected. Therefore, 'while (low <= high)' is necessary."
            ),
            QuizQuestion(
                id = 6,
                questionNumber = 6,
                totalQuestions = 10,
                questionText = "Can standard Binary Search achieve O(log n) performance on a Singly Linked List?",
                options = listOf(
                    "Yes, linked lists have pointers so it's even faster",
                    "No, because linked lists lack O(1) random index access",
                    "Yes, if the linked list is sorted",
                    "Only on doubly linked circular lists"
                ),
                correctIndex = 1,
                explanation = "Accessing the middle node in a linked list requires linear traversal O(n), negating the logarithmic benefit."
            ),
            QuizQuestion(
                id = 7,
                questionNumber = 7,
                totalQuestions = 10,
                questionText = "When searching for the first occurrence of a duplicate element, what should you do when arr[mid] == target?",
                options = listOf(
                    "Immediately return mid",
                    "Save mid as candidate answer and search left: high = mid - 1",
                    "Search right: low = mid + 1",
                    "Reset search from index 0"
                ),
                correctIndex = 1,
                explanation = "To locate the leftmost duplicate, record mid as potential answer and continue searching left with high = mid - 1."
            ),
            QuizQuestion(
                id = 8,
                questionNumber = 8,
                totalQuestions = 10,
                questionText = "What is the auxiliary space complexity of iterative Binary Search?",
                options = listOf(
                    "O(log n)",
                    "O(n)",
                    "O(1)",
                    "O(n²)"
                ),
                correctIndex = 2,
                explanation = "Iterative binary search uses only low, high, and mid scalar variables, requiring O(1) constant auxiliary memory."
            ),
            QuizQuestion(
                id = 9,
                questionNumber = 9,
                totalQuestions = 10,
                questionText = "If an array has 1,000,000 elements, approximately how many comparisons will Binary Search make at most?",
                options = listOf(
                    "Approximately 20 comparisons",
                    "100,000 comparisons",
                    "500,000 comparisons",
                    "1,000,000 comparisons"
                ),
                correctIndex = 0,
                explanation = "2²⁰ = 1,048,576. Thus, at most 20 comparisons are needed to find any target in 1 million items!"
            ),
            QuizQuestion(
                id = 10,
                questionNumber = 10,
                totalQuestions = 10,
                questionText = "Binary Search can also be used to find roots of continuous monotonic mathematical functions. This is known as:",
                options = listOf(
                    "Newton-Raphson only",
                    "Bisection Method / Binary Search on Answer Space",
                    "Gradient Descent",
                    "Monte Carlo Simulation"
                ),
                correctIndex = 1,
                explanation = "The Bisection Method applies Binary Search directly on numeric intervals to pinpoint mathematical roots with high precision."
            )
        )
    }

    val activeQuestions = if (questions.isNotEmpty()) questions else defaultQuestions
    val totalQs = activeQuestions.size

    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var scoreCount by remember { mutableStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = activeQuestions.getOrElse(currentIndex) { activeQuestions.first() }

    LaunchedEffect(isQuizCompleted) {
        if (isQuizCompleted) {
            onQuizCompleted(scoreCount, totalQs, 25)
        }
    }

    val cardBg = if (isDark) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDark) RahiNavyBorder else RahiOffWhiteBorder

    if (isQuizCompleted) {
        // Quiz Results & Celebration Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) FocusDarkBg else RahiOffWhite)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(RahiWarmOrangeContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = RahiWarmOrange,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Challenge Complete! 🎉",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = if (isDark) Color.White else RahiDeepNavy
            )

            Text(
                text = quizTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                color = cardBg,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Your Score",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )

                    Text(
                        text = "$scoreCount / $totalQs",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (scoreCount >= totalQs * 0.7) RahiMutedGreen else RahiWarmOrange
                    )

                    val accuracy = if (totalQs > 0) ((scoreCount.toFloat() / totalQs) * 100).toInt() else 0
                    LinearProgressIndicator(
                        progress = { accuracy / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = RahiMutedGreen,
                        trackColor = if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+25 XP", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = RahiWarmOrange)
                            Text(text = "Earned", style = MaterialTheme.typography.labelSmall, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🔥 12 Days", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = RahiWarmOrange)
                            Text(text = "Streak Kept", style = MaterialTheme.typography.labelSmall, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$accuracy%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = RahiMutedGreen)
                            Text(text = "Accuracy", style = MaterialTheme.typography.labelSmall, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onBack,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) RahiSoftBlue else RahiDeepNavy,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("quiz_finish_back_btn")
            ) {
                Text(text = "Return to Practice Arena", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    currentIndex = 0
                    selectedOption = null
                    isSubmitted = false
                    scoreCount = 0
                    isQuizCompleted = false
                },
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, if (isDark) RahiSoftBlue else RahiDeepNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Retake Challenge", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = if (isDark) RahiSoftBlue else RahiDeepNavy)
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) FocusDarkBg else RahiOffWhite)
    ) {
        // Top Bar
        Surface(
            color = cardBg,
            border = BorderStroke(1.dp, cardBorder),
            shadowElevation = if (isDark) 0.dp else 1.dp,
            modifier = Modifier.fillMaxWidth().statusBarsPadding()
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
                            tint = if (isDark) Color.White else RahiDeepNavy
                        )
                    }
                    Text(
                        text = "Question ${currentIndex + 1} of $totalQs",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) Color.White else RahiDeepNavy
                    )
                }

                // Timer Pill (10:00 Indicator)
                Surface(
                    color = if (isDark) Color(0xFF132034) else RahiSoftBlueContainer,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(0.5.dp, RahiSoftBlue.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = RahiSoftBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "09:42",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isDark) Color(0xFF93C5FD) else RahiOnSoftBlueContainer
                        )
                    }
                }
            }
        }

        // Animated Progress Bar across questions
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / totalQs },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = RahiMutedGreen,
            trackColor = if (isDark) Color(0xFF27354A) else Color(0xFFE2E8F0)
        )

        // Question Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MULTIPLE CHOICE",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp),
                            color = RahiSoftBlue
                        )
                        Text(
                            text = "Score: $scoreCount",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = RahiMutedGreen
                        )
                    }

                    Text(
                        text = currentQ.questionText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            lineHeight = 24.sp
                        ),
                        color = if (isDark) Color.White else RahiDeepNavy
                    )
                }
            }

            // Options List (A, B, C, D)
            val optionLetters = listOf("A", "B", "C", "D")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { idx, opt ->
                    val isSelected = selectedOption == idx
                    val isCorrect = idx == currentQ.correctIndex

                    val optBg = when {
                        isSubmitted && isCorrect -> if (isDark) Color(0xFF063323) else RahiMutedGreenContainer
                        isSubmitted && isSelected && !isCorrect -> if (isDark) Color(0xFF451A1A) else Color(0xFFFFECEB)
                        isSelected -> if (isDark) Color(0xFF132034) else RahiSoftBlueContainer
                        else -> cardBg
                    }

                    val optBorder = when {
                        isSubmitted && isCorrect -> RahiMutedGreen
                        isSubmitted && isSelected && !isCorrect -> Color(0xFFEF4444)
                        isSelected -> RahiSoftBlue
                        else -> cardBorder
                    }

                    Surface(
                        color = optBg,
                        border = BorderStroke(1.5.dp, optBorder),
                        shape = RoundedCornerShape(24.dp),
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isSubmitted) {
                                selectedOption = idx
                            }
                            .testTag("quiz_option_$idx")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSubmitted && isCorrect -> RahiMutedGreen
                                            isSubmitted && isSelected && !isCorrect -> Color(0xFFEF4444)
                                            isSelected -> RahiSoftBlue
                                            else -> if (isDark) Color(0xFF27354A) else Color(0xFFF1F5F9)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetters.getOrElse(idx) { "${idx + 1}" },
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected || (isSubmitted && isCorrect)) Color.White else (if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569))
                                )
                            }

                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 14.sp
                                ),
                                color = if (isDark) Color.White else RahiDeepNavy,
                                modifier = Modifier.weight(1f)
                            )

                            if (isSubmitted && isCorrect) {
                                Icon(Icons.Default.CheckCircle, null, tint = RahiMutedGreen, modifier = Modifier.size(20.dp))
                            } else if (isSubmitted && isSelected && !isCorrect) {
                                Icon(Icons.Default.Cancel, null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Explanation drawer when submitted
            AnimatedVisibility(visible = isSubmitted) {
                Surface(
                    color = if (isDark) Color(0xFF132034) else Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, cardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedOption == currentQ.correctIndex) Icons.Default.CheckCircle else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (selectedOption == currentQ.correctIndex) RahiMutedGreen else RahiWarmOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (selectedOption == currentQ.correctIndex) "Correct! Well Done" else "Explanation",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (selectedOption == currentQ.correctIndex) RahiMutedGreen else RahiWarmOrange
                            )
                        }
                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons: Submit / Next Question
            if (!isSubmitted) {
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            isSubmitted = true
                            if (selectedOption == currentQ.correctIndex) {
                                scoreCount++
                            }
                        }
                    },
                    enabled = selectedOption != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) RahiSoftBlue else RahiDeepNavy,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("quiz_submit_answer_btn")
                ) {
                    Text(
                        text = "Submit Answer",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            } else {
                Button(
                    onClick = {
                        if (currentIndex < totalQs - 1) {
                            currentIndex++
                            selectedOption = null
                            isSubmitted = false
                        } else {
                            isQuizCompleted = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) RahiSoftBlue else RahiDeepNavy,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("quiz_next_question_btn")
                ) {
                    Text(
                        text = if (currentIndex < totalQs - 1) "Next Question" else "See Results",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
