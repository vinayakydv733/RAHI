package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AarohanViewModel
import com.example.ui.screens.MainAppContainer
import com.example.ui.theme.AarohanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AarohanViewModel = viewModel()
            val customDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val isDark = customDarkTheme ?: systemDark

            AarohanTheme(darkTheme = isDark) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainAppContainer(viewModel = viewModel)
                }
            }
        }
    }
}
