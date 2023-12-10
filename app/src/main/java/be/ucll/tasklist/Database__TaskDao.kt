package be.ucll.tasklist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface Database__TaskDao {

    // *-----------  Mockdata Inserts -----------------------------*

    //User_table

    @Insert
    suspend fun insert(user: Database__User)

    @Query("SELECT password FROM user_table WHERE userName = 'Sebastiaan'")
    fun getPassword(): String

    //account_table

    @Insert
    suspend fun insert(account: Database__Account)

    //transaction accounts

    @Insert
    suspend fun insert(transaction:  Database__Transaction)

    //asset_table

    @Insert
    suspend fun insert(transaction:  Database__Asset)

    //asset transactions
    @Insert
    suspend fun insert(transaction: Database__AssetTransaction)

    // *-----------  get data -----------------------------*


    @Transaction
    @Query("SELECT * FROM account_table WHERE accountType = :accountType")
    fun getAccountsWithTransactions(accountType: String): List<Database__AccountsAndTransactions>

    @Transaction
    @Query("SELECT * FROM account_table WHERE accountType = 'AssetAccount'")
    fun getAssetAccountWithTransaction(): List<Database__AssetAccountsAndAssets>

    @Transaction
    @Query("SELECT * FROM investment_database_table")
    fun getAssetsWithTransactions(): List<Database__AssetAndTransactions>

    @Query("SELECT * FROM investment_database_table WHERE investmentId = :investmentId")
    fun getAllAssetTransactionsById(investmentId: Int): List<Database__AssetAndTransactions>

    @Query("SELECT * FROM investment_database_table WHERE investmentType = :typeInvestment")
    fun getAllAssetsAndTransactionsByType(typeInvestment: String): List<Database__AssetAndTransactions>


    @Query("SELECT * FROM transaction_table WHERE accountID = :accountId")
    fun getTransactionsFromAccountById(accountId: Int): List<Database__Transaction>

}