package com.example.notesapplication

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapplication.models.User
import com.example.notesapplication.models.UserRequest
import com.example.notesapplication.models.UserResponse
import com.example.notesapplication.repository.UserRepository
import com.example.notesapplication.utils.NetworkResult
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>> get() = userRepository.userResponseLiveData
    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest )
        }
    }

    fun validateCredentials(username : String, emailAddress : String, password: String, isLogin : Boolean) : Pair<String, Boolean>{
        var result = Pair("", true)
        if((!isLogin && TextUtils.isEmpty(username)) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair("Please provide the credentials", false)
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair("Please provide valid email",false)
        }else if(password.length <=5){
            result = Pair("Password length should be greater than 5", false)
        }
        return result
    }
}