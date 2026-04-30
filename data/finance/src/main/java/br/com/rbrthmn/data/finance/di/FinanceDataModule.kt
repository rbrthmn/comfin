package br.com.rbrthmn.data.finance.di

import br.com.rbrthmn.data.finance.repository.BankAccountRepository
import br.com.rbrthmn.data.finance.repository.CreditCardRepository
import br.com.rbrthmn.data.finance.repository.TransactionRepository
import br.com.rbrthmn.data.finance.repository.impl.BankAccountRepositoryImpl
import br.com.rbrthmn.data.finance.repository.impl.CreditCardRepositoryImpl
import br.com.rbrthmn.data.finance.repository.impl.TransactionRepositoryImpl
import org.koin.dsl.module

val financeDataModule = module {
    single<BankAccountRepository> { BankAccountRepositoryImpl(get()) }
    single<CreditCardRepository> { CreditCardRepositoryImpl(get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
}
