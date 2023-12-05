package be.ucll.tasklist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class Database__User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0L,
    val userName: String,
    val password: String,
)
