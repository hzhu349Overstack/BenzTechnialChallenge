package com.tps.challenge.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


inline fun <reified T : ViewModel> createGenericViewModelFactory(
    crossinline initializer: () -> T
): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            if (modelClass.isAssignableFrom(T::class.java)) {
                return initializer() as VM
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}