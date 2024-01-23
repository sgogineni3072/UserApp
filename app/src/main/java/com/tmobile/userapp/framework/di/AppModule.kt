package com.tmobile.userapp.framework.di

import com.tmobile.userapp.framework.db.UserDatabase
import com.tmobile.core.network.UserDataSource
import com.tmobile.core.network.UserHttpClient
import com.tmobile.core.network.UserService
import com.tmobile.core.repositories.api.UserRepository
import com.tmobile.core.repositories.api.UserRepositoryImpl
import com.tmobile.userapp.framework.UIErrorMapper
import com.tmobile.userapp.framework.UIModelMapper
import com.tmobile.userapp.framework.UserViewModel
import com.tmobile.core.utils.NetworkErrorParser
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
        UIModelMapper()
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