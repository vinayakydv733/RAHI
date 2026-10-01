package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Smart Learning App UI Pastel Palette (Dribbble Inspiration)
val BackgroundSurface = Color(0xFFF5F6FA) // Off-white/slate grey background
val BackgroundGradientStart = Color(0xFFE8DDF5) // Soft pastel lavender
val BackgroundGradientEnd = Color(0xFFF3EEFA)

val PastelMagenta = Color(0xFFDF8BEB) // Active states, Ongoing tasks
val PastelMint = Color(0xFFCCEBDD) // Today's tasks, Analytics
val PastelBlue = Color(0xFFACD8F6) // Completed tasks
val PastelYellow = Color(0xFFF7E589) // Upcoming tasks, Pills
val CharcoalDark = Color(0xFF191C27) // Headings, Active states

val TextPrimary = CharcoalDark
val TextSecondary = Color(0xFF8C90A0)

// Standard Material Colors mapped to the aesthetic
val Primary = PastelMagenta
val OnPrimary = Color.White
val PrimaryContainer = PastelMagenta.copy(alpha = 0.2f)
val OnPrimaryContainer = CharcoalDark

val Secondary = PastelMint
val OnSecondary = CharcoalDark
val SecondaryContainer = PastelMint.copy(alpha = 0.5f)
val OnSecondaryContainer = CharcoalDark

val Tertiary = PastelBlue
val OnTertiary = CharcoalDark
val TertiaryContainer = PastelBlue.copy(alpha = 0.5f)
val OnTertiaryContainer = CharcoalDark

val Surface = BackgroundSurface
val OnSurface = TextPrimary
val SurfaceVariant = Color.White
val OnSurfaceVariant = TextSecondary

val Outline = Color(0xFFE5E7EB)
val OutlineVariant = Color(0xFFF3F4F6)

// Tri-State Network Modes (Kept for compatibility)
val OnlineGreen = PastelMint
val OnlineGreenBg = PastelMint.copy(alpha = 0.3f)
val LowDataAmber = PastelYellow
val LowDataAmberBg = PastelYellow.copy(alpha = 0.3f)
val OfflineSlate = TextSecondary
val OfflineSlateBg = OutlineVariant

// Focus Mode OLED Palette (Kept for compatibility)
val FocusDarkBg = Color(0xFF0B0F19)
val FocusDarkSurface = Color(0xFF131B2E)
val FocusHighlightGold = PastelYellow
val FocusSecondaryMint = PastelMint

// RAHI Official Design System Palette (Mapped to pastel variants)
val RahiDeepNavy = CharcoalDark          
val RahiNavySurface = CharcoalDark       
val RahiNavyBorder = Outline        

val RahiOffWhite = BackgroundSurface          
val RahiOffWhiteCard = Color(0xFFFFFFFF)      
val RahiOffWhiteBorder = Outline    

val RahiMutedGreen = PastelMint        
val RahiMutedGreenContainer = PastelMint.copy(alpha = 0.3f)
val RahiOnMutedGreenContainer = CharcoalDark

val RahiSoftBlue = PastelBlue          
val RahiSoftBlueContainer = PastelBlue.copy(alpha = 0.3f)
val RahiOnSoftBlueContainer = CharcoalDark

val RahiWarmOrange = PastelYellow        
val RahiWarmOrangeContainer = PastelYellow.copy(alpha = 0.3f)
val RahiOnWarmOrangeContainer = CharcoalDark
