package co.fanavari.myapplication.ui.gallery

import androidx.recyclerview.widget.RecyclerView
import co.fanavari.myapplication.R
import co.fanavari.myapplication.data.UnsplashPhoto
import co.fanavari.myapplication.databinding.ItemUnsplashBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PhotoViewHolder (private val binding: ItemUnsplashBinding):
    RecyclerView.ViewHolder(binding.root){

    fun bind(photo: UnsplashPhoto){
        binding.apply {
            titleAnimal.text = photo.user.userName

            Glide.with(itemView)
                .load(photo.urls.regular)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
//                .error(R.drawable.ic_error_24)
                .into(animalImageView)

        }
    }

}

