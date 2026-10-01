package com.example.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AarohanDao {
    // Users & Authentication
    @Query("SELECT * FROM user_accounts WHERE isActiveSession = 1 LIMIT 1")
    fun getActiveUser(): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE email = :identifier OR phone = :identifier LIMIT 1")
    suspend fun findUserByIdentifier(identifier: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts")
    fun getAllUsers(): Flow<List<UserAccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserAccountEntity)

    @Query("UPDATE user_accounts SET isActiveSession = 0")
    suspend fun clearActiveSessions()

    @Query("UPDATE user_accounts SET isActiveSession = 1 WHERE email = :email")
    suspend fun setActiveSession(email: String)

    @Query("UPDATE user_accounts SET lessonsFinished = lessonsFinished + 1, streakDays = streakDays + 1 WHERE email = :email")
    suspend fun incrementUserProgress(email: String)

    @Query("UPDATE user_accounts SET streakDays = streakDays + 1, lessonsFinished = lessonsFinished + 1, quizAccuracy = :newAccuracy WHERE email = :email")
    suspend fun updateQuizStats(email: String, newAccuracy: Int)

    // Course Modules
    @Query("SELECT * FROM course_modules ORDER BY id ASC")
    fun getAllModules(): Flow<List<CourseModuleEntity>>

    @Query("SELECT * FROM course_modules WHERE id = :moduleId LIMIT 1")
    fun getModule(moduleId: String): Flow<CourseModuleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<CourseModuleEntity>)

    @Query("UPDATE course_modules SET isSavedOffline = :isSaved WHERE id = :moduleId")
    suspend fun updateDownloadStatus(moduleId: String, isSaved: Boolean)

    @Query("UPDATE course_modules SET progressPercent = :percent, completedLessons = :completed WHERE id = :moduleId")
    suspend fun updateModuleProgress(moduleId: String, completed: Int, percent: Int)

    // STEM Lesson Content
    @Query("SELECT * FROM lesson_contents WHERE id = :id LIMIT 1")
    fun getLessonById(id: String): Flow<LessonContentEntity?>

    @Query("SELECT * FROM lesson_contents WHERE moduleId = :moduleId ORDER BY milestoneCurrent ASC")
    fun getLessonsByModule(moduleId: String): Flow<List<LessonContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonContentEntity>)

    // Quizzes
    @Query("SELECT * FROM quiz_questions WHERE moduleId = :moduleId ORDER BY questionNumber ASC")
    fun getQuizQuestions(moduleId: String): Flow<List<QuizQuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestionEntity>)

    @Insert
    suspend fun recordQuizAttempt(attempt: QuizAttemptEntity)

    // Doubts / Mentorship Chat
    @Query("SELECT * FROM doubt_messages WHERE userEmail = :userEmail ORDER BY id ASC")
    fun getDoubtsForUser(userEmail: String): Flow<List<DoubtMessageEntity>>

    @Insert
    suspend fun insertDoubt(doubt: DoubtMessageEntity)
}

@Database(
    entities = [
        UserAccountEntity::class,
        CourseModuleEntity::class,
        LessonContentEntity::class,
        QuizQuestionEntity::class,
        DoubtMessageEntity::class,
        QuizAttemptEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AarohanDatabase : RoomDatabase() {
    abstract fun aarohanDao(): AarohanDao

    companion object {
        @Volatile
        private var INSTANCE: AarohanDatabase? = null

        fun getDatabase(context: Context): AarohanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AarohanDatabase::class.java,
                    "aarohan_stem_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
