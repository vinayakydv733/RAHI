package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey val email: String,
    val fullName: String,
    val phone: String,
    val password: String,
    val educationLevel: String = "Undergraduate (B.Tech / B.Sc / BCA)",
    val stream: String = "Computer Science & Engineering",
    val interestsCsv: String = "Programming (Python/C++),Data Structures & Algorithms,Mathematics for CS",
    val level: Int = 4,
    val streakDays: Int = 12,
    val weeklyHours: Float = 4.8f,
    val lessonsFinished: Int = 17,
    val quizAccuracy: Int = 84,
    val isBilingualActive: Boolean = true,
    val readingMode: String = "STANDARD",
    val isActiveSession: Boolean = false
)

@Entity(tableName = "course_modules")
data class CourseModuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val level: String,
    val totalLessons: Int,
    val completedLessons: Int,
    val progressPercent: Int,
    val isSavedOffline: Boolean,
    val downloadSizeMB: Int,
    val description: String,
    val imageUrl: String,
    val iconName: String
)

@Entity(tableName = "lesson_contents")
data class LessonContentEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val milestoneCurrent: Int,
    val milestoneTotal: Int,
    val title: String,
    val subtitle: String,
    val moduleName: String,
    val timeComplexity: String,
    val coreConcept: String,
    val hindiConcept: String,
    val principlesCsv: String, // Delimited "title:::description|||title:::description"
    val formulaTitle: String,
    val formulaCode: String,
    val examTrapWarning: String,
    val targetNumber: Int,
    val arrayElementsCsv: String, // "2,5,8,12,16,23,38"
    val quickCheckQuestion: String,
    val quickCheckAnswer: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey val id: Int,
    val moduleId: String,
    val questionNumber: Int,
    val totalQuestions: Int,
    val questionText: String,
    val optionsCsv: String, // "Option 1|||Option 2|||Option 3|||Option 4"
    val correctIndex: Int,
    val explanation: String
)

@Entity(tableName = "doubt_messages")
data class DoubtMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderName: String,
    val text: String,
    val isUser: Boolean,
    val codeSnippet: String?,
    val timestamp: String,
    val userEmail: String = "priya.sharma@aarohan.edu"
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userEmail: String,
    val questionId: Int,
    val selectedOption: Int,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
