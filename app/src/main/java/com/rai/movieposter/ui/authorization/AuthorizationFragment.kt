package com.rai.movieposter.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rai.movieposter.R
import com.rai.movieposter.data.LceState
import com.rai.movieposter.databinding.FragmetAuthorizationBinding
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment() {

    private var _binding: FragmetAuthorizationBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmetAuthorizationBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        registerObserver()
        listenToChannels()

        with(binding) {
            emailSignInButton.setOnClickListener {
                progressBar.isVisible = true
                val email = binding.fieldEmail.text.toString()
                val password = binding.fieldPassword.text.toString()
                viewModel.signInUser(email, password)
            }
            emailCreateAccountButton.setOnClickListener {
                progressBar.isVisible = true
                val email = binding.fieldEmail.text.toString()
                val password = binding.fieldPassword.text.toString()
                viewModel.signUpUser(email, password)
            }
        }
    }

    private fun registerObserver() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                findNavController().navigate(R.id.action_аuthorizationFragment_to_listMovieFragment)
                Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allEventsFlow.collect { event ->
                when (event) {
                    is LceState.Message -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }
                    is LceState.Error -> {
                        Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                        binding.progressBar.isInvisible = true
                    }
                }
            }
        }
    }

    private fun getUser() {
        viewModel.getCurrentUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}