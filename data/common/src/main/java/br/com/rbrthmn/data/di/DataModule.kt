package br.com.rbrthmn.data.di

import android.content.Context
import androidx.room.Room
import br.com.rbrthmn.data.BuildConfig
import br.com.rbrthmn.data.DatabaseSeeder
import br.com.rbrthmn.data.SeederVersion
import br.com.rbrthmn.data.db.ComFinDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import androidx.core.content.edit

private const val PREFS_DEBUG = "debug_prefs"
private const val KEY_SEEDER_VERSION = "seeder_version"

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ComFinDatabase::class.java,
            "comfin.db"
        ).build().also { db ->
            if (BuildConfig.DEBUG) {
                val prefs = androidContext().getSharedPreferences(PREFS_DEBUG, Context.MODE_PRIVATE)
                val storedVersion = prefs.getInt(KEY_SEEDER_VERSION, 0)
                if (storedVersion < SeederVersion.CURRENT) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.clearAllTables()
                        DatabaseSeeder.seed(db)
                        prefs.edit { putInt(KEY_SEEDER_VERSION, SeederVersion.CURRENT) }
                    }
                }
            }
        }
    }

    single { get<ComFinDatabase>().transactionDao() }
    single { get<ComFinDatabase>().reserveDao() }
    single { get<ComFinDatabase>().reserveTransactionDao() }
    single { get<ComFinDatabase>().recurringExpenseDao() }
    single { get<ComFinDatabase>().allocationDao() }
    single { get<ComFinDatabase>().bankAccountDao() }
    single { get<ComFinDatabase>().creditCardDao() }
    single { get<ComFinDatabase>().investmentDao() }
}
