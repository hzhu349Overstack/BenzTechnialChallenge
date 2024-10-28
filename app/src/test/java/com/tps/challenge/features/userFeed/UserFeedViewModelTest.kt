package com.tps.challenge.features.userFeed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tps.challenge.data.model.User
import com.tps.challenge.domain.FetchAllUserUseCase
import com.tps.challenge.features.model.ApiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UserFeedViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: UserFeedViewModel
    private val testDispatcher = StandardTestDispatcher()


    private val useCase: FetchAllUserUseCase = mockk(relaxed = true)


    @Mock
    private lateinit var observer: Observer<ApiState<List<User>>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = UserFeedViewModel(useCase)
        viewModel.allUserLiveData.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.allUserLiveData.removeObserver(observer)
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAllUser should set loading and success state when users are fetched successfully`() = runTest {
        val userList = listOf(
            User(
                avatar_url = "https://example.com/avatar1.jpg",
                events_url = "https://example.com/events1",
                followers_url = "https://example.com/followers1",
                following_url = "https://example.com/following1",
                gists_url = "https://example.com/gists1",
                gravatar_id = "",
                html_url = "https://example.com/user1",
                id = 1,
                login = "JohnDoe",
                node_id = "node1",
                organizations_url = "https://example.com/orgs1",
                received_events_url = "https://example.com/received1",
                repos_url = "https://example.com/repos1",
                site_admin = false,
                starred_url = "https://example.com/starred1",
                subscriptions_url = "https://example.com/subscriptions1",
                type = "User",
                url = "https://example.com/user1",
                user_view_type = "public"
            ),
            User(
                avatar_url = "https://example.com/avatar2.jpg",
                events_url = "https://example.com/events2",
                followers_url = "https://example.com/followers2",
                following_url = "https://example.com/following2",
                gists_url = "https://example.com/gists2",
                gravatar_id = "",
                html_url = "https://example.com/user2",
                id = 2,
                login = "JaneDoe",
                node_id = "node2",
                organizations_url = "https://example.com/orgs2",
                received_events_url = "https://example.com/received2",
                repos_url = "https://example.com/repos2",
                site_admin = false,
                starred_url = "https://example.com/starred2",
                subscriptions_url = "https://example.com/subscriptions2",
                type = "User",
                url = "https://example.com/user2",
                user_view_type = "public"
            )
        )
        coEvery { useCase.invoke() } returns userList

        viewModel.fetchAllUser()
        advanceUntilIdle()

        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(ApiState.Empty)
        inOrder.verify(observer).onChanged(ApiState.Loading)
        inOrder.verify(observer).onChanged(ApiState.Success(userList))

        assertEquals(ApiState.Success(userList), viewModel.allUserLiveData.value)
    }


    @Test
    fun `fetchAllUser should set loading and error state when no users are found`() = runTest {
        coEvery { useCase.invoke() } returns emptyList()

        viewModel.fetchAllUser()

        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(ApiState.Loading) // Verify Loading state
        inOrder.verify(observer).onChanged(ApiState.Error("No users found")) // Verify Error state (if expected)

        assertTrue(viewModel.allUserLiveData.value is ApiState.Error)
    }


    @Test
    fun `fetchAllUser should set loading and error state when exception is thrown`() = runTest {
        val exception = RuntimeException("No users found")
        coEvery {(useCase.invoke()) } throws exception

        viewModel.fetchAllUser()

        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(ApiState.Empty)
        inOrder.verify(observer).onChanged(ApiState.Loading)

        assertTrue(viewModel.allUserLiveData.value is ApiState.Error)
        assertEquals("No users found", (viewModel.allUserLiveData.value as ApiState.Error).message)
    }
}
