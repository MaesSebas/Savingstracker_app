package be.ucll.tasklist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatabaseParcableAssetAndTransactions(
    val data: List<DatabaseAssetAndTransactions>
): Parcelable