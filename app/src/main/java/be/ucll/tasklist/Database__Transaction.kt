package be.ucll.tasklist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Database__Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionID: Long = 0L,
    //val investmentId: Long,
    val userID: Long,
    val accountID: Long,
    val companyName: String,
    val description: String,
    val transactionDate: String,
    val category: String,
    val amount: Double,
    val type: String
)