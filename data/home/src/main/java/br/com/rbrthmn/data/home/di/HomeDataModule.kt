package br.com.rbrthmn.data.home.di

import br.com.rbrthmn.data.home.local.LocalHomeDataSource
import br.com.rbrthmn.data.home.repository.HomeRepository
import org.koin.dsl.module

val homeDataModule = module {
    single<HomeRepository> {
        LocalHomeDataSource(
            bankAccountDao = get(),
            creditCardDao = get(),
            transactionDao = get()
        )
    }
}
