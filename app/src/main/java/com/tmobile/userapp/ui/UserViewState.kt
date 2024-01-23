package com.tmobile.userapp.ui

data class UserViewState(
    val shouldShowLoadingMessage: Boolean = false,
    val users: List<UserUIModel> = emptyList()
)