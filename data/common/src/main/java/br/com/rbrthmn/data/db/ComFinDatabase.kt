package br.com.rbrthmn.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.rbrthmn.data.converter.DatabaseConverters
import br.com.rbrthmn.data.dao.budget.AllocationDao
import br.com.rbrthmn.data.dao.budget.RecurringExpenseDao
import br.com.rbrthmn.data.dao.finance.BankAccountDao
import br.com.rbrthmn.data.dao.finance.CreditCardDao
import br.com.rbrthmn.data.dao.finance.TransactionDao
import br.com.rbrthmn.data.dao.wealth.InvestmentDao
import br.com.rbrthmn.data.dao.wealth.ReserveDao
import br.com.rbrthmn.data.dao.wealth.ReserveTransactionDao
import br.com.rbrthmn.data.entity.budget.AllocationEntity
import br.com.rbrthmn.data.entity.budget.RecurringExpenseEntity
import br.com.rbrthmn.data.entity.finance.BankAccountEntity
import br.com.rbrthmn.data.entity.finance.CreditCardEntity
import br.com.rbrthmn.data.entity.finance.TransactionEntity
import br.com.rbrthmn.data.entity.wealth.InvestmentEntity
import br.com.rbrthmn.data.entity.wealth.ReserveEntity
import br.com.rbrthmn.data.entity.wealth.ReserveTransactionEntity

@Database(
    entities = [
        // Finance
        TransactionEntity::class,
        BankAccountEntity::class,
        CreditCardEntity::class,
        // Wealth
        ReserveEntity::class,
        ReserveTransactionEntity::class,
        InvestmentEntity::class,
        // Budget
        AllocationEntity::class,
        RecurringExpenseEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DatabaseConverters::class)
abstract class ComFinDatabase : RoomDatabase() {
    // Finance
    abstract fun transactionDao(): TransactionDao
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun creditCardDao(): CreditCardDao
    // Wealth
    abstract fun reserveDao(): ReserveDao
    abstract fun reserveTransactionDao(): ReserveTransactionDao
    abstract fun investmentDao(): InvestmentDao
    // Budget
    abstract fun allocationDao(): AllocationDao
    abstract fun recurringExpenseDao(): RecurringExpenseDao
}
