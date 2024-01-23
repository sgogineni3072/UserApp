package com.tmobile.userapp.ui

sealed class UserEvent {
    data class ShowError(val errorMessage: String) : UserEvent()
}