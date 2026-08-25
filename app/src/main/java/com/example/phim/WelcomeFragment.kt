package com.example.phim

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // for the fade in animation
        val welcomeText = view.findViewById<TextView>(R.id.welcomeText)
        val navArrowDown = view.findViewById<ImageButton>(R.id.downArrow)

        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        welcomeText.startAnimation(fadeIn)
        navArrowDown.startAnimation(fadeIn)

        //Navigation Down
        navArrowDown.setOnClickListener {
                findNavController().navigate(
                R.id.action_welcomeFragment_to_HomeFragment)
        }
    }
}