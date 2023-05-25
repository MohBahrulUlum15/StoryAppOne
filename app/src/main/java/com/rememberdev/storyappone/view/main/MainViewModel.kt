package com.rememberdev.storyappone.view.main

import android.util.Log
import androidx.lifecycle.*
import com.rememberdev.storyappone.data.api.ApiConfig
import com.rememberdev.storyappone.model.ListStoryItem
import com.rememberdev.storyappone.model.StoriesResponse
import com.rememberdev.storyappone.model.UserModel
import com.rememberdev.storyappone.model.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel(private val preferences: UserPreferences) : ViewModel() {
    private val _photoList = MutableLiveData<List<ListStoryItem>>()
    val photoList: LiveData<List<ListStoryItem>> = _photoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoriesList(token: String) {
        _isLoading.value = true
        val bearerToken = "Bearer $token"
        val client = ApiConfig.getApiService().getStories(bearerToken)
        client.enqueue(object : retrofit2.Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _photoList.value = response.body()?.listStory as List<ListStoryItem>
                    Log.d("MainViewModel", _photoList.value.toString())
                } else {
                    Log.e("MainViewModel", "response onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return preferences.getUser().asLiveData()
    }

    fun logoutUser() {
        viewModelScope.launch { preferences.logout() }
    }
}