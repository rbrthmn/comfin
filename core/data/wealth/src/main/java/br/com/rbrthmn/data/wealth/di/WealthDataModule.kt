package br.com.rbrthmn.data.wealth.di

import br.com.rbrthmn.data.wealth.repository.InvestmentRepository
import br.com.rbrthmn.data.wealth.repository.ReserveRepository
import br.com.rbrthmn.data.wealth.repository.impl.InvestmentRepositoryImpl
import br.com.rbrthmn.data.wealth.repository.impl.ReserveRepositoryImpl
import org.koin.dsl.module

val wealthDataModule = module {
    single<ReserveRepository> { ReserveRepositoryImpl(get(), get()) }
    single<InvestmentRepository> { InvestmentRepositoryImpl(get()) }
}
