package be.ucll.tasklist

import android.os.Parcelable
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
@DatabaseView
data class DatabaseAssetAndTransactions(
    @Embedded val asset: Database__Asset,
    @Relation(
        parentColumn = "investmentId",
        entityColumn = "investmentId"
    )
    val transactions: List<Database__AssetTransaction>
): Parcelable