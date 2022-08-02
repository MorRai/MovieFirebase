package com.rai.movieposter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.rai.movieposter.data.Constants
import com.rai.movieposter.data.Filters
import com.rai.movieposter.databinding.DialogFiltersBinding

class FilterDialogFragment : DialogFragment() {

    private var _binding: DialogFiltersBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val args by navArgs<FilterDialogFragmentArgs>()

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
            val mFilters = args.filter
            yearEnd.setText(mFilters.yearEnd?.toString() ?: "")
            yearStart.setText(mFilters.yearStart?.toString() ?: "")
            ImbdEnd.setText(mFilters.imbdEnd?.toString() ?: "")
            ImbdStart.setText(mFilters.imbdStart?.toString() ?: "")
            kinopoiskEnd.setText(mFilters.kinopoiskEnd?.toString() ?: "")
            kinopoiskStart.setText(mFilters.kinopoiskStart?.toString() ?: "")
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
        setFragmentResult(
            Constants.REQUEST_KEY,
            bundleOf(Constants.EXTRA_KEY to filters)
        )
        dismiss()
    }

    private fun onCancelClicked() {
        dismiss()
    }


    private val filters: Filters
        get() {
            val filters = Filters(null, null, null, null, null, null)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}