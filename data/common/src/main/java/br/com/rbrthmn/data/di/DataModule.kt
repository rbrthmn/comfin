package br.com.rbrthmn.data.di

import androidx.room.Room
import br.com.rbrthmn.data.BuildConfig
import br.com.rbrthmn.data.DatabaseSeeder
import br.com.rbrthmn.data.db.ComFinDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ComFinDatabase::class.java,
            "comfin.db"
        ).build().also { db ->
            if (BuildConfig.DEBUG) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (db.bankAccountDao().getAll().first().isEmpty()) {
                        DatabaseSeeder.seed(db)
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
