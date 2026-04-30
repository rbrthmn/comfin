package br.com.rbrthmn.data.operations.di

import br.com.rbrthmn.data.operations.local.LocalOperationsDataSource
import br.com.rbrthmn.data.operations.repository.OperationsRepository
import org.koin.dsl.module

val operationsDataModule = module {
    single<OperationsRepository> {
        LocalOperationsDataSource(
            transactionDao = get(),
            bankAccountDao = get(),
            creditCardDao = get(),
            reserveDao = get()
        )
    }
}
