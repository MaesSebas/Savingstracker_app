package be.ucll.tasklist

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@DatabaseView
data class Database__C_AssetAndTransactions(
    @Embedded val asset: Database__C_Asset,
    @Relation(
        parentColumn = "investmentId",
        entityColumn = "investmentId"
    )
    val transactions: List<Database__C_AssetTransaction>
)