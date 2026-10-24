package com.example.financeapp.legacy

internal data class MainUiState(
    val balance: Int = 0,
    val counter: Int = 0,
)

internal enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
}

internal data class Transaction(
    var type: TransactionType,
    var amount: Int,
)

internal sealed interface MainUiEvent {
    data class AddTransaction(val transaction: Transaction) : MainUiEvent
}

internal fun reduce(state: MainUiState, event: MainUiEvent): MainUiState = when (event) {
    is MainUiEvent.AddTransaction -> {
        val changeBalance = when (event.transaction.type) {
            TransactionType.DEPOSIT -> {
                state.balance + event.transaction.amount
            }

            TransactionType.WITHDRAWAL -> {
                state.balance - event.transaction.amount
            }
        }
        state.copy(counter = state.counter + 1, balance = changeBalance)
    }
}
