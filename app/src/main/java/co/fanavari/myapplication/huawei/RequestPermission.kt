package co.fanavari.myapplication.huawei

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

object RequestPermission {
    var TAG = "RequestPermission"
    fun requestLocationPermission(context: Context?) {
        // check location permisiion
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q")
            if (context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
                && context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(context as Activity, strings, 1)
            }
        } else {
            if (context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && context?.let {
                    checkSelfPermission(
                        it,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(context as Activity, strings, 2)
            }
        }
    }

    fun requestActivityTransitionPermission(context: Context?) {

        // check location permisiion
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (checkSelfPermission(
                    context!!,
                    "com.huawei.hms.permission.ACTIVITY_RECOGNITION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf("com.huawei.hms.permission.ACTIVITY_RECOGNITION")
                ActivityCompat.requestPermissions(context as Activity, permissions, 1)
                LocationLog.i(TAG, "requestActivityTransitionButtonHandler: apply permission")
            }
        } else {
            if (checkSelfPermission(
                    context!!,
                    "android.permission.ACTIVITY_RECOGNITION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions =
                    arrayOf("android.permission.ACTIVITY_RECOGNITION")
                ActivityCompat.requestPermissions(context as Activity, permissions, 2)
                LocationLog.i(TAG, "requestActivityTransitionButtonHandler: apply permission")
            }
        }
    }

    fun requestBackgroundPermission(context: Context?) {
        if (checkSelfPermission(
                context!!,
                "android.permission.ACCESS_BACKGROUND_LOCATION"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf("android.permission.ACCESS_BACKGROUND_LOCATION")
            if (context is Activity) {
                ActivityCompat.requestPermissions((context as Activity?)!!, permissions, 0)
            }
            LocationLog.i(TAG, "android.permission.ACCESS_BACKGROUND_LOCATION")
        }
    }
}