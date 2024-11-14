/*
 *
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.rbrthmn.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import br.com.rbrthmn.data.local.database.FinancialCompanion
import br.com.rbrthmn.data.local.database.FinancialCompanionDao

/**
 * Unit tests for [DefaultFinancialCompanionRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultFinancialCompanionRepositoryTest {

    @Test
    fun financialCompanions_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultFinancialCompanionRepository(FakeFinancialCompanionDao())

        repository.add("Repository")

        assertEquals(repository.financialCompanions.first().size, 1)
    }

}

private class FakeFinancialCompanionDao : FinancialCompanionDao {

    private val data = mutableListOf<FinancialCompanion>()

    override fun getFinancialCompanions(): Flow<List<FinancialCompanion>> = flow {
        emit(data)
    }

    override suspend fun insertFinancialCompanion(item: FinancialCompanion) {
        data.add(0, item)
    }
}
