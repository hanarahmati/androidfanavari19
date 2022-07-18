package co.fanavari.myapplication.huawei

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import co.fanavari.myapplication.LifecyleActivity
import co.fanavari.myapplication.NavigationComponentExampleActivity
import co.fanavari.myapplication.R
import co.fanavari.myapplication.databinding.ActivityHuaweiPushBinding
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HuaweiPushActivity : AppCompatActivity() {

    private lateinit var binding: co.fanavari.myapplication.databinding.ActivityHuaweiPushBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityHuaweiPushBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnGetToken.setOnClickListener {
                getToken()
                addTopic()
            }
            buttonSetToken.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    actionCreateLocalNotification("test","testtttttttttttt newwww")
                }
            }
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

        }}

    private fun actionCreateLocalNotification(title: String?, message: String?){
        val channelId = "test channel"
        val channelName = "test channel name"
        val intent = Intent(this, NavigationComponentExampleActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,channelId )
            .setSmallIcon(R.drawable.ic_add)
            .setContentIntent(pendingIntent)
            .setContentText(message)
            .setContentTitle(title)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())


    }

    companion object {
        private const val CODELABS_ACTION: String = "com.huawei.codelabpush.action"
        val TAG = "PushDemoLog"
    }
}