package co.fanavari.myapplication.huawei

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import co.fanavari.myapplication.NavigationComponentExampleActivity
import co.fanavari.myapplication.R
import co.fanavari.myapplication.databinding.ActivityHuaweiPushBinding
import com.huawei.agconnect.common.network.AccessNetworkManager
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.HAEventType.SUBMITSCORE
import com.huawei.hms.analytics.type.HAParamType.SCORE
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HuaweiPushActivity : AppCompatActivity() {

    private lateinit var binding: co.fanavari.myapplication.databinding.ActivityHuaweiPushBinding
    private lateinit var instance: HiAnalyticsInstance
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
            }
            buttonSetToken.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    actionCreateLocalNotification("test", "testtttttttttttt newwww")
                }
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

    companion object {
        private const val CODELABS_ACTION: String = "com.huawei.codelabpush.action"
        val TAG = "PushDemoLog"
    }
}