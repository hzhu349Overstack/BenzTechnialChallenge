package com.tps.challenge.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tps.challenge.application.TCApplication
import com.tps.challenge.data.model.User
import com.tps.challenge.domain.FetchAllUserUseCase
import kotlinx.coroutines.launch

class UserFeedViewModel(private val useCase: FetchAllUserUseCase) : ViewModel() {
    private val _allUserLiveData: MutableLiveData<ApiState<List<User>>> by lazy {
        MutableLiveData<ApiState<List<User>>>(ApiState.Empty)
    }
    val allUserLiveData: LiveData<ApiState<List<User>>> = _allUserLiveData

    fun fetchAllUser() {
        viewModelScope.launch {
            _allUserLiveData.value = ApiState.Loading

            try {
                val users = useCase.invoke()
                if (users.isNotEmpty()) {
                    _allUserLiveData.value = ApiState.Success(users)
                } else {
                    _allUserLiveData.value = ApiState.Error("No users found")
                }
            } catch (e: Exception) {
                _allUserLiveData.value = ApiState.Error(e.message ?: "Not Configured")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TCApplication
                val userCase = application.getAppComponent().useCase
                UserFeedViewModel(
                    userCase
                )
            }
        }
    }
}