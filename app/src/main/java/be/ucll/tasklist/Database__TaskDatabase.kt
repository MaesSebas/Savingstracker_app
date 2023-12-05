package be.ucll.tasklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Database__Asset::class, Database__User::class, Database__Transaction::class, Database__Account::class, Database__AssetTransaction::class], version = 1, exportSchema = false)
abstract class Database__TaskDatabase : RoomDatabase() {
    abstract val databaseTaskDao: Database__TaskDao

    companion object {
        @Volatile
        private var INSTANCE: Database__TaskDatabase? = null

        fun getInstance(context: Context): Database__TaskDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Database__TaskDatabase::class.java,
                        "tasks_database"
                    ).addCallback(TaskDatabaseCallback()).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private class TaskDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.databaseTaskDao)
                    }
                }
            }
        }

        private suspend fun populateDatabase(databaseConnection: Database__TaskDao) {
            insertUserMockData(databaseConnection)
            insertCardAccountMockData(databaseConnection)
            insertCardTransactionMockData(databaseConnection)
            insertSavingsAccountMockData(databaseConnection)
            insertSavingsTransactionMockData(databaseConnection)
            insertExtraLegalAccountMockData(databaseConnection)
            insertExtraLegalTransactionMockData(databaseConnection)
            insertAssetAccountMockData(databaseConnection)
            insertAssetsAndTransactionsMockData(databaseConnection)

        }

        private suspend fun insertAssetAccountMockData(databaseConnection: Database__TaskDao) {
            val databasAssetAccountMockdataGeneratorClass = Database__cards_mockdata()
            val assetAccountMockData = databasAssetAccountMockdataGeneratorClass.generateMockAssetAccount()
            for(account in assetAccountMockData) {
                databaseConnection.insert(account)
            }
        }

        private suspend fun insertCardAccountMockData(databaseConnection: Database__TaskDao) {
            val databasAccountMockdataGeneratorClass = Database__cards_mockdata()
            val accountMockData = databasAccountMockdataGeneratorClass.generateMockCheckingsAccounts()
            for(account in accountMockData) {
                databaseConnection.insert(account)
            }
        }

        private suspend fun insertAssetsAndTransactionsMockData(databaseConnection: Database__TaskDao) {
            val databaseAssetMockGeneratorClass = Database__cards_mockdata()
            val assetData = databaseAssetMockGeneratorClass.generateAssetMockDataAssetAccounts()
            for (asset in assetData) {
                databaseConnection.insert(asset)
            }
            val transactionData = databaseAssetMockGeneratorClass.generateMockTransactionData(assetData)
            for (transaction in transactionData) {
                databaseConnection.insert(transaction)
            }
        }

        private suspend fun insertCardTransactionMockData(databaseConnection: Database__TaskDao) {
            val databaseTransactionMockGeneratorClass = Database__cards_mockdata()
            val transactionData = databaseTransactionMockGeneratorClass.generateTransactionsMockDataCheckingsAccounts()
            for (transaction in transactionData) {
                databaseConnection.insert(transaction)
            }
        }

        private suspend fun insertSavingsAccountMockData(databaseConnection: Database__TaskDao) {
            val databaseSavingsAccountMockGeneratorClass = Database__cards_mockdata()
            val savingsAccoontData = databaseSavingsAccountMockGeneratorClass.generateMockSavingsAccounts()
            for (account in savingsAccoontData) {
                databaseConnection.insert(account)
            }
        }

        private suspend fun insertSavingsTransactionMockData(databaseConnection: Database__TaskDao) {
            val databaseSavingsTransactionMockGeneratorClass = Database__cards_mockdata()
            val savingsTransactionData = databaseSavingsTransactionMockGeneratorClass.generateTransactionsMockDataSavingsAccounts()
            for (transaction in savingsTransactionData) {
                databaseConnection.insert(transaction)
            }
        }

        private suspend fun insertExtraLegalAccountMockData(databaseConnection: Database__TaskDao) {
            val databaseSavingsAccountMockGeneratorClass = Database__cards_mockdata()
            val savingsAccoontData = databaseSavingsAccountMockGeneratorClass.generateMockExtraLegalAccounts()
            for (account in savingsAccoontData) {
                databaseConnection.insert(account)
            }
        }

        private suspend fun insertExtraLegalTransactionMockData(databaseConnection: Database__TaskDao) {
            val databaseSavingsTransactionMockGeneratorClass = Database__cards_mockdata()
            val savingsTransactionData = databaseSavingsTransactionMockGeneratorClass.generateTransactionsMockDataExtraLegalAccounts()
            for (transaction in savingsTransactionData) {
                databaseConnection.insert(transaction)
            }
        }

        private suspend fun insertUserMockData(databaseConnection: Database__TaskDao) {
            databaseConnection.insert(Database__User(0, "Sebastiaan", "1234"))
        }
    }
}