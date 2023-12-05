package be.ucll.tasklist

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class Database__AccountsAndTransactions(
    @Embedded val account: Database__Account,
    @Relation(
        parentColumn = "accountID",
        entityColumn = "accountID"
    )
    val transactions: List<Database__Transaction>
)