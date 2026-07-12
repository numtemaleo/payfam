package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.CategoryBudget
import com.example.data.FamilyMember
import com.example.data.FamilyTransaction
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Dark Credit Card mirroring the Paytin mockup.
 */
@Composable
fun PayFamCreditCard(
    balance: Double,
    cardHolder: String,
    onSetBudgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(16.dp, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(DarkCardGradientStart, DarkCardGradientEnd)
                )
            )
            .drawBehind {
                // Draw abstract card security pattern or grid lines
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f), 0f)
                drawCircle(
                    color = VibrantLime.copy(alpha = 0.05f),
                    radius = size.maxDimension * 0.4f,
                    center = Offset(size.width * 0.8f, size.height * 0.2f)
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.03f),
                    start = Offset(0f, size.height * 0.5f),
                    end = Offset(size.width, size.height * 0.5f),
                    strokeWidth = 2f,
                    pathEffect = pathEffect
                )
            }
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // First Row: Header of Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VISA",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Balance",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                // Modern Pill button "+ Fixer Budget" (Set Budget)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable { onSetBudgetClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Fixer Budget",
                        tint = VibrantLime,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Fixer Budget",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Middle Row: Total Balance value
            Text(
                text = "$ ${String.format("%,.2f", balance)}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp
            )

            // Bottom Row: Holder and Expiration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "**** **** **** 7281",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = cardHolder,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Lime wireless symbol box from mockups
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VibrantLime)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Contactless",
                        tint = DeepSlateBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Custom Radar / Spider Chart built with Jetpack Compose Canvas.
 * It visualizes relative expenses across categories.
 */
@Composable
fun RadarSpiderChart(
    categories: List<String>,
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val maxRadius = size.minDimension / 2.3f

        // Draw concentric webs (3 levels)
        val webLevels = 3
        for (level in 1..webLevels) {
            val radius = maxRadius * (level.toFloat() / webLevels)
            val path = Path()
            for (i in categories.indices) {
                val angle = i * (2 * Math.PI / categories.size) - Math.PI / 2
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.15f),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Draw radial spokes lines
        for (i in categories.indices) {
            val angle = i * (2 * Math.PI / categories.size) - Math.PI / 2
            val x = center.x + maxRadius * cos(angle).toFloat()
            val y = center.y + maxRadius * sin(angle).toFloat()
            drawLine(
                color = Color.White.copy(alpha = 0.1f),
                start = center,
                end = Offset(x, y),
                strokeWidth = 1.5.dp.toPx()
            )
        }

        // Plot the actual category percentages path
        val valuePath = Path()
        val points = mutableListOf<Offset>()
        for (i in categories.indices) {
            val pct = values.getOrElse(i) { 0.5f }.coerceIn(0.1f, 1.0f)
            val radius = maxRadius * pct
            val angle = i * (2 * Math.PI / categories.size) - Math.PI / 2
            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()
            val pt = Offset(x, y)
            points.add(pt)
            if (i == 0) {
                valuePath.moveTo(x, y)
            } else {
                valuePath.lineTo(x, y)
            }
        }
        if (points.isNotEmpty()) {
            valuePath.close()
            // Fill translucent
            drawPath(
                path = valuePath,
                color = VibrantLime.copy(alpha = 0.25f)
            )
            // Draw joint stroke line
            drawPath(
                path = valuePath,
                color = VibrantLime,
                style = Stroke(width = 2.5.dp.toPx())
            )
            // Draw joint circles
            points.forEach { pt ->
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = pt
                )
                drawCircle(
                    color = VibrantLime,
                    radius = 2.dp.toPx(),
                    center = pt
                )
            }
        }
    }
}

/**
 * Custom modern Column stacked chart representing debit (green) and credit card (dark grey)
 * spending per month as requested in the overview sub-section of screen 3.
 */
@Composable
fun StackedBarChart(
    modifier: Modifier = Modifier
) {
    // Elegant sample stats for months
    val months = listOf("JAN", "FEB", "MAR", "APR", "MAI", "JUN")
    val creditData = listOf(100f, 120f, 140f, 220f, 180f, 160f)
    val debitData = listOf(140f, 180f, 240f, 320f, 280f, 340f)
    val maxTotal = 550f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dépenses Mensuelles",
                fontSize = 14.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VibrantLime)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Débit", fontSize = 10.sp, color = TextSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(DeepSlateBlack)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Crédit", fontSize = 10.sp, color = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(modifier = Modifier.weight(1f)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gridLines = 4
                val spacingY = size.height / gridLines
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                // Grid helper lines
                for (i in 0..gridLines) {
                    val y = i * spacingY
                    drawLine(
                        color = Color(0xFFECEFF1),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f,
                        pathEffect = pathEffect
                    )
                }

                // Drawing Stacked Columns
                val colWidth = 24.dp.toPx()
                val colSpacing = (size.width - (months.size * colWidth)) / (months.size + 1)

                for (idx in months.indices) {
                    val crHp = (creditData[idx] / maxTotal) * size.height
                    val dbHp = (debitData[idx] / maxTotal) * size.height

                    val x = colSpacing + idx * (colWidth + colSpacing)

                    // Draw Stacked Credit Bar (dark charcoal bottom seg)
                    drawRoundRect(
                        color = DeepSlateBlack,
                        topLeft = Offset(x, size.height - crHp),
                        size = Size(colWidth, crHp),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )

                    // Draw Stacked Debit Bar (lime top seg)
                    drawRoundRect(
                        color = VibrantLime,
                        topLeft = Offset(x, size.height - crHp - dbHp),
                        size = Size(colWidth, dbHp),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
            }
        }

        // Months Label Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            months.forEach { month ->
                Text(
                    text = month,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Add Transaction sheet dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    membersList: List<FamilyMember>,
    categoriesList: List<com.example.data.FamilyCategory>,
    onDismiss: () -> Unit,
    onSave: (name: String, amount: Double, type: String, category: String, description: String, method: String) -> Unit,
    onAddCategory: (String) -> Unit
) {
    var selectedMember by remember { mutableStateOf(membersList.firstOrNull()?.name ?: "William") }
    var selectedCategory by remember(categoriesList) { mutableStateOf(categoriesList.firstOrNull()?.name ?: "Alimentation") }
    var amountText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf("SPENDING") } // "EARNING" or "SPENDING"
    var paymentMethod by remember { mutableStateOf("Debit Card") } // "Debit Card", "Credit Card", "Cash"

    var showCustomCategoryInput by remember { mutableStateOf(false) }
    var customCategoryName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = SoftGreyCard,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Ajouter une Transaction",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepSlateBlack,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Tab Selector for Earning vs Spending
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFEFEFEF))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (transactionType == "SPENDING") DeepSlateBlack else Color.Transparent)
                        .clickable { transactionType = "SPENDING" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Dépense",
                            color = if (transactionType == "SPENDING") Color.White else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (transactionType == "EARNING") VibrantLime else Color.Transparent)
                            .clickable { transactionType = "EARNING" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Revenu",
                            color = DeepSlateBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Family Member Selector
                Text("Membre de la famille", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFEFEF), RoundedCornerShape(12.dp))
                        .clickable { /* Select first, or cycle */ }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small scrollable row inside dialog for choice
                    membersList.forEach { member ->
                        val isSelected = selectedMember == member.name
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) VibrantLime else Color.White)
                                .clickable { selectedMember = member.name }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${member.avatarEmoji} ${member.name}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }

                // Amount Section
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Montant ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null, tint = DeepSlateBlack) }
                )

                // Description Section
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = DeepSlateBlack) }
                )

                // Category Grid Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Catégorie", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (showCustomCategoryInput) "Annuler" else "+ Personnalisée",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (showCustomCategoryInput) SpendingCoral else VibrantLime,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DeepSlateBlack)
                            .clickable { showCustomCategoryInput = !showCustomCategoryInput }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (showCustomCategoryInput) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customCategoryName,
                            onValueChange = { customCategoryName = it },
                            label = { Text("Nom Catégorie") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        IconButton(
                            onClick = {
                                if (customCategoryName.trim().isNotEmpty()) {
                                    val newCat = customCategoryName.trim()
                                    onAddCategory(newCat)
                                    selectedCategory = newCat
                                    customCategoryName = ""
                                    showCustomCategoryInput = false
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VibrantLime),
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Ajouter Catégorie", tint = DeepSlateBlack)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var expanded by remember { mutableStateOf(false) }
                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentLightGreen, contentColor = DeepSlateBlack),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Catégorie : $selectedCategory  ▼", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(SoftGreyCard)
                        ) {
                            categoriesList.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.name) },
                                    onClick = {
                                        selectedCategory = cat.name
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Payment Method
                Text("Méthode de Paiement", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Debit Card", "Credit Card", "Cash").forEach { method ->
                        val active = paymentMethod == method
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (active) DeepSlateBlack else Color(0xFFEFEFEF))
                                .clickable { paymentMethod = method }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = method,
                                color = if (active) Color.White else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Actions buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E5E5), contentColor = TextPrimary),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Annuler", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val amount = amountText.toDoubleOrNull() ?: 0.0
                            if (amount > 0 && selectedMember.isNotEmpty()) {
                                onSave(selectedMember, amount, transactionType, selectedCategory, descriptionText, paymentMethod)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantLime, contentColor = DeepSlateBlack),
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Sauvegarder", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Floating Edit Budget Dialog.
 */
@Composable
fun SetBudgetDialog(
    categories: List<String>,
    onDismiss: () -> Unit,
    onSave: (category: String, limit: Double) -> Unit
) {
    var selectedCat by remember(categories) { mutableStateOf(categories.firstOrNull() ?: "") }
    var limitText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SoftGreyCard,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Fixer un Budget Limite",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepSlateBlack,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Category selector spinner
                Text("Sélectionnez la Catégorie", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Box {
                    var expanded by remember { mutableStateOf(false) }
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentLightGreen, contentColor = DeepSlateBlack),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("$selectedCat ▼", fontWeight = FontWeight.Bold)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat) }, onClick = {
                                selectedCat = cat
                                expanded = false
                            })
                        }
                    }
                }

                // Amount Textfield
                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Budget Mensuel Limite ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1), contentColor = TextPrimary),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Décliner", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val limit = limitText.toDoubleOrNull() ?: 0.0
                            if (limit > 0) {
                                onSave(selectedCat, limit)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantLime, contentColor = DeepSlateBlack),
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Valider", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
