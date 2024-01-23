package com.tmobile.userapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmobile.userapp.db.UserDao
import com.tmobile.userapp.db.UserEntity
import com.tmobile.userapp.db.mapToEntity
import com.tmobile.userapp.db.mapToUiModelList
import com.tmobile.userapp.network.NetworkResponse
import com.tmobile.userapp.repositories.api.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
    private val uiModelMapper: UIModelMapper,
    private val uiErrorMapper: UIErrorMapper,
    private val database: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(UserViewState())
    private val _events = MutableSharedFlow<UserEvent>()

    val viewState: Flow<UserViewState> = _state
    val events: Flow<UserEvent> = _events

    fun getUsers(scope: CoroutineScope = viewModelScope) {
        if(_state.value.users.isNotEmpty()) {
            _state.value = _state.value.copy(users = _state.value.users)
        } else {
            showLoadingState()
            scope.launch {
                callGetUsers()
            }
        }
    }

    private suspend fun callGetUsers(scope: CoroutineScope = viewModelScope) {
        scope.launch {
            withContext(dispatcher) {
                if (database.getAllUserEntities().isNotEmpty()) {
                    _state.value = _state.value.copy(users = database.getAllUserEntities().mapToUiModelList())
                }
                repository.getUsers()
            }.apply {
                when (this) {
                    is NetworkResponse.Success -> {
                        for (user in this.data.data) {
                            database.insertUserEntity(user.mapToEntity())
                        }
                        _state.value = _state.value.copy(users = uiModelMapper.mapNetworkResponseToUIModel(this.data))
                        hideLoadingState()
                    }
                    is NetworkResponse.Error -> {
                        _events.emit(UserEvent.ShowError(uiErrorMapper.mapErrorCodeToMessage(this.code)))
                        hideLoadingState()
                    }
                }
            }
        }
    }

    private fun showLoadingState() {
        _state.value = _state.value.copy(shouldShowLoadingMessage = true)
    }

    private fun hideLoadingState() {
        _state.value = _state.value.copy(shouldShowLoadingMessage = false)
    }
}