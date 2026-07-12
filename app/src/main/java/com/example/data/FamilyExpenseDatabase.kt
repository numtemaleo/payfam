package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val avatarEmoji: String, // "👨", "👩", "👧", "👦", "🦁", "🦊" etc.
    val colorHex: String     // hex color for branding avatars
)

@Entity(tableName = "transactions")
data class FamilyTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberName: String,
    val amount: Double,
    val type: String,        // "EARNING" or "SPENDING"
    val category: String,    // "Alimentation", "Logement", "Factures", "Loisirs", "Santé", "Transport", "Revenus", "Autre"
    val description: String,
    val timestamp: Long,
    val paymentMethod: String // "Debit Card", "Credit Card", "Cash"
)

@Entity(tableName = "category_budgets")
data class CategoryBudget(
    @PrimaryKey val category: String,
    val limitAmount: Double
)

@Entity(tableName = "family_categories")
data class FamilyCategory(
    @PrimaryKey val name: String,
    val isCustom: Boolean = false
)

@Dao
interface FamilyExpenseDao {
    @Query("SELECT * FROM family_members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<FamilyMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMember)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<FamilyTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: FamilyTransaction)

    @Delete
    suspend fun deleteTransaction(transaction: FamilyTransaction)

    @Query("SELECT * FROM category_budgets")
    fun getAllBudgets(): Flow<List<CategoryBudget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: CategoryBudget)

    @Query("SELECT * FROM family_categories ORDER BY isCustom ASC, name ASC")
    fun getAllCategories(): Flow<List<FamilyCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: FamilyCategory)

    @Delete
    suspend fun deleteCategory(category: FamilyCategory)
}

@Database(
    entities = [FamilyMember::class, FamilyTransaction::class, CategoryBudget::class, FamilyCategory::class],
    version = 2,
    exportSchema = false
)
abstract class FamilyExpenseDatabase : RoomDatabase() {
    abstract fun dao(): FamilyExpenseDao
}

class FamilyExpenseRepository(private val dao: FamilyExpenseDao) {
    val members: Flow<List<FamilyMember>> = dao.getAllMembers()
    val transactions: Flow<List<FamilyTransaction>> = dao.getAllTransactions()
    val budgets: Flow<List<CategoryBudget>> = dao.getAllBudgets()
    val categories: Flow<List<FamilyCategory>> = dao.getAllCategories()

    suspend fun addMember(member: FamilyMember) = dao.insertMember(member)
    suspend fun addTransaction(transaction: FamilyTransaction) = dao.insertTransaction(transaction)
    suspend fun deleteTransaction(transaction: FamilyTransaction) = dao.deleteTransaction(transaction)
    suspend fun updateBudget(budget: CategoryBudget) = dao.insertBudget(budget)
    suspend fun addCategory(category: FamilyCategory) = dao.insertCategory(category)
    suspend fun deleteCategory(category: FamilyCategory) = dao.deleteCategory(category)
}
