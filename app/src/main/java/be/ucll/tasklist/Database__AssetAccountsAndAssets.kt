package be.ucll.tasklist

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation

@DatabaseView
data class Database__AssetAccountsAndAssets(
    @Embedded val account: Database__Account,
    @Relation(
        parentColumn = "accountID",
        entityColumn = "accountID",
    )
    val assets: List<Database__Asset>
)
