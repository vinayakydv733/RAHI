package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AarohanDatabase
import com.example.data.local.LessonContentEntity
import com.example.data.local.UserAccountEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RAHI", appName)
    }

    @Test
    fun `verify room database stores and retrieves stem lessons and user accounts`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AarohanDatabase.getDatabase(context)
        val dao = db.aarohanDao()

        // Insert test user
        val testUser = UserAccountEntity(
            email = "student101@aarohan.edu",
            fullName = "Kavita Rao",
            phone = "+91 99999 88888",
            password = "securePassword",
            educationLevel = "Undergraduate",
            stream = "Computer Science",
            interestsCsv = "DSA,Math",
            isActiveSession = true
        )
        dao.insertUser(testUser)

        val retrievedUser = dao.findUserByEmail("student101@aarohan.edu")
        assertNotNull(retrievedUser)
        assertEquals("Kavita Rao", retrievedUser?.fullName)

        // Insert test STEM lesson
        val testLesson = LessonContentEntity(
            id = "test_stem_1",
            moduleId = "binary_search",
            milestoneCurrent = 1,
            milestoneTotal = 8,
            title = "Test Divide & Conquer",
            subtitle = "STEM Module",
            moduleName = "Algorithms",
            timeComplexity = "O(log n)",
            coreConcept = "Divide and conquer",
            hindiConcept = "विभाजन और विजय",
            principlesCsv = "Sorted:::Array must be sorted",
            formulaTitle = "Mid formula",
            formulaCode = "mid = low + (high - low)/2",
            examTrapWarning = "Integer overflow warning",
            targetNumber = 23,
            arrayElementsCsv = "2,5,8,12,16,23,38",
            quickCheckQuestion = "Can BS run on linked lists?",
            quickCheckAnswer = "No"
        )
        dao.insertLessons(listOf(testLesson))

        val retrievedLesson = dao.getLessonById("test_stem_1").first()
        assertNotNull(retrievedLesson)
        assertEquals("Test Divide & Conquer", retrievedLesson?.title)
    }
}
