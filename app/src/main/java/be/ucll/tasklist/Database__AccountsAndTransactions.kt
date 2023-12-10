package be.ucll.tasklist

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Entity
@Parcelize
data class Database__AccountsAndTransactions(
    @Embedded val account: Database__Account,
    @Relation(
        parentColumn = "accountID",
        entityColumn = "accountID"
    )
    val transactions: List<Database__Transaction>
): Parcelable, Serializable