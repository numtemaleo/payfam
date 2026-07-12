package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.PayFamViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PayFamAppMain()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PayFamAppMain() {
    val viewModel: PayFamViewModel = viewModel()
    
    // Core data bindings from Room Flow
    val members by viewModel.members.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    // Navigation state machine
    var currentScreen by remember { mutableStateOf("onboarding") } // "onboarding", "home", "statistics"
    
    // Popup Dialogue states
    var showAddTxDialog by remember { mutableStateOf(false) }
    var showSetBudgetDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCreamBackground)
    ) {
        // Safe Edge Area Container
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Crossfade screen navigation
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        slideInHorizontally { width -> if (targetState == "onboarding") -width else width } + fadeIn() with
                                slideOutHorizontally { width -> if (targetState == "onboarding") width else -width } + fadeOut()
                    },
                    label = "screen_navigation"
                ) { screen ->
                    when (screen) {
                        "onboarding" -> {
                            OnboardingScreen(
                                onLetStartClick = { currentScreen = "home" }
                            )
                        }
                        "home" -> {
                            HomeScreen(
                                members = members,
                                transactions = transactions,
                                budgets = budgets,
                                viewModel = viewModel,
                                onSetBudgetClick = { showSetBudgetDialog = true },
                                onAddTransactionClick = { showAddTxDialog = true }
                            )
                        }
                        "statistics" -> {
                            StatisticsScreen(
                                members = members,
                                transactions = transactions,
                                budgets = budgets,
                                viewModel = viewModel,
                                onBack = { currentScreen = "home" }
                            )
                        }
                    }
                }

                // SCREEN 2 & 3: Pill Dock Navigation Bottom Bar (Matching attached mockups exactly)
                if (currentScreen != "onboarding") {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp, start = 32.dp, end = 32.dp)
                            .shadow(24.dp, RoundedCornerShape(28.dp))
                            .clip(RoundedCornerShape(28.dp))
                            .background(DeepSlateBlack)
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("floating_pill_dock")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(280.dp)
                        ) {
                            // Dashboard Tab Icon
                            IconButton(
                                onClick = { currentScreen = "home" },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .testTag("dock_home_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Wallet,
                                    contentDescription = "Accueil Dashboard",
                                    tint = if (currentScreen == "home") VibrantLime else Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // Center Action Button "+ Add / Scan"
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(VibrantLime)
                                    .clickable { showAddTxDialog = true }
                                    .testTag("dock_scan_add_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterCenterFocus,
                                    contentDescription = "Ajouter ou Scanner",
                                    tint = DeepSlateBlack,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            // Statistics Tab Icon
                            IconButton(
                                onClick = { currentScreen = "statistics" },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .testTag("dock_stats_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = "Statistiques Rapports",
                                    tint = if (currentScreen == "statistics") VibrantLime else Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Dialogs
        if (showAddTxDialog && members.isNotEmpty()) {
            AddTransactionDialog(
                membersList = members,
                categoriesList = categories,
                onDismiss = { showAddTxDialog = false },
                onSave = { name, amount, type, category, description, method ->
                    viewModel.addTransaction(name, amount, type, category, description, method)
                    showAddTxDialog = false
                },
                onAddCategory = { newCategory ->
                    viewModel.addCategory(newCategory)
                }
            )
        }

        if (showSetBudgetDialog) {
            SetBudgetDialog(
                categories = categories.map { it.name },
                onDismiss = { showSetBudgetDialog = false },
                onSave = { category, limit ->
                    viewModel.setBudgetLimit(category, limit)
                    showSetBudgetDialog = false
                }
            )
        }
    }
}
