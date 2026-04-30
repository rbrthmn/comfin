package br.com.rbrthmn.data.misc.di

import br.com.rbrthmn.data.misc.local.LocalMiscDataSource
import br.com.rbrthmn.data.misc.repository.MiscRepository
import org.koin.dsl.module

val miscDataModule = module {
    single<MiscRepository> {
        LocalMiscDataSource(
            reserveDao = get(),
            reserveTransactionDao = get(),
            investmentDao = get(),
            allocationDao = get(),
            recurringExpenseDao = get()
        )
    }
}
