package com.rai.movieposter.ui.listMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rai.movieposter.R
import com.rai.movieposter.adapters.ListMovieAdapter
import com.rai.movieposter.data.Constants
import com.rai.movieposter.data.Filters
import com.rai.movieposter.data.LceState
import com.rai.movieposter.data.Response
import com.rai.movieposter.databinding.FragmetListMovieBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListMovieFragment : Fragment() {

    private var _binding: FragmetListMovieBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private var layoutManager: GridLayoutManager? = null
    private lateinit var adapter: ListMovieAdapter

    private val viewModel by viewModel<ListMovieViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(Constants.REQUEST_KEY) { _, bundle ->
            viewModel.onFilterChanged(bundle.getSerializable(Constants.EXTRA_KEY) as Filters)
        }
    }

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
        listenToChannels()

        with(binding) {
            floatingActionButton.setOnClickListener {
                val action = ListMovieFragmentDirections.actionListMovieFragmentToAddMovieFragment(
                    getString(R.string.add_fragment_title))
                findNavController().navigate(action)
            }

            layoutManager = GridLayoutManager(requireContext(), 1)
            recyclerView.layoutManager = layoutManager

            adapter = ListMovieAdapter(requireContext(), layoutManager) {
                val action =
                    ListMovieFragmentDirections.actionListMovieFragmentToMovieDetailFragment(it.nameMovie
                        ?: "")
                //либо кидать экземплр класс
                findNavController().navigate(action)
            }

            recyclerView.adapter = adapter

            viewModel.movieDataFlow.onEach { response ->
                when (response) {
                    is Response.Success -> {
                        adapter.submitList(response.data)
                        binding.paginationProgressBar.isInvisible = true
                    }
                    is Response.Error -> {
                        binding.paginationProgressBar.isInvisible = true
                        Toast.makeText(requireContext(),
                            response.message, Toast.LENGTH_SHORT).show()
                    }
                    is Response.Loading -> {
                        binding.paginationProgressBar.isVisible = true
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_filters -> {
                        findNavController().navigate(ListMovieFragmentDirections.actionListMovieFragmentToFilterDialogFragment(
                            viewModel.getFilter()))
                    }
                    R.id.action_switch_layout -> {
                        chooseLayout()
                        setIcon(it)
                    }
                    R.id.action_sign_out -> {
                        viewModel.signOut()
                        findNavController().navigate(ListMovieFragmentDirections.actionListMovieFragmentToАuthorizationFragment())
                    }
                }
                true
            }
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
                    }
                }
            }
        }
    }


    private fun chooseLayout() {
        if (layoutManager?.spanCount == 1) {
            layoutManager?.spanCount =  resources.getInteger(R.integer.grid_column_count)

        } else {
            layoutManager?.spanCount = 1
        }
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return
        menuItem.icon =
            if (layoutManager?.spanCount == 1)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}