package com.idontwannafly.taskmanager.ui.screens.details

import androidx.lifecycle.viewModelScope
import com.idontwannafly.taskmanager.ui.base.BaseViewModel
import com.idontwannafly.taskmanager.app.extensions.collect
import com.idontwannafly.taskmanager.features.details.DetailsUseCase
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val useCase: DetailsUseCase
) : BaseViewModel<DetailsContract.State, DetailsContract.Event, DetailsContract.Effect>() {

    private val currentDetails: TaskDetails
        get() = viewState.value.details

    init {
        collectDetails()
    }

    override fun provideInitialState(): DetailsContract.State = DetailsContract.State(
        TaskDetails.empty(),
        false
    )

    override suspend fun handleEvent(event: DetailsContract.Event) {
        when (event) {
            is DetailsContract.Event.UpdateDetails -> updateDetails(event.details)
        }
    }

    private fun collectDetails() {
        useCase.detailsFlow.collect(viewModelScope) {
            setDetails(it)
            setLoading(false)
        }
    }

    private fun updateDetails(details: TaskDetails) {
        if (details == currentDetails) return
        viewModelScope.launch {
            setLoading(true)
            useCase.updateDetails(details)
        }
    }

    private suspend fun setDetails(details: TaskDetails) {
        val state = viewState.value.copy(details = details)
        postState(state)
    }

    private suspend fun setLoading(value: Boolean) {
        val state = viewState.value.copy(isLoading = value)
        postState(state)
    }

}