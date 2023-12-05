package be.ucll.tasklist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TaskDao {

    // *-----------  Mockdata Inserts -----------------------------*

    //User_table

    @Insert
    suspend fun insert(user: Database__C_User)

    @Query("SELECT password FROM user_table WHERE userName = 'Sebastiaan'")
    fun getPassword(): String

    //account_table

    @Insert
    suspend fun insert(account: Database__C_Account)

    //transaction accounts

    @Insert
    suspend fun insert(transaction:  Database__C_Transaction)

    //asset_table

    @Insert
    suspend fun insert(transaction:  Database__C_Asset)

    //asset transactions
    @Insert
    suspend fun insert(transaction: Database__C_AssetTransaction)

    // *-----------  get data -----------------------------*


    @Transaction
    @Query("SELECT * FROM account_table WHERE accountType = :accountType")
    fun getAccountsWithTransactions(accountType: String): List<Database__C_AccountsAndTransactions>

    @Transaction
    @Query("SELECT * FROM account_table WHERE accountType = 'AssetAccount'")
    fun getAssetAccountWithTransaction(): List<Database__C_AssetAccountsAndAssets>

    @Transaction
    @Query("SELECT * FROM investment_database_table")
    fun getAssetsWithTransactions(): List<Database__C_AssetAndTransactions>


    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    fun get(taskId: Long): LiveData<Task>

    @Query("SELECT * FROM task_table ORDER BY taskId DESC")
    fun getAll(): LiveData<List<Task>>
}