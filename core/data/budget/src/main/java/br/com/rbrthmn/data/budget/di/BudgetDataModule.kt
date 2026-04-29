package br.com.rbrthmn.data.budget.di

import br.com.rbrthmn.data.budget.repository.AllocationRepository
import br.com.rbrthmn.data.budget.repository.RecurringExpenseRepository
import br.com.rbrthmn.data.budget.repository.impl.AllocationRepositoryImpl
import br.com.rbrthmn.data.budget.repository.impl.RecurringExpenseRepositoryImpl
import org.koin.dsl.module

val budgetDataModule = module {
    single<AllocationRepository> { AllocationRepositoryImpl(get()) }
    single<RecurringExpenseRepository> { RecurringExpenseRepositoryImpl(get()) }
}
