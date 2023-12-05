package be.ucll.tasklist

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation

@DatabaseView
data class Database__AssetAndTransactions(
    @Embedded val asset: Database__Asset,
    @Relation(
        parentColumn = "investmentId",
        entityColumn = "investmentId"
    )
    val transactions: List<Database__AssetTransaction>
)