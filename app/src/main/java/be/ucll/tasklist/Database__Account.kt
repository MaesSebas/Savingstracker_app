package be.ucll.tasklist

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "account_table")
data class Database__Account(
    @PrimaryKey(autoGenerate = true)
    val accountID: Long = 0L,
    val userID: Long,
    var accountName: String,
    var accountNumber: String,
    var totalBalance: String,
    var accountType: String,
    var assetCategoryValues: String = "",
): Parcelable