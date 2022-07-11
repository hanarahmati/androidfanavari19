package co.fanavari.myapplication.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.fanavari.myapplication.data.UnsplashPhoto
import co.fanavari.myapplication.databinding.ItemUnsplashBinding

class UnsplashPhotoAdapter : PagingDataAdapter<UnsplashPhoto,
        PhotoViewHolder>(PHOTO_COMARATOR) {
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemUnsplashBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false
        )
        return PhotoViewHolder(binding)
    }

    companion object{
        private val PHOTO_COMARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>(){
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ) = oldItem == newItem

        }
    }
}

