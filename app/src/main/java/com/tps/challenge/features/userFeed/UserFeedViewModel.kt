package com.tps.challenge.features.userFeed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tps.challenge.data.model.User
import com.tps.challenge.domain.FetchAllUserUseCase
import com.tps.challenge.features.model.ApiState
import kotlinx.coroutines.launch

class UserFeedViewModel(private val useCase: FetchAllUserUseCase) : ViewModel() {
    private val _allUserLiveData: MutableLiveData<ApiState<List<User>>> by lazy {
        MutableLiveData<ApiState<List<User>>>(ApiState.Empty)
    }
    val allUserLiveData: LiveData<ApiState<List<User>>> = _allUserLiveData

    init {
        fetchAllUser()
    }

    fun fetchAllUser() {
        viewModelScope.launch {
            _allUserLiveData.value = ApiState.Loading

            try {
                val users = useCase.invoke()
                if (users.isNotEmpty()) {
                    _allUserLiveData.value = ApiState.Success(users)
                    Log.d("UserFeedViewModel", "Users fetched successfully: $users")
                } else {
                    _allUserLiveData.value = ApiState.Error("No users found")
                    Log.d("UserFeedViewModel", "No users found")
                }
            } catch (e: Exception) {
                Log.e("UserFeedViewModel", "Error fetching users: ${e.message}")
                _allUserLiveData.value = ApiState.Error(e.message ?: "Not Configured")
            }
        }
    }

    companion object {
    }
}