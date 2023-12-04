package be.ucll.tasklist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_table")
data class Database__C_Account(
    @PrimaryKey(autoGenerate = true)
    val accountID: Long = 0L,
    val userID: Long,
    val accountName: String,
    val accountNumber: String,
    val totalBalance: String,
    val accountType: String
)