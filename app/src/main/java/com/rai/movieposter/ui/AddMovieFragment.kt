package com.rai.movieposter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.rai.movieposter.MovieViewModel
import com.rai.movieposter.R
import com.rai.movieposter.databinding.FragmentAddMovieBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AddMovieFragment : Fragment() {

    private var _binding: FragmentAddMovieBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val navigationArgs: AddMovieFragmentArgs by navArgs()

    private val viewModel by sharedViewModel<MovieViewModel>()

   // private val viewModel: MovieViewModel by activityViewModels {
//        MovieViewModelFactory(FirebaseMovieService)
  //  }

    private var uriImage: String? = null
    private var uriVideo: String? = null

    private val imageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        binding.imageLabel.setImageURI(uri)
        uriImage = null
        lifecycle.coroutineScope.launch {
            viewModel.uploadImage(uri!!).collect {
                uriImage = it
            }
        }
    }


    private val videoLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        binding.videoLabel.setVideoURI(uri)
        uriVideo = null
        lifecycle.coroutineScope.launch {
            viewModel.uploadImage(uri!!).collect {
                uriVideo = it
            }
        }
    }

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
        val movie = navigationArgs.movie
        with(binding) {
            if (movie != null) {
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
                uriImage = movie.image
                uriVideo = movie.trailer
                videoLabel.setVideoPath(movie.trailer)

            }


            val mediaController = MediaController(requireContext())
            mediaController.setAnchorView(videoLabel)
            videoLabel.setMediaController(mediaController)


            btnChooseImage.setOnClickListener {
                imageLauncher.launch("image/*")
            }

            btnChooseVideo.setOnClickListener {
                videoLauncher.launch("video/*")
            }

            saveAction.setOnClickListener {
                if (uriImage == null ||uriVideo == null) {
                    Toast.makeText(requireContext(), "Выгружуется изображение или видео на сервер, ожидайте!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
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
                    trailer = uriVideo ?: "",
                    image = uriImage ?: "")

                findNavController().navigate(R.id.action_addMovieFragment_to_listMovieFragment)
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}