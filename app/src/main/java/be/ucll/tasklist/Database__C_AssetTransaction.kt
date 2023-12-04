package be.ucll.tasklist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investmenttransaction_table")
data class Database__C_AssetTransaction(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0L,
    val investmentId: Long,
    val transactionDate: String,
    val transactionType: String,
    val quantity: Int,
    val value: Double?,
)