package com.rememberdev.storyappone.view.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rememberdev.storyappone.data.api.ApiConfig
import com.rememberdev.storyappone.model.RegisterResponse
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel() : ViewModel() {

    companion object{
        private const val TAG = "RegisterViewModel"
    }

    private val _successRegister = MutableLiveData<Boolean>()
    val successRegister: LiveData<Boolean> = _successRegister

    fun userRegister(name:String, email:String, password:String){
        val client = ApiConfig.getApiService().getUsers(name, email, password)
        client.enqueue(object : retrofit2.Callback<RegisterResponse>{
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _successRegister.value = false
            }

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    val dataUser = response.body()
                    if (dataUser != null){
                        Log.d(TAG, "onResponse Success: ${dataUser.message}")
                        _successRegister.value = true
                    } else{
                        Log.e(TAG, "Cannot create User: ${dataUser?.message}")
                        _successRegister.value = false
                    }
                }
            }
        })
    }

//    fun getSuccessRegister(): Boolean {
//        return successRegister
//    }
}