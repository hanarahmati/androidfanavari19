package co.fanavari.myapplication.ui.gallery

import android.content.Context
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
import co.fanavari.myapplication.utlis.ConnectivityManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GalleryViewModel> ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)
        val adapter = UnsplashPhotoAdapter()
        connectivityManager.registerConnectionObserver(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
                .withLoadStateHeaderAndFooter(
                    header = UnsplashPhotoLoadStateAdapter{ adapter.retry() },
                    footer = UnsplashPhotoLoadStateAdapter{ adapter.retry() }
                )

            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        connectivityManager.isNetworkAvailable.observe(viewLifecycleOwner){
            if(!it)
                Snackbar.make(binding.root, "no internet", Snackbar.LENGTH_LONG)
                    .show()
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if(loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
            adapter.itemCount < 1){
                    recyclerView.isVisible = false
                    textViewEmptyList.isVisible = true
                }
                else{
                    textViewEmptyList.isVisible = false
                }
            }


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unRegisterConnectionObserver(this)
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        connectivityManager.registerConnectionObserver(this)
    }
}