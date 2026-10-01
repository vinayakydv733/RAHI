package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class Opportunity(
    val id: String,
    val title: String,
    val provider: String,
    val category: String, // "Scholarship", "Internship", "Hackathon", "Placement"
    val stipendOrAward: String,
    val deadline: String,
    val daysLeft: Int,
    val eligibility: String,
    val mode: String, // "Online", "Hybrid", "National"
    val description: String,
    var isBookmarked: Boolean = false,
    var isApplied: Boolean = false
)

@Composable
fun OpportunitiesHubScreen(
    isDarkTheme: Boolean = false,
    onNavigateToDownloads: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedOpportunityForDetails by remember { mutableStateOf<Opportunity?>(null) }

    val categories = listOf("All", "Scholarships", "Internships", "Hackathons", "Placements")

    var opportunities by remember {
        mutableStateOf(
            listOf(
                Opportunity(
                    id = "nsp_stem_2026",
                    title = "National Scholarship for STEM Higher Education",
                    provider = "Ministry of Education & MeitY",
                    category = "Scholarships",
                    stipendOrAward = "₹50,000 / year",
                    deadline = "Oct 25, 2026",
                    daysLeft = 14,
                    eligibility = "Undergraduate B.Tech / BCA / B.Sc (Rural / Tier-2/3 priority)",
                    mode = "National",
                    description = "Direct benefit scholarship for meritorious technical and mathematical sciences students enrolled in accredited Indian universities."
                ),
                Opportunity(
                    id = "pragati_aicte",
                    title = "AICTE Pragati Technical Scholarship for Women",
                    provider = "All India Council for Technical Education",
                    category = "Scholarships",
                    stipendOrAward = "₹50,000 / year + Tuition",
                    deadline = "Nov 10, 2026",
                    daysLeft = 28,
                    eligibility = "Female technical students (1st & 2nd year degree/diploma)",
                    mode = "National",
                    description = "Empowering female technical scholars with financial grants for books, tuition, and equipment."
                ),
                Opportunity(
                    id = "gsoc_summer",
                    title = "Google Summer of Code Open Source Fellowship",
                    provider = "Google Open Source Initiative",
                    category = "Internships",
                    stipendOrAward = "₹1,80,000 - ₹3,20,000",
                    deadline = "Oct 18, 2026",
                    daysLeft = 7,
                    eligibility = "Open to university students with DSA & Git skills",
                    mode = "Remote / Online",
                    description = "12-week global coding program matching students with open-source mentors to build real-world software libraries."
                ),
                Opportunity(
                    id = "isro_student_intern",
                    title = "ISRO Satellite Data & Computing Internship",
                    provider = "Indian Space Research Organisation (URSC)",
                    category = "Internships",
                    stipendOrAward = "₹18,000 / month",
                    deadline = "Nov 05, 2026",
                    daysLeft = 24,
                    eligibility = "Pre-final & Final year CSE, IT, ECE, Mathematics",
                    mode = "Bengaluru / Hybrid",
                    description = "Hands-on engineering internship in geospatial data processing, orbital calculations, and telemetry systems."
                ),
                Opportunity(
                    id = "sih_national_2026",
                    title = "Smart India Hackathon (SIH) 2026",
                    provider = "AICTE & MoE Innovation Cell",
                    category = "Hackathons",
                    stipendOrAward = "₹1,00,000 Winner Prize",
                    deadline = "Oct 20, 2026",
                    daysLeft = 9,
                    eligibility = "Teams of 6 (College Students)",
                    mode = "Nationwide Finals",
                    description = "World's biggest open innovation hackathon solving challenges posed by ministries and industrial partners."
                ),
                Opportunity(
                    id = "tcs_nqt_drive",
                    title = "TCS National Qualifier Test (NQT) Drive",
                    provider = "Tata Consultancy Services",
                    category = "Placements",
                    stipendOrAward = "₹3.6L - ₹9.0L CTC",
                    deadline = "Oct 30, 2026",
                    daysLeft = 19,
                    eligibility = "2025/2026 Batch Graduates (Engineering / Science)",
                    mode = "Off-Campus Test",
                    description = "Standardized multi-level assessment connecting top scorers to prime IT, AI, and digital engineering positions."
                )
            )
        )
    }

    val filteredList = remember(selectedCategory, searchQuery, opportunities) {
        opportunities.filter { opp ->
            val matchCat = selectedCategory == "All" || opp.category.equals(selectedCategory, ignoreCase = true)
            val matchQuery = searchQuery.isBlank() ||
                    opp.title.contains(searchQuery, ignoreCase = true) ||
                    opp.provider.contains(searchQuery, ignoreCase = true) ||
                    opp.eligibility.contains(searchQuery, ignoreCase = true)
            matchCat && matchQuery
        }
    }

    val cardBg = if (isDarkTheme) RahiNavySurface else RahiOffWhiteCard
    val cardBorder = if (isDarkTheme) RahiNavyBorder else RahiOffWhiteBorder

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Hero Header Card
            Surface(
                color = if (isDarkTheme) Color(0xFF132034) else RahiSoftBlueContainer,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, RahiSoftBlue.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = RahiWarmOrangeContainer,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(0.5.dp, RahiWarmOrange.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Verified, null, tint = RahiWarmOrange, modifier = Modifier.size(12.dp))
                                Text(
                                    text = "VERIFIED PORTAL",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.ExtraBold),
                                    color = RahiOnWarmOrangeContainer
                                )
                            }
                        }

                        Text(
                            text = "Opportunities Hub",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            color = if (isDarkTheme) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "Scholarships, paid internships, national hackathons & campus placement drives tailored for STEM learners.",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                            color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF334155)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1E3A5F) else Color(0xFFDBEAFE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Opportunities",
                            tint = RahiSoftBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Search scholarships, GSoC, SIH, internships...",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        color = if (isSelected) (if (isDarkTheme) RahiSoftBlue else RahiDeepNavy) else (if (isDarkTheme) RahiNavySurface else Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else cardBorder),
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("opp_cat_${cat.lowercase()}")
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = if (isSelected) Color.White else (if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // List of Opportunities
        items(filteredList, key = { it.id }) { opp ->
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, cardBorder),
                shadowElevation = if (isDarkTheme) 0.dp else 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header Row: Category Badge + Days left + Bookmark
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = when (opp.category) {
                                    "Scholarships" -> if (isDarkTheme) Color(0xFF063323) else RahiMutedGreenContainer
                                    "Internships" -> if (isDarkTheme) Color(0xFF132034) else RahiSoftBlueContainer
                                    "Hackathons" -> if (isDarkTheme) Color(0xFF45220A) else RahiWarmOrangeContainer
                                    else -> if (isDarkTheme) Color(0xFF27354A) else Color(0xFFF1F5F9)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = opp.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 10.sp),
                                    color = when (opp.category) {
                                        "Scholarships" -> if (isDarkTheme) Color(0xFF6EE7B7) else RahiOnMutedGreenContainer
                                        "Internships" -> if (isDarkTheme) Color(0xFF93C5FD) else RahiOnSoftBlueContainer
                                        "Hackathons" -> RahiWarmOrange
                                        else -> if (isDarkTheme) Color.White else RahiDeepNavy
                                    },
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }

                            Surface(
                                color = if (opp.daysLeft <= 7) Color(0xFFFFECEB) else (if (isDarkTheme) Color(0xFF27354A) else Color(0xFFF1F5F9)),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = if (opp.daysLeft <= 7) Color(0xFFDC2626) else (if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "${opp.daysLeft}d left",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        color = if (opp.daysLeft <= 7) Color(0xFFDC2626) else (if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B))
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                opportunities = opportunities.map {
                                    if (it.id == opp.id) it.copy(isBookmarked = !it.isBookmarked) else it
                                }
                                Toast.makeText(
                                    context,
                                    if (!opp.isBookmarked) "Opportunity saved to bookmarks!" else "Removed from bookmarks",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (opp.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (opp.isBookmarked) RahiWarmOrange else (if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Title & Provider
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = opp.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                            color = if (isDarkTheme) Color.White else RahiDeepNavy
                        )
                        Text(
                            text = "by ${opp.provider} • ${opp.mode}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                        )
                    }

                    // Highlights pill row: Stipend + Eligibility
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "Grant / Stipend",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                                Text(
                                    text = opp.stipendOrAward,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RahiMutedGreen
                                )
                            }
                        }

                        Surface(
                            color = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            modifier = Modifier.weight(1.4f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "Eligibility",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                                Text(
                                    text = opp.eligibility,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
                                    color = if (isDarkTheme) Color.White else RahiDeepNavy,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Action buttons: Details & Apply Now
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { selectedOpportunityForDetails = opp },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, if (isDarkTheme) RahiSoftBlue else RahiDeepNavy),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "View Details",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isDarkTheme) RahiSoftBlue else RahiDeepNavy
                            )
                        }

                        Button(
                            onClick = {
                                opportunities = opportunities.map {
                                    if (it.id == opp.id) it.copy(isApplied = true) else it
                                }
                                Toast.makeText(
                                    context,
                                    "Application Submitted for ${opp.title}! Tracking active in profile.",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            enabled = !opp.isApplied,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (opp.isApplied) RahiMutedGreen else (if (isDarkTheme) RahiSoftBlue else RahiDeepNavy),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (opp.isApplied) {
                                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp))
                                    Text(text = "Applied", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                } else {
                                    Text(text = "Apply Now", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                    Icon(Icons.Default.ArrowOutward, null, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Details Dialog
    selectedOpportunityForDetails?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedOpportunityForDetails = null },
            title = {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Provider: ${item.provider}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    Text(text = "Award / Stipend: ${item.stipendOrAward}", style = MaterialTheme.typography.bodySmall.copy(color = RahiMutedGreen, fontWeight = FontWeight.Bold))
                    Text(text = "Deadline: ${item.deadline} (${item.daysLeft} days remaining)", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Eligibility Criteria: ${item.eligibility}", style = MaterialTheme.typography.bodySmall)
                    Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        opportunities = opportunities.map {
                            if (it.id == item.id) it.copy(isApplied = true) else it
                        }
                        Toast.makeText(context, "Application initiated for ${item.title}!", Toast.LENGTH_SHORT).show()
                        selectedOpportunityForDetails = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RahiDeepNavy)
                ) {
                    Text("Proceed to Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedOpportunityForDetails = null }) {
                    Text("Close")
                }
            }
        )
    }
}
