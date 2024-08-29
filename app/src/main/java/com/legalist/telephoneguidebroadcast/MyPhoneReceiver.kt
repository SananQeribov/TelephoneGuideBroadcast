package com.legalist.telephoneguidebroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MyPhoneReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context?, intent: Intent?) {
        val telephonyManager =
            context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // API 31 və yuxarı üçün registerTelephonyCallback istifadə olunur
            val telephonyCallback =
                object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                    override fun onCallStateChanged(state: Int) {

                    }
                }
            telephonyManager.registerTelephonyCallback(context.mainExecutor, telephonyCallback)

        } else {
            telephonyManager.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {

                    println("phonenumber: $phoneNumber")
                    val localmanager = LocalBroadcastManager.getInstance(context)
                    val intent = Intent("my.result.receiver")
                    intent.putExtra("phoneNumber", phoneNumber)
                    localmanager.sendBroadcast(intent)
                }


            }, PhoneStateListener.LISTEN_CALL_STATE)
        }
    }
}
