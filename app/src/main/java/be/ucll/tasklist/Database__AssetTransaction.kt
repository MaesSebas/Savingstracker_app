package be.ucll.tasklist

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "investmenttransaction_table")
data class Database__AssetTransaction(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0L,
    val userID: Long,
    val accountID: Long,
    val investmentId: Long,
    val transactionDate: String,
    val transactionType: String,
    var quantity: Int,
    var value: Double?,
): Parcelable