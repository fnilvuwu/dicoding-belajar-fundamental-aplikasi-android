package com.example.bfaa.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bfaa.api.RetrofitClient
import com.example.bfaa.data.model.User
import com.example.bfaa.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String) {
        val apiInstance = RetrofitClient.apiInstance
        apiInstance?.getSearchUser(query)?.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Failure", t.message ?: "Unknown error occurred.")
            }
        })
    }

    fun getSearchUser(): LiveData<ArrayList<User>> {
        return listUsers
    }
}
