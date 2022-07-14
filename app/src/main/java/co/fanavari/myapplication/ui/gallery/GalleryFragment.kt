package co.fanavari.myapplication.ui.gallery

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import co.fanavari.myapplication.R
import co.fanavari.myapplication.databinding.FragmentGalleryBinding
import co.fanavari.myapplication.utils.ConnectivityManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {


    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GalleryViewModel>()

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
                .withLoadStateHeaderAndFooter(
                    header = UnsplashPhotoLoadStateAdapter{adapter.retry()},
                    footer = UnsplashPhotoLoadStateAdapter{adapter.retry()}
                )
        }
        viewModel.photos.observe(viewLifecycleOwner) {

            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        binding.buttonRetry.setOnClickListener {
            adapter.retry()
        }


        connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner) {
            if (!it) {

                Snackbar.make(binding.root, "no internet", Snackbar.LENGTH_LONG)
                    .show()

            }

        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmptyList.isVisible = true
                } else {
                    textViewEmptyList.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unregisterConnectionObserver(this)
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(viewLifecycleOwner)
    }
}
