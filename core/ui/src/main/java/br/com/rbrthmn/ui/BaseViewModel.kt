package br.com.rbrthmn.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<U, I> : ViewModel() {
    abstract val uiState: StateFlow<U>
    abstract fun onIntent(intent: I)
    abstract fun doOnInit(): BaseViewModel<U, I>
}
