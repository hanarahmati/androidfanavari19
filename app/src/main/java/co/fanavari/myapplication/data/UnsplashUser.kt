package co.fanavari.myapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashUser(
    val userName: String,
    val name: String
): Parcelable {

}
