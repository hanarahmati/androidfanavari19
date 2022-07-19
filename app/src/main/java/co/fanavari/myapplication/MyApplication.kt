package co.fanavari.myapplication

import android.app.Application
import com.huawei.hms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        MapsInitializer.setApiKey("DAEDAF/j156xqMxpfxCsICy2up7Ljiq1YKBpZWW5Oe/KXS2+WadluTrUGGcxjDVwwAIb17R2scQpQRRXsijLCeLJhFQHlCx5aYpvZA==")
    }
}
