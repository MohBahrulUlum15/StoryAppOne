package com.rememberdev.storyappone.view.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rememberdev.storyappone.data.api.ApiConfig
import com.rememberdev.storyappone.model.LoginResponse
import com.rememberdev.storyappone.model.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(private val preferences: UserPreferences): ViewModel() {

    companion object{
        private const val TAG = "LoginViewModel"
    }

    private val _successLogin = MutableLiveData<Boolean>()
    val successLogin: LiveData<Boolean> = _successLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun userLogin(email: String, password: String){
        _isLoading.value = true
        val client =ApiConfig.getApiService().login(email, password)
        client.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse != null){
                        Log.d(TAG, "Hasil onResponse: Tidak Error")
                        _successLogin.value = true
                        val id = loginResponse.loginResult?.userId.toString()
                        val token = loginResponse.loginResult?.token.toString()
                        login(id, token)
                    }
                    else{
                        Log.e(TAG, "ERROR LOGIN USER: ${loginResponse?.message}")
                    }
                }
                else if(response.code() == 401){
                    _successLogin.value = false
                }
                _isLoading.value = false
            }
        })
    }

    fun login(id: String, token: String) {
        viewModelScope.launch {
            preferences.login(id,token)
        }
    }
}