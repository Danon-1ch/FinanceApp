package com.example.financeapp

import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionViewModelTest {
    @Test
    fun expenseSubtractsAndResetsDraft() {
        val viewModel = TransactionViewModel()
        viewModel.onEvent(
            MainUiEvent.AddTransaction(
                Transaction(amount = 250, type = TransactionType.EXPENSE, description = "Кофе"),
            ),
        )

        assertEquals(-250, viewModel.uiState.balance)
        assertEquals(1, viewModel.uiState.transactionCount)
        assertEquals("", viewModel.uiState.amount)
        assertEquals("", viewModel.uiState.description)
    }

    @Test
    fun zeroAndOverflowProduceDifferentErrors() {
        val viewModel = TransactionViewModel()
        viewModel.onEvent(MainUiEvent.DescriptionChanged("Кофе"))
        viewModel.onEvent(MainUiEvent.AmountChanged("0"))
        viewModel.addTransaction()
        assertEquals(TransactionError.INVALID_INPUT, viewModel.uiState.error)

        viewModel.onEvent(MainUiEvent.AmountChanged("2147483648"))
        viewModel.addTransaction()
        assertEquals(TransactionError.AMOUNT_TOO_LARGE, viewModel.uiState.error)
        assertEquals(0, viewModel.uiState.balance)
    }
}
