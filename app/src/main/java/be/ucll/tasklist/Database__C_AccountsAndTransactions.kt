package be.ucll.tasklist

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class Database__C_AccountsAndTransactions(
    @Embedded val account: Database__C_Account,
    @Relation(
        parentColumn = "accountID",
        entityColumn = "accountID"
    )
    val transactions: List<Database__C_Transaction>
)