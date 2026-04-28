package br.com.rbrthmn.data.di

import androidx.room.Room
import br.com.rbrthmn.data.db.ComFinDatabase
import br.com.rbrthmn.data.repository.AllocationRepository
import br.com.rbrthmn.data.repository.BankAccountRepository
import br.com.rbrthmn.data.repository.CreditCardRepository
import br.com.rbrthmn.data.repository.InvestmentRepository
import br.com.rbrthmn.data.repository.RecurringExpenseRepository
import br.com.rbrthmn.data.repository.ReserveRepository
import br.com.rbrthmn.data.repository.TransactionRepository
import br.com.rbrthmn.data.repository.impl.AllocationRepositoryImpl
import br.com.rbrthmn.data.repository.impl.BankAccountRepositoryImpl
import br.com.rbrthmn.data.repository.impl.CreditCardRepositoryImpl
import br.com.rbrthmn.data.repository.impl.InvestmentRepositoryImpl
import br.com.rbrthmn.data.repository.impl.RecurringExpenseRepositoryImpl
import br.com.rbrthmn.data.repository.impl.ReserveRepositoryImpl
import br.com.rbrthmn.data.repository.impl.TransactionRepositoryImpl
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

    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    single<ReserveRepository> { ReserveRepositoryImpl(get(), get()) }
    single<RecurringExpenseRepository> { RecurringExpenseRepositoryImpl(get()) }
    single<AllocationRepository> { AllocationRepositoryImpl(get()) }
    single<BankAccountRepository> { BankAccountRepositoryImpl(get()) }
    single<CreditCardRepository> { CreditCardRepositoryImpl(get()) }
    single<InvestmentRepository> { InvestmentRepositoryImpl(get()) }
}
