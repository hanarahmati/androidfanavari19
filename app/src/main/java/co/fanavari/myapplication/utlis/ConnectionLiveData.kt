package co.fanavari.myapplication.utlis

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

val TAG = "C-MANAGER"
class ConnectionLiveData(context: Context):LiveData<Boolean> (){

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetwork: MutableSet<Network> = HashSet()

    private fun checkValidNetworks(){
        postValue(validNetwork.size > 0)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    override fun onActive() {

        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG,"onAvailable: $network")
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            Log.d(TAG, "onAvailable: $network $hasInternetCapability")
            if(hasInternetCapability == true){
                Log.d(TAG,"onAvailable: adding network. $network" )
                validNetwork.add(network)
                checkValidNetworks()
            }
        }

        override fun onLost(network: Network) {
            Log.d(TAG,"onLost: $network")
            validNetwork.remove(network)
            checkValidNetworks()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            if(networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET)){
                validNetwork.add(network)
                checkValidNetworks()
            }else {
                validNetwork.remove(network)
                checkValidNetworks()
            }
        }
    }



}