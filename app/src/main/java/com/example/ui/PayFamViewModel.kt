package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PayFamViewModel(application: Application) : AndroidViewModel(application) {

    private val database: FamilyExpenseDatabase by lazy {
        Room.databaseBuilder(
            application,
            FamilyExpenseDatabase::class.java,
            "family_expense_v1_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    private val repository: FamilyExpenseRepository by lazy {
        FamilyExpenseRepository(database.dao())
    }

    // Exposed Flows
    val members: StateFlow<List<FamilyMember>> = repository.members
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<FamilyTransaction>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val budgets: StateFlow<List<CategoryBudget>> = repository.budgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<FamilyCategory>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state states
    var selectedMemberFilter = MutableStateFlow<String?>(null)

    init {
        // Pre-populate data if database is empty on launch
        viewModelScope.launch {
            repository.members.first().let { currentMembers ->
                if (currentMembers.isEmpty()) {
                    populateDefaultData()
                }
            }
            repository.categories.first().let { currentCategories ->
                if (currentCategories.isEmpty()) {
                    populateDefaultCategories()
                }
            }
        }
    }

    private suspend fun populateDefaultCategories() {
        val defaultCategories = listOf(
            FamilyCategory("Alimentation", false),
            FamilyCategory("Logement", false),
            FamilyCategory("Factures", false),
            FamilyCategory("Loisirs", false),
            FamilyCategory("Santé", false),
            FamilyCategory("Transport", false),
            FamilyCategory("Revenus", false),
            FamilyCategory("Autre", false)
        )
        for (category in defaultCategories) {
            repository.addCategory(category)
        }
    }

    private suspend fun populateDefaultData() {
        // Default family members with distinct avatars and color schemes
        val defaultMembers = listOf(
            FamilyMember(name = "William", avatarEmoji = "👨", colorHex = "#60A5FA"),
            FamilyMember(name = "Marie", avatarEmoji = "👩", colorHex = "#F472B6"),
            FamilyMember(name = "Azie", avatarEmoji = "👧", colorHex = "#FBBF24"),
            FamilyMember(name = "Chaoir", avatarEmoji = "👦", colorHex = "#34D399"),
            FamilyMember(name = "Fandit", avatarEmoji = "🦁", colorHex = "#A78BFA"),
            FamilyMember(name = "Nayu", avatarEmoji = "🦊", colorHex = "#F87171")
        )
        for (member in defaultMembers) {
            repository.addMember(member)
        }

        // Default transactions showing realistic cash inflows and outflows
        val baseTime = System.currentTimeMillis()
        val HOUR = 3600000L
        val DAY = 86400000L

        val defaultTransactions = listOf(
            FamilyTransaction(
                memberName = "William",
                amount = 25453.00,
                type = "EARNING",
                category = "Revenus",
                description = "Salaire William",
                timestamp = baseTime - 4 * DAY,
                paymentMethod = "Debit Card"
            ),
            FamilyTransaction(
                memberName = "Marie",
                amount = 3250.80,
                type = "EARNING",
                category = "Revenus",
                description = "Freelance Design",
                timestamp = baseTime - 2 * DAY,
                paymentMethod = "Credit Card"
            ),
            FamilyTransaction(
                memberName = "Food Store",
                amount = 15.00,
                type = "SPENDING",
                category = "Alimentation",
                description = "Courses Express",
                timestamp = baseTime - 3 * HOUR,
                paymentMethod = "Debit Card"
            ),
            FamilyTransaction(
                memberName = "House Rent",
                amount = 290.00,
                type = "SPENDING",
                category = "Logement",
                description = "Contribution Loyer",
                timestamp = baseTime - 12 * HOUR,
                paymentMethod = "Debit Card"
            ),
            FamilyTransaction(
                memberName = "Marie",
                amount = 27.00,
                type = "SPENDING",
                category = "Loisirs",
                description = "Cinéma Week-end",
                timestamp = baseTime - 1 * DAY,
                paymentMethod = "Credit Card"
            ),
            FamilyTransaction(
                memberName = "Azie",
                amount = 45.50,
                type = "SPENDING",
                category = "Factures",
                description = "Abonnement Internet & Mobile",
                timestamp = baseTime - 3 * DAY,
                paymentMethod = "Debit Card"
            ),
            FamilyTransaction(
                memberName = "William",
                amount = 120.00,
                type = "SPENDING",
                category = "Santé",
                description = "Consultation Dentiste",
                timestamp = baseTime - 5 * DAY,
                paymentMethod = "Debit Card"
            )
        )
        for (tx in defaultTransactions) {
            repository.addTransaction(tx)
        }

        // Default category limits
        val defaultBudgets = listOf(
            CategoryBudget("Alimentation", 800.00),
            CategoryBudget("Logement", 1200.00),
            CategoryBudget("Loisirs", 400.00),
            CategoryBudget("Factures", 350.00),
            CategoryBudget("Santé", 300.00),
            CategoryBudget("Transport", 250.00)
        )
        for (budget in defaultBudgets) {
            repository.updateBudget(budget)
        }
    }

    // Business actions
    fun addTransaction(
        name: String,
        amount: Double,
        type: String,
        category: String,
        description: String,
        method: String
    ) {
        viewModelScope.launch {
            val newTx = FamilyTransaction(
                memberName = name,
                amount = amount,
                type = type,
                category = category,
                description = description,
                timestamp = System.currentTimeMillis(),
                paymentMethod = method
            )
            repository.addTransaction(newTx)
        }
    }

    fun deleteTransaction(tx: FamilyTransaction) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
        }
    }

    fun setBudgetLimit(category: String, limit: Double) {
        viewModelScope.launch {
            repository.updateBudget(CategoryBudget(category, limit))
        }
    }

    fun addFamilyMember(name: String, emoji: String, colorHex: String) {
        viewModelScope.launch {
            repository.addMember(FamilyMember(name = name, avatarEmoji = emoji, colorHex = colorHex))
        }
    }

    fun addCategory(name: String, isCustom: Boolean = true) {
        viewModelScope.launch {
            repository.addCategory(FamilyCategory(name = name, isCustom = isCustom))
        }
    }

    fun deleteCategory(name: String) {
        viewModelScope.launch {
            repository.deleteCategory(FamilyCategory(name = name))
        }
    }

    // Helper formatting and calculations for active screens
    fun formatEpoch(time: Long): String {
        val sdf = SimpleDateFormat("EEEE, dd MMMM", Locale.FRENCH)
        return sdf.format(Date(time))
    }
}
