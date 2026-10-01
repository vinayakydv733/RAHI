package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.ui.AarohanViewModel
import com.example.ui.components.AarohanBottomNavigationBar
import com.example.ui.components.AarohanTopHeader

@Composable
fun MainAppContainer(
    viewModel: AarohanViewModel
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val modules by viewModel.modules.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val doubts by viewModel.doubts.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isOfflineFilterActive by viewModel.isOfflineFilterActive.collectAsStateWithLifecycle()
    val networkMode by viewModel.networkMode.collectAsStateWithLifecycle()
    val allRegisteredUsers by viewModel.allRegisteredUsers.collectAsStateWithLifecycle()
    val currentLesson by viewModel.currentLesson.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    val pendingFullName by viewModel.pendingFullName.collectAsStateWithLifecycle()
    val pendingEmail by viewModel.pendingEmail.collectAsStateWithLifecycle()
    val pendingPhone by viewModel.pendingPhone.collectAsStateWithLifecycle()
    val pendingPassword by viewModel.pendingPassword.collectAsStateWithLifecycle()
    val pendingEducationLevel by viewModel.pendingEducationLevel.collectAsStateWithLifecycle()
    val pendingStream by viewModel.pendingStream.collectAsStateWithLifecycle()
    val pendingInterests by viewModel.pendingInterests.collectAsStateWithLifecycle()

    var activeQuizId by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = currentScreen != ScreenDestination.MainApp || activeQuizId != null || currentTab != NavigationTab.HOME) {
        if (activeQuizId != null) {
            activeQuizId = null
        } else if (currentScreen != ScreenDestination.MainApp) {
            viewModel.navigateBack()
        } else if (currentTab != NavigationTab.HOME) {
            viewModel.selectTab(NavigationTab.HOME)
        }
    }

    when (val screen = currentScreen) {
        is ScreenDestination.Onboarding -> {
            OnboardingScreen(
                onFinish = { viewModel.navigateTo(ScreenDestination.SignIn) }
            )
        }
        is ScreenDestination.SignIn -> {
            SignInScreen(
                onSignIn = { identifier, password ->
                    viewModel.login(identifier, password) { success, errorMsg ->
                        if (!success) {
                            Toast.makeText(context, errorMsg ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onNavigateToRegister = { viewModel.navigateTo(ScreenDestination.RegisterStep1) },
                onToggleLowData = { viewModel.toggleNetworkMode() },
                registeredUsers = allRegisteredUsers,
                onQuickSwitchUser = { email ->
                    viewModel.switchUser(email)
                    viewModel.navigateTo(ScreenDestination.MainApp)
                }
            )
        }
        is ScreenDestination.RegisterStep1 -> {
            RegisterStep1Screen(
                fullName = pendingFullName,
                email = pendingEmail,
                phone = pendingPhone,
                password = pendingPassword,
                onUpdateFullName = { viewModel.pendingFullName.value = it },
                onUpdateEmail = { viewModel.pendingEmail.value = it },
                onUpdatePhone = { viewModel.pendingPhone.value = it },
                onUpdatePassword = { viewModel.pendingPassword.value = it },
                onContinue = { viewModel.navigateTo(ScreenDestination.RegisterStep2) },
                onBackToSignIn = { viewModel.navigateTo(ScreenDestination.SignIn) }
            )
        }
        is ScreenDestination.RegisterStep2 -> {
            RegisterStep2Screen(
                educationLevel = pendingEducationLevel,
                stream = pendingStream,
                selectedInterests = pendingInterests,
                onSelectEducation = { viewModel.pendingEducationLevel.value = it },
                onSelectStream = { viewModel.pendingStream.value = it },
                onToggleInterest = { interest ->
                    val current = viewModel.pendingInterests.value.toMutableList()
                    if (current.contains(interest)) current.remove(interest) else current.add(interest)
                    viewModel.pendingInterests.value = current
                },
                onCompleteRegistration = {
                    viewModel.completeRegistration { success, errorMsg ->
                        if (success) {
                            Toast.makeText(context, "Welcome to RAHI!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, errorMsg ?: "Registration error", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onBack = { viewModel.navigateBack() }
            )
        }
        is ScreenDestination.FocusMode -> {
            FocusModeScreen(
                onExitFocusMode = { viewModel.navigateBack() }
            )
        }
        is ScreenDestination.AskDoubt -> {
            AskDoubtScreen(
                doubts = doubts,
                onSendDoubt = { viewModel.askDoubt(it) },
                onBack = { viewModel.navigateBack() }
            )
        }
        is ScreenDestination.Quiz -> {
            val questions by viewModel.quizQuestions.collectAsStateWithLifecycle()
            QuizScreen(
                questions = questions,
                quizTitle = "Practice Quiz",
                onBack = { viewModel.navigateBack() }
            )
        }
        is ScreenDestination.Downloads -> {
            DownloadsScreen(
                modules = modules,
                storageUsedMB = profile.storageUsedMB,
                storageMaxMB = profile.storageMaxMB,
                onBack = { viewModel.navigateBack() },
                onDownloadMore = { viewModel.selectTab(NavigationTab.LEARN) },
                onDeleteModule = { viewModel.toggleDownload(it) }
            )
        }
        is ScreenDestination.Accessibility -> {
            AccessibilitySettingsScreen(
                currentMode = profile.readingMode,
                onModeSelected = { viewModel.setAccessibilityMode(it) },
                onBack = { viewModel.navigateBack() },
                isDarkTheme = isDarkTheme ?: false,
                onToggleDarkTheme = { viewModel.toggleDarkTheme() }
            )
        }
        is ScreenDestination.ProgressAnalytics -> {
            ProgressAnalyticsScreen(
                profile = profile,
                onBack = { viewModel.navigateBack() },
                onLogout = { viewModel.logout() },
                registeredUsers = allRegisteredUsers,
                onSwitchUser = { email -> viewModel.switchUser(email) }
            )
        }
        is ScreenDestination.LessonReader -> {
            Scaffold(
                topBar = {
                    AarohanTopHeader(
                        title = "Lesson Reader",
                        networkMode = networkMode,
                        onNetworkModeClick = { viewModel.toggleNetworkMode() },
                        onProfileClick = { viewModel.navigateTo(ScreenDestination.ProgressAnalytics) },
                        onBackClick = { viewModel.navigateBack() },
                        showBackButton = true,
                        isDarkTheme = isDarkTheme ?: false,
                        onToggleTheme = { viewModel.toggleDarkTheme() }
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    val lessonToDisplay = currentLesson ?: LessonContent(
                        id = "bs_1",
                        moduleId = "binary_search",
                        title = "Introduction to Binary Search",
                        subtitle = "Curated for Rural STEM Cohorts • Class 11-12 & First Year",
                        moduleName = "Foundations of Algorithmic Reasoning",
                        milestoneCurrent = 1,
                        milestoneTotal = 8,
                        progressPercent = 12,
                        timeComplexity = "O(log n)",
                        coreConcept = "Binary Search is an exceptionally efficient algorithm used to find the exact position of a target element in a sorted array by repeatedly dividing the search interval in half.",
                        hindiConcept = "जैसे किसी भारी शब्दकोश (Dictionary) में शब्द ढूंढते वक्त आप किताब के बीच का पन्ना खोलते हैं, वैसे ही बाइनरी सर्च हर कदम पर आधी सूची को छांट देता है।",
                        principles = listOf(
                            KeyPrinciple("Precondition: Sorted Collections", "Elements must already be sequenced in ascending or descending order."),
                            KeyPrinciple("Logarithmic Time: O(log n)", "In 1,000,000 items, it finds your element in at most 20 comparisons."),
                            KeyPrinciple("Middle Comparison Pivot", "Always evaluate target against array[mid] before picking left or right branch."),
                            KeyPrinciple("Zero-Waste Elimination", "Immediately discards 50% of remaining candidates at every step.")
                        ),
                        formulaTitle = "The Midpoint Formula",
                        formulaCode = "mid = low + ((high - low) / 2);",
                        examTrapWarning = "Do not write (low + high) / 2. When dealing with arrays larger than 2³⁰ elements, that addition overflows signed 32-bit integers and yields a negative index!",
                        targetNumber = 23,
                        arrayElements = listOf(2, 5, 8, 12, 16, 23, 38),
                        quickCheckQuestion = "Can Binary Search run on linked lists?",
                        quickCheckAnswer = "No! Linked lists do not support O(1) random memory indexing, making Binary Search inefficient O(n) on them."
                    )
                    LessonReaderScreen(
                        lesson = lessonToDisplay,
                        onBack = { viewModel.navigateBack() },
                        onOpenFocusMode = { viewModel.navigateTo(ScreenDestination.FocusMode) },
                        onOpenAskDoubt = { viewModel.navigateTo(ScreenDestination.AskDoubt) },
                        onNextLesson = { viewModel.navigateTo(ScreenDestination.Quiz) }
                    )
                }
            }
        }
        is ScreenDestination.MainApp -> {
            Scaffold(
                topBar = {
                    AarohanTopHeader(
                        title = when (currentTab) {
                            NavigationTab.HOME -> "RAHI • Practice Arena"
                            NavigationTab.LEARN -> "Learn Catalog"
                            NavigationTab.PRACTICE -> "Practice Drills"
                            NavigationTab.EXPLORE -> "Opportunities Hub"
                            NavigationTab.PROFILE -> "Learner Profile"
                        },
                        networkMode = networkMode,
                        onNetworkModeClick = { viewModel.toggleNetworkMode() },
                        onProfileClick = { viewModel.navigateTo(ScreenDestination.ProgressAnalytics) },
                        isDarkTheme = isDarkTheme ?: false,
                        onToggleTheme = { viewModel.toggleDarkTheme() }
                    )
                },
                bottomBar = {
                    AarohanBottomNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        NavigationTab.HOME -> {
                            val questions by viewModel.quizQuestions.collectAsStateWithLifecycle()
                            if (activeQuizId != null) {
                                QuizScreen(
                                    questions = questions,
                                    quizTitle = when (activeQuizId) {
                                        "daily_challenge" -> "Daily Challenge: Binary Search Boundary Invariant"
                                        "bs_bounds" -> "Binary Search Invariants & Bounds"
                                        "arrays_pointers" -> "Array Memory Pointers & Dynamic Lists"
                                        "linked_lists_fast_slow" -> "Linked List Fast-Slow Pointer Traversal"
                                        "math_cs_bits" -> "Bit Manipulation & Modular Arithmetic"
                                        "big_o_blitz" -> "Asymptotic Complexity & Big-O Blitz"
                                        "offline_mock_1" -> "Full Offline STEM Mock Test #1"
                                        "offline_reasoning" -> "Algorithmic Reasoning Mock Test"
                                        "offline_math_blitz" -> "Speed & Accuracy Math Blitz"
                                        else -> activeQuizId ?: "STEM Practice Drill"
                                    },
                                    onQuizCompleted = { score, total, xp ->
                                        viewModel.submitQuizResult(score, total, xp)
                                    },
                                    onBack = { activeQuizId = null }
                                )
                            } else {
                                PracticeArenaScreen(
                                    profile = profile,
                                    isDarkTheme = isDarkTheme ?: false,
                                    onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                                    initialMode = PracticeMode.DAILY_CHALLENGE,
                                    onStartQuiz = { quizId ->
                                        activeQuizId = quizId
                                    },
                                    onSelectTab = { tab ->
                                        viewModel.selectTab(tab)
                                    },
                                    onNavigateToScreen = { destination ->
                                        if (destination is ScreenDestination.LessonReader) {
                                            viewModel.openLesson(destination.moduleId)
                                        } else {
                                            viewModel.navigateTo(destination)
                                        }
                                    }
                                )
                            }
                        }
                        NavigationTab.LEARN -> {
                            LearnCatalogScreen(
                                modules = modules,
                                selectedCategory = selectedCategory,
                                isOfflineOnly = isOfflineFilterActive,
                                onCategorySelected = { viewModel.setCategory(it) },
                                onToggleOfflineFilter = { viewModel.toggleOfflineFilter() },
                                onToggleDownload = { viewModel.toggleDownload(it) },
                                onModuleClick = { moduleId ->
                                    viewModel.openLesson(if (moduleId.startsWith("bs")) moduleId else "bs_1")
                                }
                            )
                        }
                        NavigationTab.PRACTICE -> {
                            val questions by viewModel.quizQuestions.collectAsStateWithLifecycle()
                            if (activeQuizId != null) {
                                QuizScreen(
                                    questions = questions,
                                    quizTitle = when (activeQuizId) {
                                        "daily_challenge" -> "Daily Challenge: Binary Search Boundary Invariant"
                                        "bs_bounds" -> "Binary Search Invariants & Bounds"
                                        "arrays_pointers" -> "Array Memory Pointers & Dynamic Lists"
                                        "linked_lists_fast_slow" -> "Linked List Fast-Slow Pointer Traversal"
                                        "math_cs_bits" -> "Bit Manipulation & Modular Arithmetic"
                                        "big_o_blitz" -> "Asymptotic Complexity & Big-O Blitz"
                                        "offline_mock_1" -> "Full Offline STEM Mock Test #1"
                                        "offline_reasoning" -> "Algorithmic Reasoning Mock Test"
                                        "offline_math_blitz" -> "Speed & Accuracy Math Blitz"
                                        else -> activeQuizId ?: "STEM Practice Drill"
                                    },
                                    onQuizCompleted = { score, total, xp ->
                                        viewModel.submitQuizResult(score, total, xp)
                                    },
                                    onBack = { activeQuizId = null }
                                )
                            } else {
                                PracticeArenaScreen(
                                    profile = profile,
                                    isDarkTheme = isDarkTheme ?: false,
                                    onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                                    initialMode = PracticeMode.PRACTICE,
                                    onStartQuiz = { quizId ->
                                        activeQuizId = quizId
                                    },
                                    onSelectTab = { tab ->
                                        viewModel.selectTab(tab)
                                    },
                                    onNavigateToScreen = { destination ->
                                        if (destination is ScreenDestination.LessonReader) {
                                            viewModel.openLesson(destination.moduleId)
                                        } else {
                                            viewModel.navigateTo(destination)
                                        }
                                    }
                                )
                            }
                        }
                        NavigationTab.EXPLORE -> {
                            OpportunitiesHubScreen(
                                isDarkTheme = isDarkTheme ?: false,
                                onNavigateToDownloads = { viewModel.navigateTo(ScreenDestination.Downloads) }
                            )
                        }
                        NavigationTab.PROFILE -> {
                            ProgressAnalyticsScreen(
                                profile = profile,
                                onBack = { viewModel.selectTab(NavigationTab.HOME) },
                                onLogout = { viewModel.logout() },
                                registeredUsers = allRegisteredUsers,
                                onSwitchUser = { email -> viewModel.switchUser(email) }
                            )
                        }
                    }
                }
            }
        }
    }
}
