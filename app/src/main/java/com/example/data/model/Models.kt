package com.example.data.model

enum class NetworkMode(val label: String, val description: String) {
    ONLINE("Online", "Full access & cloud sync"),
    LOW_DATA("Low Data", "Audio & vector slides"),
    OFFLINE("Offline", "Cached local packs")
}

enum class AccessibilityMode(val displayName: String) {
    STANDARD("Standard Reading"),
    DYSLEXIA_FRIENDLY("Dyslexia-Friendly Font"),
    HIGH_CONTRAST("High Contrast Mode")
}

enum class NavigationTab(val title: String, val iconName: String) {
    HOME("Arena", "bolt"),
    LEARN("Learn", "menu_book"),
    PRACTICE("Drills", "fitness_center"),
    EXPLORE("Explore", "award_star"),
    PROFILE("Profile", "person")
}

sealed class ScreenDestination {
    object Onboarding : ScreenDestination()
    object SignIn : ScreenDestination()
    object RegisterStep1 : ScreenDestination()
    object RegisterStep2 : ScreenDestination()
    object MainApp : ScreenDestination()
    data class LessonReader(val moduleId: String = "binary_search") : ScreenDestination()
    object FocusMode : ScreenDestination()
    object AskDoubt : ScreenDestination()
    object Quiz : ScreenDestination()
    object Downloads : ScreenDestination()
    object Accessibility : ScreenDestination()
    object ProgressAnalytics : ScreenDestination()
}

data class CourseModule(
    val id: String,
    val title: String,
    val category: String, // "DSA", "Programming", "Mathematics", "Aptitude", "Career"
    val level: String, // "Beginner", "Intermediate"
    val totalLessons: Int,
    val completedLessons: Int,
    val progressPercent: Int,
    val isSavedOffline: Boolean,
    val downloadSizeMB: Int,
    val description: String,
    val imageUrl: String,
    val iconName: String
)

data class KeyPrinciple(
    val title: String,
    val description: String
)

data class LessonContent(
    val id: String,
    val moduleId: String,
    val title: String,
    val subtitle: String,
    val moduleName: String,
    val milestoneCurrent: Int,
    val milestoneTotal: Int,
    val progressPercent: Int,
    val timeComplexity: String,
    val coreConcept: String,
    val hindiConcept: String,
    val principles: List<KeyPrinciple>,
    val formulaTitle: String,
    val formulaCode: String,
    val examTrapWarning: String,
    val targetNumber: Int,
    val arrayElements: List<Int>,
    val quickCheckQuestion: String,
    val quickCheckAnswer: String
)

data class DoubtMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val isUser: Boolean,
    val codeSnippet: String? = null,
    val timestamp: String = "Just now"
)

data class QuizQuestion(
    val id: Int,
    val questionNumber: Int,
    val totalQuestions: Int,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class LearnerProfile(
    val name: String = "Priya Sharma",
    val email: String = "priya.sharma@aarohan.edu",
    val phone: String = "+91 98765 43210",
    val level: Int = 4,
    val educationLevel: String = "Undergraduate (B.Tech / B.Sc / BCA)",
    val branch: String = "Computer Science & Engineering",
    val interests: List<String> = listOf("Programming (Python/C++)", "Data Structures & Algorithms", "Mathematics for CS"),
    val weeklyHours: Float = 4.8f,
    val lessonsFinished: Int = 17,
    val quizAccuracyPercent: Int = 84,
    val streakDays: Int = 12,
    val storageUsedMB: Int = 320,
    val storageMaxMB: Int = 2048,
    val isBilingualActive: Boolean = true,
    val readingMode: AccessibilityMode = AccessibilityMode.STANDARD,
    val networkMode: NetworkMode = NetworkMode.ONLINE
)
