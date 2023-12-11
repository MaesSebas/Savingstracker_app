package be.ucll.tasklist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatabaseTestParcable(
    val testOne: String,
    val testTwo: String
): Parcelable