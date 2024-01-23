package com.tmobile.userapp.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tmobile.userapp.repositories.api.UserRepository

class ViewModelFactory(private val api: UserRepository, private val database: UserDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserRepository::class.java, UserDao::class.java)
            .newInstance(api, database)
    }
}