package br.com.rbrthmn.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.rbrthmn.data.converter.DatabaseConverters
import br.com.rbrthmn.data.dao.AllocationDao
import br.com.rbrthmn.data.dao.BankAccountDao
import br.com.rbrthmn.data.dao.CreditCardDao
import br.com.rbrthmn.data.dao.InvestmentDao
import br.com.rbrthmn.data.dao.RecurringExpenseDao
import br.com.rbrthmn.data.dao.ReserveDao
import br.com.rbrthmn.data.dao.ReserveTransactionDao
import br.com.rbrthmn.data.dao.TransactionDao
import br.com.rbrthmn.data.entity.AllocationEntity
import br.com.rbrthmn.data.entity.BankAccountEntity
import br.com.rbrthmn.data.entity.CreditCardEntity
import br.com.rbrthmn.data.entity.InvestmentEntity
import br.com.rbrthmn.data.entity.RecurringExpenseEntity
import br.com.rbrthmn.data.entity.ReserveEntity
import br.com.rbrthmn.data.entity.ReserveTransactionEntity
import br.com.rbrthmn.data.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        ReserveEntity::class,
        ReserveTransactionEntity::class,
        RecurringExpenseEntity::class,
        AllocationEntity::class,
        BankAccountEntity::class,
        CreditCardEntity::class,
        InvestmentEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DatabaseConverters::class)
abstract class ComFinDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun reserveDao(): ReserveDao
    abstract fun reserveTransactionDao(): ReserveTransactionDao
    abstract fun recurringExpenseDao(): RecurringExpenseDao
    abstract fun allocationDao(): AllocationDao
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun creditCardDao(): CreditCardDao
    abstract fun investmentDao(): InvestmentDao
}
