package com.tmobile.userapp.framework

sealed class UserEvent {
    data class ShowError(val errorMessage: String) : UserEvent()
}