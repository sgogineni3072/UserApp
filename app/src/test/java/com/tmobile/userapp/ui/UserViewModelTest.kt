package com.tmobile.userapp.ui

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tmobile.userapp.db.UserDao
import com.tmobile.userapp.db.UserEntity
import com.tmobile.core.data.models.Support
import com.tmobile.core.data.models.User
import com.tmobile.core.data.models.UserResponse
import com.tmobile.userapp.network.NetworkResponse
import com.tmobile.userapp.repositories.api.UserRepository
import com.tmobile.userapp.tests.TestCoroutineRule
import com.tmobile.userapp.tests.ViewModelFlowCollector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    @get:Rule
    val rule = TestCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var mockUIModelMapper: UIModelMapper
    @Mock
    private lateinit var mockUIErrorMapper: UIErrorMapper
    @Mock
    private lateinit var mockUserDao: UserDao

    private val testDispatcher = Dispatchers.Unconfined
    private lateinit var collector: ViewModelFlowCollector<UserViewState, UserEvent>
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userViewModel = UserViewModel(userRepository, testDispatcher, mockUIModelMapper, mockUIErrorMapper, mockUserDao)
        collector = ViewModelFlowCollector(userViewModel.viewState, userViewModel.events, TestCoroutineDispatcher())
    }

    @Test
    fun `WHEN getUsers from viewmodel is called THEN getUsers() from repository should be called`() {
        runBlocking {
            // given
            whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())

            // when
            userViewModel.getUsers()

            // then
            verify(userRepository).getUsers()
        }
    }

    @Test
    fun `WHEN getUsers from viewmodel is called THEN shouldShowLoadingMessage state is set to true`() = collector.test { states, _ ->
        //given
        whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())
        // when
        userViewModel.getUsers()

        // then
        Assert.assertTrue(states.last().shouldShowLoadingMessage)
    }

    @Test
    fun `GIVEN successful network response WHEN getUsers is called THEN emit ShowUsers event`() = collector.test { states, _ ->
        //given
        whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())
        whenever(userRepository.getUsers()).thenReturn(NetworkResponse.Success(generateUserResponses()))
        whenever(mockUIModelMapper.mapNetworkResponseToUIModel(generateUserResponses())).thenReturn(
            generateUserUIModelsData()
        )

        // when
        userViewModel.getUsers()

        // then
        Assert.assertEquals(states.last().users, generateUserUIModelsData())
    }

    @Test
    fun `GIVEN successful network response WHEN getUsers() is called THEN shouldShowLoadingMessage state is set to false`() = collector.test { states, _ ->
        // given
        whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())
        whenever(userRepository.getUsers()).thenReturn(NetworkResponse.Success(generateUserResponses()))
        whenever(mockUIModelMapper.mapNetworkResponseToUIModel(generateUserResponses())).thenReturn(
            generateUserUIModelsData()
        )

        // when
        userViewModel.getUsers()

        // then
        Assert.assertFalse(states.last().shouldShowLoadingMessage)
    }

    @Test
    fun `GIVEN failed network response with error code -1 WHEN getUsers() is called THEN emit ShowError event with network error message`() = collector.test { _, events ->
        // given
        whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())
        whenever(userRepository.getUsers()).thenReturn(NetworkResponse.Error(ERROR_CODE_MINUS_ONE))
        whenever(mockUIErrorMapper.mapErrorCodeToMessage(ERROR_CODE_MINUS_ONE)).thenReturn(NETWORK_ERROR_MESSAGE)

        // when
        userViewModel.getUsers()

        // then
        val expectedEvents = listOf(UserEvent.ShowError(NETWORK_ERROR_MESSAGE))
        kotlin.test.assertEquals(expectedEvents, events)
    }

    @Test
    fun `GIVEN failed network response with error code -2 WHEN getUsers is called THEN emit ShowError event with unknown error message`() = collector.test { _, events ->
        // given
        whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())
        whenever(userRepository.getUsers()).thenReturn(NetworkResponse.Error(ERROR_CODE_MINUS_TWO))
        whenever(mockUIErrorMapper.mapErrorCodeToMessage(ERROR_CODE_MINUS_TWO)).thenReturn(UNKOWN_ERROR_MESSAGE)

        // when
        userViewModel.getUsers()

        // then
        val expectedEvents = listOf(UserEvent.ShowError(UNKOWN_ERROR_MESSAGE))
        kotlin.test.assertEquals(expectedEvents, events)
    }

    @Test
    fun `GIVEN failed network response WHEN getUsers() is called THEN shouldShowLoadingMessage state is set to false`() = collector.test { states, _ ->
        // given
        whenever(mockUserDao.getAllUserEntities()).thenReturn(generateUserEntities())
        whenever(userRepository.getUsers()).thenReturn(NetworkResponse.Error(ERROR_CODE_MINUS_ONE))
        whenever(mockUIErrorMapper.mapErrorCodeToMessage(ERROR_CODE_MINUS_ONE)).thenReturn(NETWORK_ERROR_MESSAGE)

        // when
        userViewModel.getUsers()

        // then
        Assert.assertFalse(states.last().shouldShowLoadingMessage)
    }

    private fun generateUserEntities() =
        mutableListOf(
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com"),
            UserEntity(1, "James", "Foster", "james@gmail.com")
        )

    private fun generateUserResponses() = UserResponse(
        1,
        10,
        12,
        2,
        mutableListOf(
            User(1, "James", "Foster", "james@gmail.com", ""),
            User(1, "James", "Foster", "james@gmail.com", ""),
            User(1, "James", "Foster", "james@gmail.com", ""),
            User(1, "James", "Foster", "james@gmail.com", "")
        ),
        Support("", "")
    )

    private fun generateUserUIModelsData() =
        arrayListOf(
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
            UserUIModel("James", "Foster", "james@gmail.com", ""),
        )

    companion object {
        //Error codes & message
        const val ERROR_CODE_MINUS_ONE = -1
        const val NETWORK_ERROR_MESSAGE = "Network error"
        const val ERROR_CODE_MINUS_TWO = -2
        const val UNKOWN_ERROR_MESSAGE = "Unknown error"
    }

}