package com.rai.movieposter.ui.listMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rai.movieposter.R
import com.rai.movieposter.adapters.ListMovieAdapter
import com.rai.movieposter.data.Constants
import com.rai.movieposter.data.Filters
import com.rai.movieposter.databinding.FragmetListMovieBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
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

        with(binding){
            floatingActionButton.setOnClickListener {
                val action = ListMovieFragmentDirections.actionListMovieFragmentToAddMovieFragment(
                    getString(R.string.add_fragment_title)
                )
                findNavController().navigate(action)
            }

            layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.layoutManager = layoutManager

            adapter = ListMovieAdapter(requireContext(), layoutManager) {
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


            toolbar.menu.findItem(R.id.action_filters)
            .setOnMenuItemClickListener {
                findNavController().navigate(ListMovieFragmentDirections.actionListMovieFragmentToFilterDialogFragment(viewModel.getFilter()))
                true
            }

            toolbar.menu.findItem(R.id.action_switch_layout)
            .setOnMenuItemClickListener {
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
        if (layoutManager ?.spanCount == 1) {
            layoutManager ?.spanCount = 2

        } else {
            layoutManager ?.spanCount = 1
        }
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return
        menuItem.icon =
            if (layoutManager ?.spanCount == 1)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}