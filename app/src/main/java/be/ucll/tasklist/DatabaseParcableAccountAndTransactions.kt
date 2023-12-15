package be.ucll.tasklist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatabaseParcableAccountAndTransactions(
    val data: Database__AccountsAndTransactions
): Parcelable