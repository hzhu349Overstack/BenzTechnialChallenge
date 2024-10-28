package com.tps.challenge.features.userProfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tps.challenge.data.model.UserProfile
import com.tps.challenge.domain.UserProfileUseCase
import com.tps.challenge.features.model.ApiState
import kotlinx.coroutines.launch

class UserProfileViewModel(private val useCase: UserProfileUseCase) : ViewModel() {
    private val _userProfileLiveData: MutableLiveData<ApiState<UserProfile>> by lazy {
        MutableLiveData<ApiState<UserProfile>>(ApiState.Empty)
    }
    val userProfileLiveData: LiveData<ApiState<UserProfile>> = _userProfileLiveData
    fun getUserProfile(id: Int) {
        viewModelScope.launch {
            _userProfileLiveData.value = ApiState.Loading
            try {
                useCase.invoke(id.toString())?.let {
                    _userProfileLiveData.value = ApiState.Success(it)
                } ?: run {
                    _userProfileLiveData.value = ApiState.Error("User not found")
                }
                Log.d(TAG, "getUserProfile: ${_userProfileLiveData.value}")
            } catch (e: Exception) {
                Log.e(TAG, "getUserProfile: ${e.message}")
                _userProfileLiveData.value = ApiState.Error(e.message.toString())

            }
        }

    }
    companion object {
        const val TAG = "UserProfileViewModel"
    }

}