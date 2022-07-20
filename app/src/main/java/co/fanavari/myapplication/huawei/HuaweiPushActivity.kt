package co.fanavari.myapplication.huawei

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import co.fanavari.myapplication.NavigationComponentExampleActivity
import co.fanavari.myapplication.R
import co.fanavari.myapplication.databinding.ActivityHuaweiPushBinding
import com.huawei.agconnect.common.network.AccessNetworkManager
import com.huawei.hmf.tasks.Task
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.HAEventType.SUBMITSCORE
import com.huawei.hms.analytics.type.HAParamType.SCORE
import com.huawei.hms.common.ApiException
import com.huawei.hms.location.*
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HuaweiPushActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHuaweiPushBinding
    private lateinit var instance: HiAnalyticsInstance
    // Define a location provider client.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // Define a device setting client.
    private lateinit var settingsClient: SettingsClient
    var mLocationRequest: LocationRequest? = null
    var mLocationCallback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityHuaweiPushBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnGetToken.setOnClickListener {
                getToken()
                addTopic()
                reportAnswerEvt("test")
                postScore(25L)
                setUserprofile("hana")
                requestLocationUpdatesWithCallback()
            }
            buttonSetToken.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    actionCreateLocalNotification("test", "testtttttttttttt newwww")

                }
                removeLocationUpdatesWithCallback()
            }
            buttonOpenMapActivity.setOnClickListener {
                val intent = Intent(this@HuaweiPushActivity, MapActivity::class.java)
                startActivity(intent)

            }
        }

        AccessNetworkManager.getInstance().setAccessNetwork(true)

        // Enable SDK log recording.
        HiAnalyticsTools.enableLog()
        instance = HiAnalytics.getInstance(this)
        // Or initialize Analytics Kit with the given context.
        instance.setUserProfile("userKey", "value")

        applyPermission()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

// Set the interval for requesting location updates (unit: ms).
// Set the location type.
        mLocationRequest = LocationRequest().apply {
            interval = 1000
            needAddress = true
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        var mLocationCallback: LocationCallback? = null
        if (null == mLocationCallback) {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult != null) {
                        val locations: List<Location> =
                            locationResult.locations
                        if (locations.isNotEmpty()) {
                            for (location in locations) {
                                Log.i(
                                    TAG,
                                    "onLocationResult location[Longitude,Latitude,Accuracy]:${location.longitude} , ${location.latitude} , ${location.accuracy}"
                                )
                            }
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    locationAvailability?.let {
                        val flag: Boolean = locationAvailability.isLocationAvailable
                        Log.i(TAG, "onLocationAvailability isLocationAvailable:$flag")
                    }
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful")
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION  failed")
            }
        }
        if (requestCode == 2) {
            if (grantResults.size > 2 && grantResults[2] == PERMISSION_GRANTED && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful"
                )
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed")
            }
        }
        if (requestCode == 3) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED
                || grantResults[1] == PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply LOCATION PERMISSION successful"
                )
                RequestPermission.requestBackgroundPermission(this)
            } else {
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed"
                )
            }
        }
    }

    private fun requestLocationUpdatesWithCallback() {
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // check devices settings before request location updates.
            //Before requesting location update, invoke checkLocationSettings to check device settings.
            val locationSettingsResponseTask: Task<LocationSettingsResponse> =
                settingsClient.checkLocationSettings(locationSettingsRequest)

            locationSettingsResponseTask.addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse? ->
                Log.i(TAG, "check location settings success  {$locationSettingsResponse}")
                // request location updates
                fusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper()
                )
                    .addOnSuccessListener {
                        LocationLog.i(TAG, "requestLocationUpdatesWithCallback onSuccess")
                    }
                    .addOnFailureListener { e ->
                        LocationLog.e(
                            TAG,
                            "requestLocationUpdatesWithCallback onFailure:${e.message}"
                        )
                    }
            }
                .addOnFailureListener { e: Exception ->
                    LocationLog.e(TAG, "checkLocationSetting onFailure:${e.message}")
                }
        } catch (e: Exception) {
            LocationLog.e(TAG, "requestLocationUpdatesWithCallback exception:${e.message}")
        }
    }

    private fun removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                    LocationLog.i(
                        TAG,
                        "removeLocationUpdatesWithCallback onSuccess"
                    )
                }
                .addOnFailureListener { e ->
                    LocationLog.e(
                        TAG,
                        "removeLocationUpdatesWithCallback onFailure:${e.message}"
                    )
                }
        } catch (e: Exception) {
            LocationLog.e(
                TAG,
                "removeLocationUpdatesWithCallback exception:${e.message}"
            )
        }
    }

    private fun getToken() {
        // Create a thread.
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Obtain the app ID from the agconnect-services.json file.
                val appId = "106625821"

                // Set tokenScope to HCM.
                val tokenScope = "HCM"
                val token =
                    HmsInstanceId.getInstance(this@HuaweiPushActivity).getToken(appId, tokenScope)
                Log.i(TAG, "get token:$token")

                // Check whether the token is null.
                if (!TextUtils.isEmpty(token)) {
                    sendRegTokenToServer(token)
                }
            } catch (e: ApiException) {
                Log.e(TAG, "get token failed, $e")
            }

        }
    }

    private fun sendRegTokenToServer(token: String?) {
        Log.i(TAG, "sending token to server. token:$token")
        val messageId = System.currentTimeMillis().toString()

// The input parameter of the RemoteMessage.Builder method is push.hcm.upstream, which cannot be changed.
        val remoteMessage = RemoteMessage.Builder("push.hcm.upstream")
            .setMessageId(messageId)
            .addData("key1", "data1")
            .addData("key2", "data2")
            .build()
        try {
            // Send an uplink message.
            HmsMessaging.getInstance(this).send(remoteMessage)
            Log.i(TAG, "send message successfully")
        } catch (e: Exception) {
            Log.e(TAG, "send message catch exception: $e")
        }
    }

    private fun setReceiveNotifyMsg(enable: Boolean) {
        showLog("Control the display of notification messages:begin")
        if (enable) {
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showLog("turnOnPush Complete")
                    binding.textViewShowToken.setText("set_push_unable")
                } else {
                    showLog("turnOnPush failed: cause=" + task.exception.message)
                }
            }
        } else {
            HmsMessaging.getInstance(this).turnOffPush().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showLog("turnOffPush Complete")
                    binding.textViewShowToken.setText("set_push_enable")
                } else {
                    showLog("turnOffPush  failed: cause =" + task.exception.message)
                }
            }
        }
    }

    private fun showLog(log: String?) {
        CoroutineScope(Dispatchers.Main).launch {

            binding.textViewShowLog.text = log

        }
    }

    private fun addTopic() {
        binding.btnGetToken.setOnClickListener {
            try {
                HmsMessaging.getInstance(this@HuaweiPushActivity)
                    .subscribe("msg")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i(TAG, "subscribe Complete")
                            showLog("subscribe Complete")
                        } else {
                            showLog("subscribe failed: ret=" + task.exception.message)
                        }
                    }
            } catch (e: Exception) {
                showLog("subscribe failed: exception=" + e.message)
            }

        }
    }

    private fun actionCreateLocalNotification(title: String?, message: String?) {
        val channelId = "test channel"
        val channelName = "test channel name"
        val intent = Intent(this, NavigationComponentExampleActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_add)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setContentTitle(title)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())


    }

    private fun reportAnswerEvt(answer: String) {
        // TODO: Report a custom event.
        // Event name: Answer
        // Event parameters:
        //  -- question: String
        //  -- answer: String
        //  -- answerTime: String

        // Initialize parameters.
        val bundle = Bundle()
        bundle.putString("question", binding.btnGetToken.getText().toString().trim())
        bundle.putString("answer", answer)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        bundle.putString("answerTime", sdf.format(Date()))

        // Report a custom event.
        instance.onEvent("Answer", bundle)
    }

    private fun postScore(score: Long) {
        // TODO: Report a score through the SUBMITSCORE event.
        // Initialize parameters.
        val bundle = Bundle()
        bundle.putLong(SCORE, score)

        // Report a predefined event.
        instance.onEvent(SUBMITSCORE, bundle)
    }

    private fun setUserprofile(editFavorSport: String){
        instance.setUserProfile("favor_sport",editFavorSport);
    }

    private fun applyPermission(){
        // Dynamically apply for required permissions if the API level is 28 or smaller.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "android sdk <= 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            // Dynamically apply for required permissions if the API level is greater than 28. The android.permission.ACCESS_BACKGROUND_LOCATION permission is required.
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }
    }

    companion object {
        private const val CODELABS_ACTION: String = "com.huawei.codelabpush.action"
        val TAG = "PushDemoLog"
    }

    override fun onDestroy() {
        // don't need to receive callback
        removeLocationUpdatesWithCallback()
        super.onDestroy()
    }
}