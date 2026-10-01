package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserAccountEntity
import com.example.data.model.*
import com.example.data.repository.AarohanRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AarohanViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AarohanRepository(application)

    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.MainApp)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null) // null = system default, false = light, true = dark
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    fun toggleDarkTheme() {
        val current = _isDarkTheme.value ?: false
        _isDarkTheme.value = !current
    }

    fun setDarkTheme(isDark: Boolean?) {
        _isDarkTheme.value = isDark
    }

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _activeLessonId = MutableStateFlow("bs_1")
    val activeLessonId: StateFlow<String> = _activeLessonId.asStateFlow()

    private val screenStack = mutableListOf<ScreenDestination>()

    // Room-backed reactive flows
    val modules: StateFlow<List<CourseModule>> = repository.modules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profile: StateFlow<LearnerProfile> = repository.learnerProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LearnerProfile())

    val currentUser: StateFlow<UserAccountEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allRegisteredUsers: StateFlow<List<UserAccountEntity>> = repository.allRegisteredUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doubts: StateFlow<List<DoubtMessage>> = repository.doubts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedCategory: StateFlow<String> = repository.selectedCategory
    val isOfflineFilterActive: StateFlow<Boolean> = repository.isOfflineFilterActive
    val networkMode: StateFlow<NetworkMode> = repository.networkMode

    val currentLesson: StateFlow<LessonContent?> = _activeLessonId
        .flatMapLatest { id -> repository.getLesson(id) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val quizQuestions: StateFlow<List<QuizQuestion>> = repository.getQuizQuestions("binary_search")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Registration in-progress state across steps
    var pendingFullName = MutableStateFlow("Priya Sharma")
    var pendingEmail = MutableStateFlow("priya.sharma@aarohan.edu")
    var pendingPhone = MutableStateFlow("+91 98765 43210")
    var pendingPassword = MutableStateFlow("password123")
    var pendingEducationLevel = MutableStateFlow("Undergraduate (B.Tech / B.Sc / BCA)")
    var pendingStream = MutableStateFlow("Computer Science & Engineering")
    var pendingInterests = MutableStateFlow(listOf("Programming (Python/C++)", "Data Structures & Algorithms", "Mathematics for CS"))

    fun navigateTo(destination: ScreenDestination) {
        screenStack.add(_currentScreen.value)
        _currentScreen.value = destination
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            return true
        }
        if (_currentScreen.value != ScreenDestination.MainApp) {
            _currentScreen.value = ScreenDestination.MainApp
            return true
        }
        return false
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
        _currentScreen.value = ScreenDestination.MainApp
    }

    fun openLesson(lessonId: String) {
        _activeLessonId.value = lessonId
        navigateTo(ScreenDestination.LessonReader(lessonId))
    }

    fun setCategory(category: String) {
        repository.setCategory(category)
    }

    fun toggleOfflineFilter() {
        repository.toggleOfflineFilter()
    }

    fun toggleNetworkMode() {
        repository.toggleNetworkMode()
    }

    fun toggleDownload(moduleId: String) {
        repository.toggleModuleDownload(moduleId)
    }

    fun askDoubt(text: String) {
        val email = currentUser.value?.email ?: "priya.sharma@aarohan.edu"
        repository.askDoubt(text, email)
    }

    fun setAccessibilityMode(mode: AccessibilityMode) {
        // Saved in UI state
    }

    fun submitQuizResult(score: Int, total: Int, xpEarned: Int) {
        val email = currentUser.value?.email ?: "priya.sharma@aarohan.edu"
        repository.recordQuizCompletion(email, score, total, xpEarned)
    }

    fun login(identifier: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.loginUser(identifier, password)
            if (result.isSuccess) {
                navigateTo(ScreenDestination.MainApp)
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun completeRegistration(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.registerUser(
                fullName = pendingFullName.value,
                email = pendingEmail.value,
                phone = pendingPhone.value,
                password = pendingPassword.value,
                educationLevel = pendingEducationLevel.value,
                stream = pendingStream.value,
                interests = pendingInterests.value
            )
            if (result.isSuccess) {
                navigateTo(ScreenDestination.MainApp)
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun switchUser(email: String) {
        viewModelScope.launch {
            repository.switchUserAccount(email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutUser()
            navigateTo(ScreenDestination.SignIn)
        }
    }
}
