package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryBudget
import com.example.data.FamilyMember
import com.example.data.FamilyTransaction
import com.example.ui.PayFamViewModel
import com.example.ui.theme.*

/**
 * SCREEN 1: Onboarding / Welcome Screen
 * Bold lime green background with a custom-drawn wallet/cards canvas illustration,
 * matching layout 1 of the attached image.
 */
@Composable
fun OnboardingScreen(
    onLetStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VibrantLime)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = "PayFam Premium Logo",
                        tint = DeepSlateBlack,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PayFam",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = DeepSlateBlack,
                        letterSpacing = (-0.5).sp
                    )
                }
                Text(
                    text = "www.payfam.com",
                    fontSize = 12.sp,
                    color = DeepSlateBlack.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }

            // Central Canvas Drawing (Modern abstract family wallet & overlapping card)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(280.dp)
                        .testTag("onboarding_canvas")
                ) {
                    val canvasSize = size.minDimension
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // Draw primary decorative backdrop circle
                    drawCircle(
                        color = AccentLightGreen.copy(alpha = 0.35f),
                        radius = canvasSize * 0.45f,
                        center = center
                    )

                    // Draw abstract credit-card flying/rotated
                    val cardW = canvasSize * 0.5f
                    val cardH = canvasSize * 0.3f
                    val cardX = center.x - cardW * 0.6f
                    val cardY = center.y - cardH * 0.8f

                    // Rotated Card Represented via Path (subtle rotation)
                    val cardPath = Path().apply {
                        moveTo(cardX, cardY + 16f)
                        lineTo(cardX + cardW, cardY - 20f)
                        lineTo(cardX + cardW + 8f, cardY + cardH - 20f)
                        lineTo(cardX + 8f, cardY + cardH + 16f)
                        close()
                    }
                    drawPath(
                        path = cardPath,
                        color = DeepSlateBlack
                    )

                    // Drawing card details
                    drawCircle(
                        color = VibrantLime,
                        radius = 12f,
                        center = Offset(cardX + cardW - 30f, cardY + 20f)
                    )

                    // Draw primary wallet container
                    val walletW = canvasSize * 0.62f
                    val walletH = canvasSize * 0.44f
                    val walletX = center.x - walletW * 0.45f
                    val walletY = center.y - walletH * 0.1f

                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(walletX, walletY),
                        size = Size(walletW, walletH),
                        cornerRadius = CornerRadius(24f, 24f)
                    )

                    // Draw a modern pointing/gesturing line illustration with circle node accents
                    drawLine(
                        color = DeepSlateBlack,
                        start = Offset(center.x - 100f, center.y + 120f),
                        end = Offset(center.x - 20f, center.y + 60f),
                        strokeWidth = 6f
                    )
                    drawLine(
                        color = DeepSlateBlack,
                        start = Offset(center.x - 20f, center.y + 60f),
                        end = Offset(center.x + 80f, center.y + 90f),
                        strokeWidth = 6f
                    )
                    // Node Accent
                    drawCircle(
                        color = DeepSlateBlack,
                        radius = 14f,
                        center = Offset(center.x + 80f, center.y + 90f)
                    )
                }
            }

            // Heading Title and Subtext
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "La comptabilité\nsimplifiée pour toute la famille.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 38.sp,
                    color = DeepSlateBlack,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-1).sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Suivez vos dépenses familiales en temps réel, fixez des budgets ensemble et boostez l'épargne domestique de manière fluide.",
                    fontSize = 14.sp,
                    color = DeepSlateBlack.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer actions (Skip & Start Button)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Passer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepSlateBlack.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clickable { onLetStartClick() }
                        .padding(8.dp)
                        .testTag("skip_button")
                )

                // Circular modern button "Let's Start" with direct arrow icon
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .background(DeepSlateBlack)
                        .clickable { onLetStartClick() }
                        .padding(start = 22.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                        .testTag("lets_start_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Commencer",
                        color = VibrantLime,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(VibrantLime),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Aller à l'accueil",
                            tint = DeepSlateBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * SCREEN 2: Home Dashboard View
 * Showcases the Visa Card Balance, Fast Actions (Send, Bill, Mobile, More), Live Quick Filter
 * family members, and interactive lists of recent transaction.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    members: List<FamilyMember>,
    transactions: List<FamilyTransaction>,
    budgets: List<CategoryBudget>,
    viewModel: PayFamViewModel,
    onSetBudgetClick: () -> Unit,
    onAddTransactionClick: () -> Unit
) {
    var selectedFilterMember by remember { mutableStateOf<String?>(null) }
    var currentBalance = remember(transactions) {
        val earnings = transactions.filter { it.type == "EARNING" }.sumOf { it.amount }
        val spendings = transactions.filter { it.type == "SPENDING" }.sumOf { it.amount }
        earnings - spendings
    }

    val filteredTransactions = remember(transactions, selectedFilterMember) {
        if (selectedFilterMember == null) {
            transactions
        } else {
            transactions.filter { it.memberName.equals(selectedFilterMember, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Dashboard Greeting Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(VibrantLime),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👪",
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "William & Co",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Bienvenue 👋",
                            fontSize = 18.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                // Notification Bell icon
                IconButton(
                    onClick = { /* Demo notification alert */ },
                    modifier = Modifier
                        .shadow(4.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .size(40.dp)
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Alertes notification",
                            tint = TextPrimary
                        )
                        // Tiny notification dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SpendingCoral)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }

        // 2. Visa Card Balance Block (Reusable)
        item {
            PayFamCreditCard(
                balance = currentBalance,
                cardHolder = selectedFilterMember ?: "William & Marie",
                onSetBudgetClick = onSetBudgetClick,
                modifier = Modifier.testTag("credit_card_widget")
            )
        }

        // 3. Quick Action Menu Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val actions = listOf(
                    Triple("Envoi", Icons.Default.Send, "send_button"),
                    Triple("Factures", Icons.Default.Description, "bill_button"),
                    Triple("Mobile", Icons.Default.Smartphone, "mobile_button"),
                    Triple("Plus", Icons.Default.GridView, "more_button")
                )

                actions.forEach { (label, icon, tag) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Demo action */ }
                            .testTag(tag)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color(0xFFE2EACD), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = DeepSlateBlack,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 4. Quick family member filter (Quick Send Section in layout 2)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Membres Actifs",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = if (selectedFilterMember != null) "Effacer Filtre" else "Tout voir",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier
                            .clickable { selectedFilterMember = null }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(members) { member ->
                        val isSelected = selectedFilterMember == member.name
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    selectedFilterMember = if (isSelected) null else member.name
                                }
                                .testTag("member_filter_${member.name.lowercase()}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) VibrantLime else Color.White
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) DeepSlateBlack else Color(0xFFE5E5E5),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.avatarEmoji,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = member.name,
                                fontSize = 11.sp,
                                color = if (isSelected) DeepSlateBlack else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // 5. Recent Activity List Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activité Récente",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Ajouter +",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = VibrantLime,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepSlateBlack)
                        .clickable { onAddTransactionClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("shortcut_add_tx_button")
                )
            }
        }

        // 6. Recent Transactions List
        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune transaction enregistrée.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(filteredTransactions) { tx ->
                val isSpending = tx.type == "SPENDING"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clickable { /* Tap options */ }
                        .padding(14.dp)
                        .testTag("tx_item_${tx.id}"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Category Icon mapping
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isSpending) AccentLightGreen else VibrantLime),
                            contentAlignment = Alignment.Center
                        ) {
                            val categoryIcon = when (tx.category.lowercase()) {
                                "alimentation" -> Icons.Default.LocalPizza
                                "logement" -> Icons.Default.Home
                                "factures" -> Icons.Default.ReceiptLong
                                "loisirs" -> Icons.Default.SportsEsports
                                "santé" -> Icons.Default.MedicalServices
                                "transport" -> Icons.Default.DirectionsCar
                                "revenus" -> Icons.Default.TrendingUp
                                else -> Icons.Default.Payment
                            }
                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = tx.category,
                                tint = DeepSlateBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = tx.description,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${tx.memberName} • ${viewModel.formatEpoch(tx.timestamp)}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Display amount (Earnings positive, Spendings negative)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (isSpending) "-$ ${String.format("%.2f", tx.amount)}" else "+$ ${String.format("%.2f", tx.amount)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSpending) SpendingCoral else DeepSlateBlack
                        )
                        IconButton(
                            onClick = { viewModel.deleteTransaction(tx) },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Supprimer Transaction",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) } // Navigation dock cushion padding
    }
}

/**
 * SCREEN 3: Statistics Screen
 * Features back actions, tab selectors ("Semaine", "Mois" etc.), an Earning goal progress panel,
 * a gorgeous relative Category Radar Spider Chart, and stacked bar overview chart of monthly credits/debits.
 */
@Composable
fun StatisticsScreen(
    members: List<FamilyMember>,
    transactions: List<FamilyTransaction>,
    budgets: List<CategoryBudget>,
    viewModel: PayFamViewModel,
    onBack: () -> Unit
) {
    var periodSelected by remember { mutableStateOf("Monthly") }

    // Aggregate category expenses
    val categoriesExp = remember(transactions) {
        val mapped = mutableMapOf<String, Double>()
        transactions.filter { it.type == "SPENDING" }.forEach { tx ->
            mapped[tx.category] = (mapped[tx.category] ?: 0.0) + tx.amount
        }
        mapped
    }

    // Prepare Radar data (categories & normalized values)
    val radarCategories = listOf("Alimentation", "Logement", "Loisirs", "Factures", "Santé", "Transport")
    val radarScores = remember(categoriesExp) {
        val maxVal = categoriesExp.values.maxOrNull() ?: 1.0
        radarCategories.map { cat ->
            val spent = categoriesExp[cat] ?: 0.0
            (spent / maxVal).toFloat().coerceAtLeast(0.15f)
        }
    }

    val totalEarnings = remember(transactions) {
        transactions.filter { it.type == "EARNING" }.sumOf { it.amount }
    }

    val totalSpendings = remember(transactions) {
        transactions.filter { it.type == "SPENDING" }.sumOf { it.amount }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Navigation Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .shadow(2.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .size(40.dp)
                        .testTag("statistics_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retourner à l'accueil",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Statistiques",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )

                IconButton(
                    onClick = { /* Actions dropdown */ },
                    modifier = Modifier
                        .shadow(2.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Menu Plus",
                        tint = TextPrimary
                    )
                }
            }
        }

        // 2. Period/Period Selector Row (Today, Weekly, Monthly, Yearly)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE6ECD0), RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Today", "Weekly", "Monthly", "Yearly").forEach { period ->
                    val isSelected = periodSelected == period
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) VibrantLime else Color.Transparent)
                            .clickable { periodSelected = period }
                            .padding(vertical = 10.dp)
                            .testTag("period_tab_$period"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) DeepSlateBlack else TextSecondary
                        )
                    }
                }
            }
        }

        // 3. Earning goal progress and Spending Radar Chart Side-by-Side row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Earning progress Container (Lime Card)
                Card(
                    modifier = Modifier
                        .weight(1.1f)
                        .height(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = VibrantLime)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📈 Revenus",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepSlateBlack.copy(alpha = 0.6f)
                            )
                            IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.MoreHoriz, contentDescription = null, tint = DeepSlateBlack)
                            }
                        }

                        // Percentage increased text
                        Column {
                            Text(
                                text = "24%",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = DeepSlateBlack,
                                lineHeight = 36.sp
                            )
                            Text(
                                text = "Vos entrées de cash mensuelles ont augmenté de 24%.",
                                fontSize = 10.sp,
                                color = DeepSlateBlack.copy(alpha = 0.7f),
                                lineHeight = 13.sp
                            )
                        }

                        // Tiny Earning progress indicators
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Cible $10k", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DeepSlateBlack)
                                Text("$ ${String.format("%,.0f", totalEarnings)} / $15k", fontSize = 9.sp, color = DeepSlateBlack)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (totalEarnings / 15000f).toFloat().coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = DeepSlateBlack,
                                trackColor = Color.White.copy(alpha = 0.4f),
                            )
                        }
                    }
                }

                // Spending Radar card (Deep Slate Card)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepSlateBlack)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📉 Dépenses",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            IconButton(onClick = {}, modifier = Modifier.size(18.dp)) {
                                Icon(Icons.Default.MoreHoriz, contentDescription = null, tint = Color.White)
                            }
                        }

                        // Value
                        Text(
                            text = "$ ${String.format("%,.2f", totalSpendings)}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )

                        // Relative Spider Web Canvas
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            RadarSpiderChart(
                                categories = radarCategories,
                                values = radarScores,
                                modifier = Modifier.size(100.dp)
                            )
                        }

                        Text(
                            text = "Distribution Répartition",
                            fontSize = 9.sp,
                            color = VibrantLime,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 4. Overview Module Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aperçu Global",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filtres avancés",
                        tint = TextPrimary
                    )
                }
            }
        }

        // 5. Stacked Bar Chart (Credits vs Debits)
        item {
            StackedBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stacked_bars")
            )
        }

        // 6. Budgets limits list progress
        item {
            Text(
                text = "Suivi Budgétaire Limites",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (budgets.isEmpty()) {
            item {
                Text("Aucun budget créé. Cliquez sur '+ Fixer Budget' ci-dessus.", fontSize = 12.sp, color = TextSecondary)
            }
        } else {
            items(budgets) { b ->
                val spent = categoriesExp[b.category] ?: 0.0
                val ratio = (spent / b.limitAmount).toFloat().coerceIn(0f, 1f)
                val overBudget = spent > b.limitAmount

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(b.category, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            "${String.format("%.2f", spent)} $ / ${String.format("%.2f", b.limitAmount)} $",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (overBudget) SpendingCoral else TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { ratio },
                        color = if (overBudget) SpendingCoral else VibrantLime,
                        trackColor = Color(0xFFEEEEEE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}
