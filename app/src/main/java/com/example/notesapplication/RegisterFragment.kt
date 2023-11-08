package com.example.notesapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.notesapplication.databinding.FragmentRegisterBinding
import com.example.notesapplication.models.UserRequest
import com.example.notesapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            val validateResult = validateUserInput()
            if (validateResult.second) {
                authViewModel.registerUser(
                    getUserRequest()
                )
            } else {
                binding.txtError.text = validateResult.first
            }
        }
        binding.btnLogin.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        bindObservers()

    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val username = binding.txtUsername.text.toString()
        return UserRequest(emailAddress, password, username)
    }

    private fun validateUserInput(): Pair<String, Boolean> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            false
        )
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressbar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    // TODO token work left
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressbar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}