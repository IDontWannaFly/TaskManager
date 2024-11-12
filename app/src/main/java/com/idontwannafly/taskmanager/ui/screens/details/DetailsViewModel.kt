package com.idontwannafly.taskmanager.ui.screens.details

import androidx.lifecycle.viewModelScope
import com.idontwannafly.taskmanager.ui.base.BaseViewModel
import com.idontwannafly.taskmanager.app.extensions.collect
import com.idontwannafly.taskmanager.features.details.DetailsUseCase
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
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
            is DetailsContract.Event.NavigateToList -> setEffect { DetailsContract.Effect.Navigation.ToList }
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

    private fun setDetails(details: TaskDetails) {
        setState { copy(details = details) }
    }

    private fun setLoading(value: Boolean) {
        setState { copy(isLoading = value) }
    }

}