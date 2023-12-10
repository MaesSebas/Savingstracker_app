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
    val accountName: String,
    val accountNumber: String,
    val totalBalance: String,
    val accountType: String
): Parcelable