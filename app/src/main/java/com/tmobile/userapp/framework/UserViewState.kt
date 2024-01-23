package com.tmobile.userapp.framework

data class UserViewState(
    val shouldShowLoadingMessage: Boolean = false,
    val users: List<UserUIModel> = emptyList()
)