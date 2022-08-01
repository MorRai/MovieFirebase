package com.rai.movieposter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rai.movieposter.MovieViewModel
import com.rai.movieposter.data.Filters
import com.rai.movieposter.databinding.DialogFiltersBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterDialogFragment : DialogFragment() {

    private var _binding: DialogFiltersBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    //private val viewModel: MovieViewModel by activityViewModels {
    //    MovieViewModelFactory(FirebaseMovieService)
    //}
    private val viewModel by sharedViewModel<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return DialogFiltersBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val mFilters = viewModel.filterQuery.value
            if (mFilters != null) {
                if (mFilters.yearEnd != null) {
                    yearEnd.setText(mFilters.yearEnd.toString())
                }
                if (mFilters.yearStart != null) {
                    yearStart.setText(mFilters.yearStart.toString())
                }
                if (mFilters.imbdEnd != null) {
                    ImbdEnd.setText(mFilters.imbdEnd.toString())
                }
                if (mFilters.imbdStart != null) {
                    ImbdStart.setText(mFilters.imbdStart.toString())
                }
                if (mFilters.kinopoiskEnd != null) {
                    kinopoiskEnd.setText(mFilters.kinopoiskEnd.toString())
                }
                if (mFilters.kinopoiskStart != null) {
                    kinopoiskStart.setText(mFilters.kinopoiskStart.toString())
                }
            }
            buttonCancel.setOnClickListener {
                onCancelClicked()
            }
            buttonSearch.setOnClickListener {
                onSearchClicked()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    private fun onSearchClicked() {
        viewModel.filterQuery.value =filters
        dismiss()
    }

    private fun onCancelClicked() {
        dismiss()
    }


    private val filters: Filters
        get() {
            val filters = Filters()
            if (binding.yearEnd.text.toString() != "") {
                filters.yearEnd = binding.yearEnd.text.toString().toInt()
            }
            if (binding.yearStart.text.toString() != "") {
                filters.yearStart = binding.yearStart.text.toString().toInt()
            }
            if (binding.ImbdEnd.text.toString() != "") {
                filters.imbdEnd = binding.ImbdEnd.text.toString().toFloat()
            }
            if (binding.ImbdStart.text.toString() != "") {
                filters.imbdStart = binding.ImbdStart.text.toString().toFloat()
            }
            if (binding.kinopoiskEnd.text.toString() != "") {
                filters.kinopoiskEnd = binding.kinopoiskEnd.text.toString().toFloat()
            }
            if (binding.kinopoiskStart.text.toString() != "") {
                filters.kinopoiskStart =
                    binding.kinopoiskStart.text.toString().toFloat()
            }
            return filters
        }

}