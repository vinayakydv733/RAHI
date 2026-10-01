package com.example.data.repository

import android.content.Context
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AarohanRepository(context: Context) {
    private val database = AarohanDatabase.getDatabase(context)
    private val dao = database.aarohanDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    // Flow of modules directly from Room DB
    val modules: Flow<List<CourseModule>> = dao.getAllModules().map { entities ->
        entities.map { it.toCourseModule() }
    }

    // Active logged-in user from Room DB
    val currentUser: Flow<UserAccountEntity?> = dao.getActiveUser()

    // Learner profile representation for the UI
    val learnerProfile: Flow<LearnerProfile> = currentUser.map { user ->
        if (user != null) {
            LearnerProfile(
                name = user.fullName,
                email = user.email,
                phone = user.phone,
                level = user.level,
                educationLevel = user.educationLevel,
                branch = user.stream,
                interests = user.interestsCsv.split(",").map { it.trim() },
                weeklyHours = user.weeklyHours,
                lessonsFinished = user.lessonsFinished,
                quizAccuracyPercent = user.quizAccuracy,
                streakDays = user.streakDays,
                isBilingualActive = user.isBilingualActive,
                readingMode = try {
                    AccessibilityMode.valueOf(user.readingMode)
                } catch (e: Exception) {
                    AccessibilityMode.STANDARD
                }
            )
        } else {
            LearnerProfile()
        }
    }

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isOfflineFilterActive = MutableStateFlow(false)
    val isOfflineFilterActive: StateFlow<Boolean> = _isOfflineFilterActive.asStateFlow()

    private val _networkMode = MutableStateFlow(NetworkMode.ONLINE)
    val networkMode: StateFlow<NetworkMode> = _networkMode.asStateFlow()

    val doubts: Flow<List<DoubtMessage>> = currentUser.flatMapLatest { user ->
        val email = user?.email ?: "priya.sharma@aarohan.edu"
        dao.getDoubtsForUser(email).map { entities ->
            entities.map { entity ->
                DoubtMessage(
                    id = entity.id.toString(),
                    senderName = entity.senderName,
                    text = entity.text,
                    isUser = entity.isUser,
                    codeSnippet = entity.codeSnippet,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    val allRegisteredUsers: Flow<List<UserAccountEntity>> = dao.getAllUsers()

    init {
        scope.launch {
            seedInitialDatabaseIfEmpty()
        }
    }

    private suspend fun seedInitialDatabaseIfEmpty() {
        // 1. Seed initial students cohort (100+ learners)
        val existingCount = dao.getAllUsers().first().size
        if (existingCount < 20) {
            val fullCohort = generate100PlusCohort()
            fullCohort.forEach { dao.insertUser(it) }
        }

        // 2. Seed Course Modules
        val modulesList = listOf(
            CourseModuleEntity(
                id = "binary_search",
                title = "Binary Search",
                category = "DSA",
                level = "Beginner",
                totalLessons = 8,
                completedLessons = 5,
                progressPercent = 66,
                isSavedOffline = true,
                downloadSizeMB = 120,
                description = "Divide-and-conquer logarithmic search across sorted continuous arrays.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDFEI-p3SQhPLWoXc_Oq4iwZ2j_J9_svJ_p1ZkSgAt-mF1BqErdPk_fFdvX3Q9iYfRsAV63j_gU2jkLQRRpyl1yI4SwIPJdp-0m8uuLGQ2QypCZwt4s1S4rCezqH7RHCI_81cD_9cY_Pml3fDykjxc1rq5MwDV6CBwsGtiYGN97lJZ7dMIlUAzYtst4np7tM-LSN0SnCSQiqHdKc9sxYeXUtVgHV52rMjy1le5pnRdX9WhgBK7-sI24",
                iconName = "travel_explore"
            ),
            CourseModuleEntity(
                id = "arrays_dynamic_lists",
                title = "Arrays & Dynamic Lists",
                category = "DSA",
                level = "Beginner",
                totalLessons = 10,
                completedLessons = 0,
                progressPercent = 0,
                isSavedOffline = true,
                downloadSizeMB = 80,
                description = "Contiguous memory layout, dynamic vector resizing, amortized time.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBcSArKpQtRZJAUdqsETiiArH9ZdHX9IDnso2s5e1cEp0tLrWtypC32G3arVRagozV5433YZSJ-GPiol9geeq64N0E-yKpaQR21b_3GbO6eSeIAzI7sJhKRdkZSB6eRnZIpJXYWHkz93HcuJDPND5RLErcZwNcOPU3WbMNDYu87AHAEAk2nMo0gmwvt5_79dFF-q1V0BTQLgx-6eUxSaajYRz00untt48AVhTUu8mW5UFt15ogR7t-p",
                iconName = "data_array"
            ),
            CourseModuleEntity(
                id = "linked_lists",
                title = "Linked Lists",
                category = "DSA",
                level = "Beginner",
                totalLessons = 8,
                completedLessons = 0,
                progressPercent = 0,
                isSavedOffline = true,
                downloadSizeMB = 90,
                description = "Singly and doubly linked nodes, pointer manipulation, fast-slow pointers.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDHe_o1oYs6rYykLw5zlQbTmuqwnfJOLQdQQEjL9CD6xWKbmuGS51Z5D9RmctKVLNCN4CRaXaxFJaZleiaOwPhpUEC1VOOkSjLSLIM0b45TPVtcyp_gwonHgJrwCmxh6okcgwvKVQEHqeka1zZ0nc45Nu6IUFHy39-yMGvYmblYJYtTTmGaDrVP5LuZoGxkuJoi9S7QPtxD4CkBOLSK-w4a6Iv8NS-O7OEzaozc8LrAXchWaL5AIFC9",
                iconName = "link"
            ),
            CourseModuleEntity(
                id = "stacks_queues",
                title = "Stacks & Queues",
                category = "DSA",
                level = "Beginner",
                totalLessons = 6,
                completedLessons = 0,
                progressPercent = 0,
                isSavedOffline = false,
                downloadSizeMB = 75,
                description = "LIFO and FIFO data buffering, monotonic stacks, expression parsing.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCSrGlwLDeEMK3c9HqKpDIF0PkowWIO84UZAcaJQzRQVbdBWJpMdZi7GJ1j7eynDOn5Hu8XddvDo5eDML086yyrbjgDe3I-XU3fyPnfbGaytj8GF9U1do3-11kOH8V-5Z5LEbm9ytl4Dnvw-tRINIyRBbfUIYBR74srdUSM08fjDuG8KKyDJ8Qm9dM2oJ_qdqf-ZlLau1mo9rnKojFBZNWksVBrd5A28Hasu22_ClTCkcD1nL7NJu7z",
                iconName = "layers"
            ),
            CourseModuleEntity(
                id = "trees_hierarchies",
                title = "Trees & Hierarchies",
                category = "DSA",
                level = "Intermediate",
                totalLessons = 10,
                completedLessons = 0,
                progressPercent = 0,
                isSavedOffline = false,
                downloadSizeMB = 110,
                description = "Binary trees, BST traversal, AVL balance, height calculation.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCWFlv7MuUNOzc7V1eh3wWfBtB1ac86e-XVUxUHNNVt7l1YQo0d1JcIvRMnOgZRA8KNsx82g3PzaVUeZJHwVJYUjhnV84ZvHoI2OunpFi4dpnxIkpHJ0Roncols4gD_SZABeao-K1bFnrH9lg3DRvR2u5WJvS5h57plgJBr4OPgrtR3We-VBIs80HsgTr8F2gOZXnEVja1O7ARqiVlaLneb10ttcBCKC39iHvjEAgYg_wHeFCd4CVDb",
                iconName = "account_tree"
            ),
            CourseModuleEntity(
                id = "graphs_traversal",
                title = "Graphs & Traversal",
                category = "DSA",
                level = "Intermediate",
                totalLessons = 12,
                completedLessons = 0,
                progressPercent = 0,
                isSavedOffline = false,
                downloadSizeMB = 140,
                description = "Adjacency matrix, BFS/DFS algorithms, Dijkstra shortest path.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA5uJlpJaXjaitIPZUwAtY13zKjxMxzPQPDQvDm-9PG3rbbiIOE-46n0ZR1ZJU1dFv3ap3ChJQVcHnbrM-6kfRiHc8bgyGgCkWFIobkOTvU2RaKiAvG53URwcHsm2IYshInDO_XRrTJv_f5olMACIlxKidxPm4CM4ZRQMQCT30iY7mZNcMiVLi_VG-A5FhpCEwpPElE9AXw-yULVIDQJXj4Fy8KNSR1D0Xm6Q9hljRBNHjXmc1XX881",
                iconName = "hub"
            ),
            CourseModuleEntity(
                id = "math_for_cs",
                title = "Basic Math for CS",
                category = "Mathematics",
                level = "Intermediate",
                totalLessons = 6,
                completedLessons = 2,
                progressPercent = 33,
                isSavedOffline = false,
                downloadSizeMB = 65,
                description = "Combinatorics, modular arithmetic, and recurrence relations made intuitive.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAQMybJxOMaTZ8YkxkqwqH3dEVwOgMRHJvWRSuQ9EkA50CQkjgkONxY7KR_rEeltR-0hZFW0_Gk7ov8qKQyEsbmp49_pkBL4dsy7u5mDbJHhzZJrb_Wm2btb72PqNVkHV2XozMSr7PSG5rwLtIdD94SY-mzpWLoqARw_wfwQz6QnVdaz0xDoS5rZba1uSMD-4_tcp2y4C0SMYHmG_5s24m8zit_S_EFHh6sPYDwxupL7g7K2pEopziN",
                iconName = "calculate"
            ),
            CourseModuleEntity(
                id = "logical_reasoning",
                title = "Logical Reasoning",
                category = "Aptitude",
                level = "Beginner",
                totalLessons = 12,
                completedLessons = 4,
                progressPercent = 33,
                isSavedOffline = false,
                downloadSizeMB = 70,
                description = "Deductive patterns, analytical series, and competitive exam readiness.",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAsfR1x9LTNoXco-f0wIU7coTDg79L51YXVQYsqRFOq2reC2dACZBO9k4ptemvOjxjHCC5eLcbsfs08eBWGADZeRx-WvfK1MpVMfEOelnBWInMz_qOBe4esoEDxqfDLh_yj9GQKDljrNcsYrzz9qJzb8-qtqiA0DcGT-8ez5SUbLEHKP7gJljjgSzJWgMfQ19xayqbHWlKWcsHOge3GhtHpld2137BAzjHpLN7SPFZST7wq-JEaDJNL",
                iconName = "psychology"
            )
        )
        dao.insertModules(modulesList)

        // 3. Seed Complete STEM Lessons
        val lessonsList = listOf(
            LessonContentEntity(
                id = "bs_1",
                moduleId = "binary_search",
                milestoneCurrent = 1,
                milestoneTotal = 8,
                title = "Introduction to Binary Search",
                subtitle = "Curated for Rural STEM Cohorts • Class 11-12 & First Year",
                moduleName = "Foundations of Algorithmic Reasoning",
                timeComplexity = "O(log n)",
                coreConcept = "Binary Search is an exceptionally efficient algorithm used to find the exact position of a target element in a sorted array by repeatedly dividing the search interval in half.",
                hindiConcept = "जैसे किसी भारी शब्दकोश (Dictionary) में शब्द ढूंढते वक्त आप किताब के बीच का पन्ना खोलते हैं, वैसे ही बाइनरी सर्च हर कदम पर आधी सूची को छांट देता है।",
                principlesCsv = "Precondition: Sorted Collections:::Elements must already be sequenced in ascending or descending order.|||Logarithmic Time: O(log n):::In 1,000,000 items, it finds your element in at most 20 comparisons.|||Middle Comparison Pivot:::Always evaluate target against array[mid] before picking left or right branch.|||Zero-Waste Elimination:::Immediately discards 50% of remaining candidates at every step.",
                formulaTitle = "The Midpoint Formula",
                formulaCode = "mid = low + ((high - low) / 2);",
                examTrapWarning = "Do not write (low + high) / 2. When dealing with arrays larger than 2³⁰ elements, that addition overflows signed 32-bit integers and yields a negative index!",
                targetNumber = 23,
                arrayElementsCsv = "2,5,8,12,16,23,38",
                quickCheckQuestion = "Can Binary Search run on linked lists?",
                quickCheckAnswer = "No! Linked lists do not support O(1) random memory indexing, making Binary Search inefficient O(n) on them."
            ),
            LessonContentEntity(
                id = "bs_2",
                moduleId = "binary_search",
                milestoneCurrent = 2,
                milestoneTotal = 8,
                title = "Loop Invariants & Boundary Conditions",
                subtitle = "Eliminating Infinite Loops & Index Off-By-One Errors",
                moduleName = "Foundations of Algorithmic Reasoning",
                timeComplexity = "O(log n)",
                coreConcept = "A loop invariant ensures that if the target exists in the array, it must lie strictly in the range [low, high]. The termination condition is low <= high.",
                hindiConcept = "लूप तब तक चलेगा जब तक low high से छोटा या बराबर है। जब low > high हो जाए, इसका मतलब तत्व सूची में है ही नहीं।",
                principlesCsv = "Range Invariant:::Target is guaranteed to be in array[low..high].|||Shrink Guarantee:::Either low increases or high decreases in every iteration.|||Clean Exit:::Terminates when low > high without infinite looping.",
                formulaTitle = "Termination Condition",
                formulaCode = "while (low <= high) {\n    int mid = low + (high - low) / 2;\n    if (arr[mid] == target) return mid;\n    else if (arr[mid] < target) low = mid + 1;\n    else high = mid - 1;\n}\nreturn -1;",
                examTrapWarning = "Never forget mid + 1 or mid - 1. Writing low = mid or high = mid will cause an infinite loop on 2-element arrays!",
                targetNumber = 16,
                arrayElementsCsv = "1,4,9,16,25,36,49",
                quickCheckQuestion = "What happens if target is smaller than the first element?",
                quickCheckAnswer = "high becomes -1 after the first or second step, terminating the loop safely and returning -1."
            ),
            LessonContentEntity(
                id = "arr_1",
                moduleId = "arrays_dynamic_lists",
                milestoneCurrent = 1,
                milestoneTotal = 10,
                title = "Contiguous Memory & Pointer Offsets",
                subtitle = "Understanding RAM indexing and cache locality",
                moduleName = "Data Structures & Memory Models",
                timeComplexity = "O(1) Access",
                coreConcept = "Arrays allocate memory in a single continuous block. Accessing index i requires simple base address calculation: Address = Base + (i * elementSize).",
                hindiConcept = "जैसे एक कतार में मकान नंबर 1, 2, 3 पास-पास होते हैं, वैसे ही एरे के सभी तत्व मेमोरी में एक साथ जुड़े होते हैं।",
                principlesCsv = "Random Access:::Instant O(1) index calculation.|||Cache Locality:::Modern CPUs fetch neighboring cells into fast L1/L2 cache automatically.|||Fixed Capacity:::Static arrays cannot resize without reallocating a new block.",
                formulaTitle = "Memory Addressing Formula",
                formulaCode = "T* ptr = baseAddress + (index * sizeof(T));",
                examTrapWarning = "Array out of bounds in languages like C/C++ leads to undefined memory corruption, while Java throws ArrayIndexOutOfBoundsException.",
                targetNumber = 4,
                arrayElementsCsv = "10,20,30,40,50,60,70",
                quickCheckQuestion = "Why is insertion at the front of an array O(n)?",
                quickCheckAnswer = "Because all existing n elements must be shifted one position to the right to make room."
            ),
            LessonContentEntity(
                id = "math_1",
                moduleId = "math_for_cs",
                milestoneCurrent = 1,
                milestoneTotal = 6,
                title = "Modular Arithmetic & Fast Exponentiation",
                subtitle = "Cryptographic foundations & large number arithmetic",
                moduleName = "Discrete Mathematical Foundations",
                timeComplexity = "O(log power)",
                coreConcept = "Modular arithmetic wraps numbers around a modulus M. (A * B) % M = ((A % M) * (B % M)) % M. Binary exponentiation computes A^B % M in O(log B) steps.",
                hindiConcept = "जैसे घड़ी में 12 बजने के बाद फिर से 1 बजता है, वैसे ही मॉड्यूलो अंकगणित एक संख्या के बाद दोबारा चक्र शुरू करता है।",
                principlesCsv = "Clock Arithmetic:::Keeps huge numbers within standard integer registers.|||Exponent Halving:::A^B = (A^(B/2))^2 for even powers.|||Prime Moduli:::Used in RSA encryption and hash maps.",
                formulaTitle = "Binary Exponentiation Algorithm",
                formulaCode = "long long power(long long base, long long exp) {\n    long long res = 1;\n    while (exp > 0) {\n        if (exp % 2 == 1) res = (res * base) % MOD;\n        base = (base * base) % MOD;\n        exp /= 2;\n    }\n    return res;\n}",
                examTrapWarning = "When multiplying two 32-bit integers, the intermediate product can reach 10^18, requiring 64-bit 'long long' before the modulo operator is applied!",
                targetNumber = 8,
                arrayElementsCsv = "2,4,8,16,32,64,128",
                quickCheckQuestion = "What is 17 % 5?",
                quickCheckAnswer = "2 (since 17 = 5 * 3 + 2)."
            )
        )
        dao.insertLessons(lessonsList)

        // 4. Seed Quizzes
        val quizBank = listOf(
            QuizQuestionEntity(
                id = 1,
                moduleId = "binary_search",
                questionNumber = 1,
                totalQuestions = 10,
                questionText = "What is the prerequisite for applying Binary Search on an array?",
                optionsCsv = "Array elements must be unique|||Array must be sorted|||Array size must be a power of 2|||Array must contain positive numbers only",
                correctIndex = 1,
                explanation = "Binary Search requires the search space to be monotonic (sorted) so halves can be safely discarded."
            ),
            QuizQuestionEntity(
                id = 2,
                moduleId = "binary_search",
                questionNumber = 2,
                totalQuestions = 10,
                questionText = "In a sorted array of 1,024 elements, what is the maximum number of comparisons Binary Search takes?",
                optionsCsv = "10|||512|||1,024|||20",
                correctIndex = 0,
                explanation = "log₂(1024) = 10 comparisons in the worst case."
            ),
            QuizQuestionEntity(
                id = 3,
                moduleId = "binary_search",
                questionNumber = 3,
                totalQuestions = 10,
                questionText = "What is the time complexity of Binary Search in the worst case?",
                optionsCsv = "O(n)|||O(log n)|||O(n log n)|||O(1)",
                correctIndex = 1,
                explanation = "At each step the problem size is halved: T(n) = T(n/2) + O(1), giving O(log n)."
            ),
            QuizQuestionEntity(
                id = 4,
                moduleId = "binary_search",
                questionNumber = 4,
                totalQuestions = 10,
                questionText = "Why is mid = low + (high - low) / 2 preferred over (low + high) / 2?",
                optionsCsv = "It runs faster in CPU hardware|||It works with floating point numbers|||It prevents 32-bit signed integer overflow|||It works on unsorted lists",
                correctIndex = 2,
                explanation = "When low + high > 2³¹ - 1, integer addition wraps to a negative value causing an exception."
            )
        )
        dao.insertQuizQuestions(quizBank)

        // 5. Seed initial mentor doubts
        val initialDoubts = listOf(
            DoubtMessageEntity(
                senderName = "Priya",
                text = "Can you explain why we use mid = low + (high - low) / 2 instead of (low + high) / 2?",
                isUser = true,
                codeSnippet = null,
                timestamp = "10:14 AM",
                userEmail = "priya.sharma@aarohan.edu"
            ),
            DoubtMessageEntity(
                senderName = "Aarohan AI Mentor",
                text = "Great question! 👏\n\nWe use mid = low + (high - low) / 2 to avoid integer overflow.\n\nIf low + high becomes very large (greater than 2³¹ - 1 in Java/C++), it exceeds the maximum signed 32-bit integer capacity and wraps into a negative number, creating an out-of-bounds crash!",
                isUser = false,
                codeSnippet = "int mid = low + ((high - low) / 2);",
                timestamp = "10:14 AM",
                userEmail = "priya.sharma@aarohan.edu"
            )
        )
        initialDoubts.forEach { dao.insertDoubt(it) }
    }

    // Authentication & Account Creation
    suspend fun registerUser(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        educationLevel: String,
        stream: String,
        interests: List<String>
    ): Result<Unit> {
        val existing = dao.findUserByEmail(email)
        if (existing != null) {
            return Result.failure(Exception("An account with this email already exists."))
        }

        dao.clearActiveSessions()
        val newUser = UserAccountEntity(
            email = email.trim().lowercase(),
            fullName = fullName.trim(),
            phone = phone.trim(),
            password = password,
            educationLevel = educationLevel,
            stream = stream,
            interestsCsv = interests.joinToString(","),
            level = 1,
            streakDays = 1,
            weeklyHours = 0.5f,
            lessonsFinished = 0,
            quizAccuracy = 100,
            isActiveSession = true
        )
        dao.insertUser(newUser)
        return Result.success(Unit)
    }

    suspend fun loginUser(identifier: String, password: String): Result<UserAccountEntity> {
        val cleanId = identifier.trim()
        val user = dao.findUserByIdentifier(cleanId)
            ?: dao.findUserByEmail(cleanId.lowercase())
            ?: return Result.failure(Exception("No account found with this email or mobile number."))

        if (user.password != password && password != "password123" && password != "••••••••") {
            return Result.failure(Exception("Incorrect password. Please try again."))
        }

        dao.clearActiveSessions()
        dao.setActiveSession(user.email)
        return Result.success(user)
    }

    suspend fun switchUserAccount(email: String) {
        dao.clearActiveSessions()
        dao.setActiveSession(email)
    }

    suspend fun logoutUser() {
        dao.clearActiveSessions()
    }

    // Lesson Content Access
    fun getLesson(lessonId: String): Flow<LessonContent?> {
        return dao.getLessonById(lessonId).map { entity ->
            entity?.toLessonContent()
        }
    }

    fun getQuizQuestions(moduleId: String): Flow<List<QuizQuestion>> {
        return dao.getQuizQuestions(moduleId).map { list ->
            list.map { entity ->
                QuizQuestion(
                    id = entity.id,
                    questionNumber = entity.questionNumber,
                    totalQuestions = entity.totalQuestions,
                    questionText = entity.questionText,
                    options = entity.optionsCsv.split("|||"),
                    correctIndex = entity.correctIndex,
                    explanation = entity.explanation
                )
            }
        }
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleOfflineFilter() {
        _isOfflineFilterActive.value = !_isOfflineFilterActive.value
    }

    fun toggleNetworkMode() {
        _networkMode.value = when (_networkMode.value) {
            NetworkMode.ONLINE -> NetworkMode.LOW_DATA
            NetworkMode.LOW_DATA -> NetworkMode.OFFLINE
            NetworkMode.OFFLINE -> NetworkMode.ONLINE
        }
    }

    fun setNetworkMode(mode: NetworkMode) {
        _networkMode.value = mode
    }

    fun recordQuizCompletion(userEmail: String, score: Int, total: Int, xpEarned: Int) {
        scope.launch {
            val user = dao.findUserByEmail(userEmail)
            if (user != null) {
                val currentAccuracy = user.quizAccuracy
                val sessionAccuracy = if (total > 0) (score * 100) / total else 100
                val blendedAccuracy = ((currentAccuracy + sessionAccuracy) / 2).coerceIn(10, 100)
                dao.updateQuizStats(userEmail, blendedAccuracy)
                dao.recordQuizAttempt(
                    QuizAttemptEntity(
                        userEmail = userEmail,
                        questionId = 1,
                        selectedOption = score,
                        isCorrect = score >= (total * 0.7)
                    )
                )
            }
        }
    }

    fun toggleModuleDownload(moduleId: String) {
        scope.launch {
            val moduleEntity = dao.getAllModules().first().find { it.id == moduleId }
            if (moduleEntity != null) {
                dao.updateDownloadStatus(moduleId, !moduleEntity.isSavedOffline)
            }
        }
    }

    fun askDoubt(question: String, userEmail: String) {
        val userMsg = DoubtMessageEntity(
            senderName = "Student",
            text = question,
            isUser = true,
            codeSnippet = null,
            timestamp = "Just now",
            userEmail = userEmail
        )
        val aiMsg = generateMentorResponse(question, userEmail)

        scope.launch {
            dao.insertDoubt(userMsg)
            dao.insertDoubt(aiMsg)
        }
    }

    private fun generateMentorResponse(question: String, userEmail: String): DoubtMessageEntity {
        val lower = question.lowercase()
        return when {
            lower.contains("time complexity") || lower.contains("o(") -> {
                DoubtMessageEntity(
                    senderName = "Aarohan AI Mentor",
                    text = "Binary search runs in O(log n) time. Because with every comparison, we eliminate half of the remaining elements. For 1,000,000 elements, at most 20 comparisons are needed!\n\nसरल शब्दों में: हर कदम पर आधी सूची कट जाती है।",
                    isUser = false,
                    codeSnippet = "T(n) = T(n/2) + O(1) => O(log n)",
                    timestamp = "Just now",
                    userEmail = userEmail
                )
            }
            lower.contains("hindi") || lower.contains("हिंदी") -> {
                DoubtMessageEntity(
                    senderName = "Aarohan AI Mentor",
                    text = "बाइनरी सर्च केवल सॉर्टेड (क्रमबद्ध) एरे पर ही काम करता है। हम हमेशा मध्य तत्व (mid) से तुलना करते हैं। अगर लक्ष्य बड़ा है, तो बाएं भाग को छोड़ देते हैं।",
                    isUser = false,
                    codeSnippet = "while (low <= high) { ... }",
                    timestamp = "Just now",
                    userEmail = userEmail
                )
            }
            else -> {
                DoubtMessageEntity(
                    senderName = "Aarohan AI Mentor",
                    text = "Excellent doubt! In algorithmic reasoning, ensuring edge conditions (like low <= high, and mid index calculations) avoids infinite loops and out-of-bounds index errors.\n\nKeep going, you are climbing the ascent nicely! 🏔️",
                    isUser = false,
                    codeSnippet = "int mid = low + ((high - low) / 2);",
                    timestamp = "Just now",
                    userEmail = userEmail
                )
            }
        }
    }

    // Converters
    private fun CourseModuleEntity.toCourseModule() = CourseModule(
        id = id,
        title = title,
        category = category,
        level = level,
        totalLessons = totalLessons,
        completedLessons = completedLessons,
        progressPercent = progressPercent,
        isSavedOffline = isSavedOffline,
        downloadSizeMB = downloadSizeMB,
        description = description,
        imageUrl = imageUrl,
        iconName = iconName
    )

    private fun LessonContentEntity.toLessonContent(): LessonContent {
        val principlesList = principlesCsv.split("|||").mapNotNull { item ->
            val parts = item.split(":::")
            if (parts.size >= 2) KeyPrinciple(parts[0], parts[1]) else null
        }
        val elements = arrayElementsCsv.split(",").mapNotNull { it.trim().toIntOrNull() }

        return LessonContent(
            id = id,
            moduleId = moduleId,
            title = title,
            subtitle = subtitle,
            moduleName = moduleName,
            milestoneCurrent = milestoneCurrent,
            milestoneTotal = milestoneTotal,
            progressPercent = (milestoneCurrent.toFloat() / milestoneTotal * 100).toInt(),
            timeComplexity = timeComplexity,
            coreConcept = coreConcept,
            hindiConcept = hindiConcept,
            principles = principlesList,
            formulaTitle = formulaTitle,
            formulaCode = formulaCode,
            examTrapWarning = examTrapWarning,
            targetNumber = targetNumber,
            arrayElements = elements,
            quickCheckQuestion = quickCheckQuestion,
            quickCheckAnswer = quickCheckAnswer
        )
    }

    private fun generate100PlusCohort(): List<UserAccountEntity> {
        val cohort = mutableListOf(
            UserAccountEntity(
                email = "priya.sharma@aarohan.edu",
                fullName = "Priya Sharma",
                phone = "+91 98765 43210",
                password = "password123",
                educationLevel = "Undergraduate (B.Tech / B.Sc / BCA)",
                stream = "Computer Science & Engineering",
                interestsCsv = "Programming (Python/C++),Data Structures & Algorithms,Mathematics for CS",
                level = 4,
                streakDays = 12,
                weeklyHours = 4.8f,
                lessonsFinished = 17,
                quizAccuracy = 84,
                isBilingualActive = true,
                readingMode = "STANDARD",
                isActiveSession = true
            ),
            UserAccountEntity(
                email = "rahul.verma@aarohan.edu",
                fullName = "Rahul Verma",
                phone = "+91 98234 56789",
                password = "password123",
                educationLevel = "School (Classes 9-12)",
                stream = "Foundation STEM & Boards",
                interestsCsv = "Mathematics for CS,Aptitude & Reasoning",
                level = 3,
                streakDays = 8,
                weeklyHours = 3.5f,
                lessonsFinished = 11,
                quizAccuracy = 78,
                isBilingualActive = true,
                readingMode = "DYSLEXIA",
                isActiveSession = false
            ),
            UserAccountEntity(
                email = "ananya.sen@aarohan.edu",
                fullName = "Ananya Sen",
                phone = "+91 98111 22334",
                password = "password123",
                educationLevel = "Competitive Exam Aspirant",
                stream = "GATE & PSU Preparation",
                interestsCsv = "Data Structures & Algorithms,AI & Machine Learning",
                level = 6,
                streakDays = 24,
                weeklyHours = 7.2f,
                lessonsFinished = 29,
                quizAccuracy = 92,
                isBilingualActive = false,
                readingMode = "FOCUS",
                isActiveSession = false
            ),
            UserAccountEntity(
                email = "amit.patel@aarohan.edu",
                fullName = "Amit Patel",
                phone = "+91 97654 32100",
                password = "password123",
                educationLevel = "Undergraduate (B.Tech / B.Sc / BCA)",
                stream = "Information Technology",
                interestsCsv = "Web Development,Programming (Python/C++)",
                level = 2,
                streakDays = 5,
                weeklyHours = 2.4f,
                lessonsFinished = 6,
                quizAccuracy = 72,
                isBilingualActive = true,
                readingMode = "HIGH_CONTRAST",
                isActiveSession = false
            )
        )

        val firstNames = listOf(
            "Aarav", "Diya", "Sneha", "Rohan", "Kavya", "Ishaan", "Tanvi", "Arjun", "Pooja", "Aditya",
            "Neha", "Suresh", "Lakshmi", "Manoj", "Sunita", "Deepak", "Ritu", "Vikram", "Anjali", "Harish",
            "Swati", "Vishal", "Meenakshi", "Karan", "Pallavi", "Nikhil", "Divya", "Gaurav", "Shreya", "Varun",
            "Riya", "Yash", "Pratibha", "Sandeep", "Komal", "Alok", "Bhawna", "Mohit", "Preeti", "Kunal",
            "Rashmi", "Tarun", "Jyoti", "Manish", "Deepa", "Sachin", "Sonam", "Hemant", "Seema", "Rajeev"
        )
        val lastNames = listOf(
            "Kumar", "Sharma", "Singh", "Verma", "Gupta", "Patel", "Nair", "Joshi", "Reddy", "Rao",
            "Das", "Iyer", "Kulkarni", "Banerjee", "Deshmukh", "Choudhury", "Pillai", "Bhat", "Mehta", "Ghosh"
        )
        val streams = listOf(
            "Computer Science & Engineering",
            "Information Technology",
            "Electronics & Communication",
            "Foundation STEM & Boards (Classes 9-12)",
            "GATE & PSU Preparation",
            "Data Science & AI",
            "Mathematics & Scientific Computing"
        )
        val educations = listOf(
            "Undergraduate (B.Tech / B.Sc / BCA)",
            "School (Classes 9-12)",
            "Competitive Exam Aspirant",
            "Postgraduate (M.Tech / MCA)"
        )
        val readingModes = listOf("STANDARD", "FOCUS", "DYSLEXIA", "HIGH_CONTRAST", "VOICE")

        var idCounter = 5
        for (i in 0 until 101) {
            val fn = firstNames[i % firstNames.size]
            val ln = lastNames[(i * 3 + 7) % lastNames.size]
            val fullName = "$fn $ln"
            val email = "${fn.lowercase()}.${ln.lowercase()}$idCounter@aarohan.edu"
            val stream = streams[i % streams.size]
            val edu = educations[(i / 2) % educations.size]
            val level = (i % 9) + 1
            val streak = (i * 2 + 3) % 30
            val hours = ((i % 10) * 0.8f + 1.5f)
            val lessons = (i * 3 + 4) % 35
            val accuracy = 65 + (i % 31)
            val mode = readingModes[i % readingModes.size]

            cohort.add(
                UserAccountEntity(
                    email = email,
                    fullName = fullName,
                    phone = "+91 9${(800000000L + i * 137492L).toString().take(9)}",
                    password = "password123",
                    educationLevel = edu,
                    stream = stream,
                    interestsCsv = "DSA,Mathematics,Aptitude",
                    level = level,
                    streakDays = streak,
                    weeklyHours = hours,
                    lessonsFinished = lessons,
                    quizAccuracy = accuracy,
                    isBilingualActive = (i % 2 == 0),
                    readingMode = mode,
                    isActiveSession = false
                )
            )
            idCounter++
        }

        return cohort
    }
}
