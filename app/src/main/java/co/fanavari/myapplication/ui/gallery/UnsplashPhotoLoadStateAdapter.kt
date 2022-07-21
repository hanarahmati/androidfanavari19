package co.fanavari.myapplication.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import co.fanavari.myapplication.databinding.UnsplashPhotoStateFooterBinding

class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) :
LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>(){
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val biding = UnsplashPhotoStateFooterBinding.inflate(LayoutInflater.from(
            parent.context
        ),
        parent,
        false)
        return LoadStateViewHolder(biding)
    }

    inner class LoadStateViewHolder(private val biding: UnsplashPhotoStateFooterBinding):
            RecyclerView.ViewHolder(biding.root){

                init {
                    biding.buttonRetry.setOnClickListener {
                        retry.invoke()
                    }
                }

        fun bind(loadState: LoadState){
            biding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
            }



}