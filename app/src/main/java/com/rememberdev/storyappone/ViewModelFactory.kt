package com.rememberdev.storyappone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rememberdev.storyappone.model.UserPreferences
import com.rememberdev.storyappone.view.auth.LoginViewModel
import com.rememberdev.storyappone.view.main.MainViewModel

class ViewModelFactory(private val preferences: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(preferences) as T
            }
//            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
//                RegisterViewModel(pref) as T
//            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}