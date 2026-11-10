package com.example.financeapp

import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionViewModelTest {
    @Test
    fun expenseSubtractsAndResetsDraft() {
        val state = reduce(
            MainUiState(),
            MainUiEvent.AddTransaction(
                Transaction(amount = 250, type = TransactionType.EXPENSE, description = "Кофе"),
            ),
        )

        assertEquals(-250, state.balance)
        assertEquals(1, state.transactionCount)
        assertEquals("", state.amount)
        assertEquals("", state.description)
    }

    @Test
    fun incomeAddsToBalance() {
        val state = reduce(
            MainUiState(),
            MainUiEvent.AddTransaction(
                Transaction(amount = 500, type = TransactionType.INCOME, description = "Зарплата"),
            ),
        )

        assertEquals(500, state.balance)
        assertEquals(1, state.transactionCount)
    }

    @Test
    fun replaysHistoryIntoTheSameBalance() {
        val state = listOf(
            Transaction(amount = 500, type = TransactionType.INCOME, description = "Зарплата"),
            Transaction(amount = 120, type = TransactionType.EXPENSE, description = "Кофе"),
        ).fold(MainUiState()) { current, transaction ->
            reduce(current, MainUiEvent.AddTransaction(transaction))
        }

        assertEquals(380, state.balance)
        assertEquals(2, state.transactionCount)
    }

    @Test
    fun balanceOverflowKeepsTheState() {
        val state = reduce(
            MainUiState(balance = Int.MAX_VALUE),
            MainUiEvent.AddTransaction(
                Transaction(amount = 1, type = TransactionType.INCOME, description = "Кофе"),
            ),
        )

        assertEquals(TransactionError.AMOUNT_TOO_LARGE, state.error)
        assertEquals(Int.MAX_VALUE, state.balance)
    }

    @Test
    fun nonDigitAmountIsIgnored() {
        val state = reduce(MainUiState(), MainUiEvent.AmountChanged("12a"))

        assertEquals("", state.amount)
    }
}
