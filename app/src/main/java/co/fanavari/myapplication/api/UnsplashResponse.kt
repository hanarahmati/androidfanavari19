package co.fanavari.myapplication.api

import co.fanavari.myapplication.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
) {
}