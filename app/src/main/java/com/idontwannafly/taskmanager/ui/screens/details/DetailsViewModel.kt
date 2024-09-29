package com.idontwannafly.taskmanager.ui.screens.details

import androidx.lifecycle.viewModelScope
import com.idontwannafly.taskmanager.BaseViewModel
import com.idontwannafly.taskmanager.features.details.DetailsUseCase
import com.idontwannafly.taskmanager.features.details.dto.TaskDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val useCase: DetailsUseCase
) : BaseViewModel() {

    private val _detailsFlow = MutableStateFlow(TaskDetails.empty())
    val detailsFlow = _detailsFlow.asStateFlow()

    init {
        initDetails()
    }

    private fun initDetails() = viewModelScope.launch {
        val details = useCase.getDetails()
        _detailsFlow.emit(details)
    }

    fun updateDetails(details: TaskDetails) {
        if (details == _detailsFlow.value) return
        viewModelScope.launch {
            useCase.updateDetails(details)
        }
    }

}