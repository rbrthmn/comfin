package br.com.rbrthmn.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.rbrthmn.data.db.ComFinDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseSeedTest {

    private lateinit var db: ComFinDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ComFinDatabase::class.java
        ).build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun seed_insertsExpectedRowCounts() = runTest {
        DatabaseSeeder.seed(db)

        assertEquals(3,  db.bankAccountDao().getAll().first().size)
        assertEquals(3,  db.creditCardDao().getAll().first().size)
        assertEquals(3,  db.reserveDao().getAll().first().size)
        assertEquals(11, db.investmentDao().getAll().first().size)
        assertEquals(11, db.recurringExpenseDao().getAll().first().size)
        assertEquals(6,  db.allocationDao().getAll().first().size)
        assertEquals(15, db.transactionDao().getAll().first().size)
    }

    @Test
    fun seed_bankAccounts_mainAccountExists() = runTest {
        DatabaseSeeder.seed(db)

        val accounts = db.bankAccountDao().getAll().first()
        assertTrue(accounts.any { it.isMainAccount })
        assertTrue(accounts.any { it.bankName == "Nubank" })
    }

    @Test
    fun seed_reserves_emergencialHasTransactions() = runTest {
        DatabaseSeeder.seed(db)

        val reserves = db.reserveDao().getAll().first()
        val emergencial = reserves.first { it.name == "Reserva emergencial" }
        val txs = db.reserveTransactionDao().getByReserve(emergencial.id).first()

        assertEquals(10, txs.size)
        assertTrue(emergencial.currentTotal > 0)
    }

    @Test
    fun seed_transactions_linkedToAccount() = runTest {
        DatabaseSeeder.seed(db)

        val txs = db.transactionDao().getAll().first()
        assertTrue(txs.all { it.bankAccountId != null })
    }

    @Test
    fun seed_investments_coverAllTypes() = runTest {
        DatabaseSeeder.seed(db)

        val investments = db.investmentDao().getAll().first()
        val types = investments.map { it.type }.toSet()

        assertTrue(types.contains("Renda Fixa"))
        assertTrue(types.contains("FII"))
        assertTrue(types.contains("Ações"))
        assertTrue(types.contains("Criptomoeda"))
    }
}
