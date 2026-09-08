package com.example.phim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.phim.repository.AuthRepository

class RegisterFragment :
    Fragment(
        R.layout.fragment_register
    ) {

    private val repository =
        AuthRepository()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        val usernameInput =
            view.findViewById<
                    EditText
                    >(
                R.id.usernameInput
            )

        val emailInput =
            view.findViewById<
                    EditText
                    >(
                R.id.registerEmailInput
            )

        val passwordInput =
            view.findViewById<
                    EditText
                    >(
                R.id.registerPasswordInput
            )

        val createButton =
            view.findViewById<
                    Button
                    >(
                R.id.createAccountButton
            )

        val backButton =
            view.findViewById<
                    Button
                    >(
                R.id.backToLoginButton
            )

        val errorText =
            view.findViewById<
                    TextView
                    >(
                R.id.registerErrorText
            )

        createButton
            .setOnClickListener {

                val username =
                    usernameInput
                        .text
                        .toString()
                        .trim()

                val email =
                    emailInput
                        .text
                        .toString()
                        .trim()

                val password =
                    passwordInput
                        .text
                        .toString()

                if (
                    username.isBlank() ||
                    email.isBlank() ||
                    password.length < 6
                ) {

                    errorText.text =
                        "Enter a username, email, and password of at least 6 characters."

                    return@setOnClickListener
                }

                repository.register(
                    username,
                    email,
                    password
                ) {
                        success,
                        message ->

                    if (
                        success
                    ) {

                        findNavController()
                            .navigate(
                                R.id.action_registerFragment_to_homeFragment
                            )

                    } else {

                        errorText.text =
                            message
                                ?: "Unable to create account."
                    }
                }
            }

        backButton
            .setOnClickListener {

                findNavController()
                    .popBackStack()
            }
    }
}