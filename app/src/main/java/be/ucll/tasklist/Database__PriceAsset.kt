package be.ucll.tasklist

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "investment_priceAsset_table")
data class Database__PriceAsset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val investmentId: Long,
    val accountID: Long,
    val dateOfPrice: String,
    val openPrice: String,
    val closePrice: String,
    val highPrice: String,
    val lowPrice: String,
    val volume: String
): Parcelable