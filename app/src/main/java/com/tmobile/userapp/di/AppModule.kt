package com.tmobile.userapp.di

import com.tmobile.userapp.db.UserDao
import com.tmobile.userapp.db.UserDatabase
import com.tmobile.userapp.network.UserDataSource
import com.tmobile.userapp.network.UserHttpClient
import com.tmobile.userapp.network.UserService
import com.tmobile.userapp.repositories.api.UserRepository
import com.tmobile.userapp.repositories.api.UserRepositoryImpl
import com.tmobile.userapp.ui.UIErrorMapper
import com.tmobile.userapp.ui.UIModelMapper
import com.tmobile.userapp.ui.UserViewModel
import com.tmobile.userapp.utils.NetworkErrorParser
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val USER_URL = "https://reqres.in/api/users?per_page=10"
const val IO = "IO"

val appModule = module() {

    //Data
    single {
        UserHttpClient()
    }

    single {
        UserHttpClient().getClient()
    }

    single {
        NetworkErrorParser()
    }

    single {
        UserService(
            httpClient = get(),
            endpoint = USER_URL
        )
    }

    single {
        UserDataSource(
            userService = get(),
            networkErrorParser = get()
        )
    }

    //Domain
    single<UserRepository> {
        UserRepositoryImpl(
            userDataSource = get()
        )
    }

    // UI
    factory(named(IO)) {
        Dispatchers.IO
    }

    single {
        UIErrorMapper(
            resources = androidContext().resources
        )
    }

    single {
        UIModelMapper(
            resources = androidContext().resources
        )
    }

    viewModel {
        UserViewModel(
            repository = get(),
            dispatcher = get(named(IO)),
            uiModelMapper = get(),
            uiErrorMapper = get(),
            database = UserDatabase.getInstance(androidContext()).userDao()
        )
    }
}