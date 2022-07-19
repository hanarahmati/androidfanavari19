package co.fanavari.myapplication.huawei

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import co.fanavari.myapplication.databinding.ActivityMapBinding
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.utils.LogM


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    // HUAWEI map
    private var hMap: HuaweiMap? = null

    private var mMapView: MapView? = null

    companion object {
        private const val TAG = "MapViewDemoActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private val RUNTIME_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)
        private const val REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        LogM.d(TAG, "onCreate:")
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!hasPermissions(this, *RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE)
        }
        MapsInitializer.initialize(this);
        MapsInitializer.setApiKey("DAEDAF/j156xqMxpfxCsICy2up7Ljiq1YKBpZWW5Oe/KXS2+WadluTrUGGcxjDVwwAIb17R2scQpQRRXsijLCeLJhFQHlCx5aYpvZA==")
        MapsInitializer.setAccessToken("test")
        mMapView = binding.mapView
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }

        mMapView?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@MapActivity)
        }
    }
    override fun onMapReady(huaweiMap: HuaweiMap?) {

        hMap = huaweiMap
        hMap?.uiSettings?.isMyLocationButtonEnabled = true
        hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.893478, 2.334595), 10f))
        hMap?.isMyLocationEnabled = true

    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }
}