package com.rai.movieposter.ui.addMovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.rai.movieposter.R
import com.rai.movieposter.data.Movie
import com.rai.movieposter.data.Response
import com.rai.movieposter.databinding.FragmentAddMovieBinding
import com.rai.movieposter.ui.detailMovie.MovieDetailFragmentArgs
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AddMovieFragment : Fragment() {

    private var _binding: FragmentAddMovieBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val args by navArgs<AddMovieFragmentArgs>()

    private val viewModel by viewModel<AddMovieViewModel> {
        parametersOf(args.movieString)
    }

    private var urlImage: String? = null
    private var urlVideo: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentAddMovieBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.movieFlow.collect { response ->
                when (response) {
                    is Response.Success -> {
                        binding.loadingProgressBar.isInvisible = true
                        bind(response.data)
                    }
                    is Response.Error -> {
                        binding.loadingProgressBar.isInvisible = true
                        Toast.makeText(requireContext(),
                            response.message, Toast.LENGTH_SHORT).show()
                    }
                    Response.Loading -> {
                        binding.loadingProgressBar.isVisible = true
                    }
                }
            }
        }

        with(binding) {
            toolbar.setupWithNavController(findNavController())

            btnChooseImage.setOnClickListener {
                imageLauncher.launch("image/*")
            }
            btnChooseVideo.setOnClickListener {
                videoLauncher.launch("video/*")
            }

            saveAction.setOnClickListener {
                val genresList = listOf(itemGenres.text.toString(),
                    itemGenres2.text.toString(),
                    itemGenres3.text.toString())

                viewModel.updateItem(nameMovie = nameMovie.text.toString(),
                    movieDate = itemData.text.toString().toInt(),
                    genres = genresList,
                    ratingImbd = ratingImbd.text.toString().toFloat(),
                    ratingKinopoisk = ratingKinopoisk.text.toString().toFloat(),
                    certificate = certificate.text.toString(),
                    countryOfOrigin = countryOfOrigin.text.toString(),
                    runtime = runtime.text.toString(),
                    storyline = storyline.text.toString(),
                    trailer = urlVideo ?: "",
                    image = urlImage ?: "")

                findNavController().navigate(R.id.action_addMovieFragment_to_listMovieFragment)
            }
        }

    }

    private fun bind(movie: Movie) {
        with(binding) {
            nameMovie.setText(movie.nameMovie)
            itemData.setText(movie.movieDate.toString())
            itemGenres.setText(movie.genres?.get(0))
            itemGenres2.setText(movie.genres?.get(1))
            itemGenres3.setText(movie.genres?.get(2))
            ratingImbd.setText(movie.ratingImbd.toString())
            ratingKinopoisk.setText(movie.ratingKinopoisk.toString())
            certificate.setText(movie.certificate)
            countryOfOrigin.setText(movie.countryOfOrigin)
            runtime.setText(movie.runtime)
            storyline.setText(movie.storyline)
            imageLabel.load(movie.image)
            urlImage = movie.image
            urlVideo = movie.trailer
            videoLabel.setVideoPath(movie.trailer)
            val mediaController = MediaController(requireContext())
            mediaController.setAnchorView(videoLabel)
            videoLabel.setMediaController(mediaController)
        }
    }

    private val imageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        lifecycle.coroutineScope.launch {
            viewModel.uploadImage(uri!!,urlImage).collect { response ->
                when (response) {
                    is Response.Success -> {
                        binding.loadingProgressBar.isInvisible = true
                        binding.saveAction.isEnabled = true
                        urlImage = response.data
                        binding.imageLabel.load(urlImage)
                    }
                    is Response.Error -> {
                        binding.loadingProgressBar.isInvisible = true
                        binding.saveAction.isEnabled = true
                        Toast.makeText(requireContext(),
                            response.message, Toast.LENGTH_SHORT).show()
                    }
                    Response.Loading -> {
                        binding.loadingProgressBar.isVisible = true
                        binding.saveAction.isEnabled = false
                        Toast.makeText(requireContext(),
                            "Выгружуется видео на сервер, ожидайте!",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private val videoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        lifecycle.coroutineScope.launch {
            viewModel.uploadImage(uri!!,urlVideo).collect { response ->
                when (response) {
                    is Response.Success -> {
                        binding.loadingProgressBar.isInvisible = true
                        binding.saveAction.isEnabled = true
                        urlVideo = response.data
                        binding.videoLabel.setVideoPath(urlVideo)
                    }
                    is Response.Error -> {
                        binding.loadingProgressBar.isInvisible = true
                        binding.saveAction.isEnabled = true
                        Toast.makeText(requireContext(),
                            response.message, Toast.LENGTH_SHORT).show()
                    }
                    Response.Loading -> {
                        binding.loadingProgressBar.isVisible = true
                        binding.saveAction.isEnabled = false
                        Toast.makeText(requireContext(),
                            "Выгружуется изображение на сервер, ожидайте!",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}