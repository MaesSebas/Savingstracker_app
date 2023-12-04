package be.ucll.tasklist

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@DatabaseView
data class Database__C_AssetAccountsAndAssets(
    @Embedded val account: Database__C_Account,
    @Relation(
        parentColumn = "accountID",
        entityColumn = "accountID",
        //entity = Database__C_AssetAndTransactions::class
    )
    val transactions: List<Database__C_Asset>
    //val assets: List<Database__C_AssetAndTransactions>
)