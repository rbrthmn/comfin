package br.com.rbrthmn.data.di

import androidx.room.Room
import br.com.rbrthmn.data.db.ComFinDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ComFinDatabase::class.java,
            "comfin.db"
        ).build()
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
