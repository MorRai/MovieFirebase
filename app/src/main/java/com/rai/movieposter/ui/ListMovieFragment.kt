package com.rai.movieposter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rai.movieposter.MovieViewModel
import com.rai.movieposter.MovieViewModelFactory
import com.rai.movieposter.R
import com.rai.movieposter.adapters.ListMovieAdapter
import com.rai.movieposter.databinding.FragmetListMovieBinding
import com.rai.movieposter.extension.addCardDecoration


class ListMovieFragment : Fragment() {

    private var _binding: FragmetListMovieBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    var isLinearLayoutManager = true

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory()
    }

    private lateinit var adapter: ListMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmetListMovieBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            floatingActionButton.setOnClickListener {
                val action = ListMovieFragmentDirections.actionListMovieFragmentToAddMovieFragment(
                    getString(R.string.add_fragment_title)
                )
                findNavController().navigate(action)
            }

            adapter = ListMovieAdapter(requireContext()) {
                val action =
                    ListMovieFragmentDirections.actionListMovieFragmentToMovieDetailFragment(it)
                findNavController().navigate(action)
            }

            recyclerView.adapter = adapter

            viewModel.movieData.observe(viewLifecycleOwner) { items ->
                items.let {
                    adapter.submitList(it)
                }
            }
            recyclerView.addCardDecoration(SPACE_SIZE)

            if (isLinearLayoutManager) {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            } else {
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            }

            toolbar.menu.findItem(R.id.action_filters)
            .setOnMenuItemClickListener {
                findNavController().navigate(R.id.action_listMovieFragment_to_filterDialogFragment)
                true
            }

            toolbar.menu.findItem(R.id.action_switch_layout)
            .setOnMenuItemClickListener {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(it)
                true
            }

            toolbar.menu.findItem(R.id.action_sign_out)
                .setOnMenuItemClickListener {
                    signOut()
                    findNavController().popBackStack()
                    true
                }
        }

    }

    private fun signOut() {
        val auth = Firebase.auth
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val SPACE_SIZE = 50
    }

}