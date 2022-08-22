package com.rai.movieposter.ui.detailMovie

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.google.android.youtube.player.*
import com.rai.movieposter.BuildConfig
import com.rai.movieposter.R
import com.rai.movieposter.data.Movie
import com.rai.movieposter.data.Response
import com.rai.movieposter.databinding.FragmetMovieDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MovieDetailFragment : Fragment() {

    private var _binding: FragmetMovieDetailBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val args by navArgs<MovieDetailFragmentArgs>()

    private val viewModel by viewModel<MovieDetailViewModel> {
        parametersOf(args.movieName)
    }

    private var youTubePlayerFragment: YouTubePlayerFragmentX? = null
    private var youTubePlayer: YouTubePlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = FragmetMovieDetailBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
        initializeYoutubePlayer()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeYoutubePlayer()
        binding.toolbar.setupWithNavController(findNavController())
        lifecycleScope.launch {
            viewModel.movieFlow.collect { response ->
                when (response) {
                    is Response.Success -> {
                        binding.paginationProgressBar.isInvisible = true
                        bind(response.data)
                    }
                    is Response.Error -> {
                        binding.paginationProgressBar.isInvisible = true
                        Toast.makeText(requireContext(),
                            response.message, Toast.LENGTH_SHORT).show()
                    }
                    Response.Loading -> {
                        binding.paginationProgressBar.isVisible = true
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bind(movie: Movie) {
        with(binding) {
            nameMovie.text = movie.nameMovie.toString()
            certificateYearCountry.text =
                movie.certificate.toString() + movie.movieDate.toString() + movie.countryOfOrigin.toString()
            genres.text = movie.genres.toString()
                .replace("[", "")
                .replace("]", "")
            runtime.text = movie.runtime.toString()
            storyline.text = movie.storyline.toString()
            imageMovie.load(movie.image)
            youTubePlayer?.loadVideo(movie.trailer)
            editItem.setOnClickListener {
                val action =
                    MovieDetailFragmentDirections.actionMovieDetailFragmentToAddMovieFragment(
                        getString(R.string.edit_fragment_title),
                        movie.nameMovie
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun initializeYoutubePlayer() {
        youTubePlayerFragment = childFragmentManager
           .findFragmentById(R.id.videoView) as YouTubePlayerFragmentX?

        youTubePlayerFragment?.initialize(BuildConfig.youtubeApiKey,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    player: YouTubePlayer,
                    wasRestored: Boolean,
                ) {
                    if (!wasRestored) {
                        youTubePlayer = player
                        youTubePlayer!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    }
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubeInitializationResult?,
                ) {
                    Toast.makeText(requireContext(),
                        "Video player failed!", Toast.LENGTH_SHORT).show()
                }

            })?: Toast.makeText(requireContext(),
            "Video player don't initialize!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}