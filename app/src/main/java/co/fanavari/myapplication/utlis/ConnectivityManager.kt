package co.fanavari.myapplication.utlis

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import co.fanavari.myapplication.LifecyleActivity
import co.fanavari.myapplication.MyApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManager @Inject constructor(
    application: MyApplication
){
    private val connectionLiveData = ConnectionLiveData(application)

     val  isNetworkAvailable = MutableLiveData(true)

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.observe(lifecycleOwner) { isConnected ->
            isConnected?.let {
                isNetworkAvailable.value = it
            }
        }
    }

    fun unRegisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner)

    }
}