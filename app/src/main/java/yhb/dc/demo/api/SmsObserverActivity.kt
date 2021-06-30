package yhb.dc.demo.api

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsMessage
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import yhb.dc.common.Demo
import yhb.dc.databinding.ActivitySmsObserverBinding

private const val SMS_URI_ALL = "content://sms/"
private val sProjection = arrayOf("_id", "address", "body", "date")
private const val tag = "SmsObserverTag"

@Demo(id = Demo.DEMO_SMS_OBSERVE)
class SmsObserverActivity : AppCompatActivity(), OnGetAndSetPinListener {
    private val mListener = this
    private lateinit var binding: ActivitySmsObserverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsObserverBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val hasNoSendPermission =
            checkSelfPermission(this, Manifest.permission.SEND_SMS) != PERMISSION_GRANTED
        val hasNoReadPermission =
            checkSelfPermission(this, Manifest.permission.READ_SMS) != PERMISSION_GRANTED
        val hasNoReceivePermission =
            checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PERMISSION_GRANTED

        // Here, thisActivity is the current activity
        if (hasNoSendPermission || hasNoReadPermission || hasNoReceivePermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS
                ), 0
            )
        } else {
            // Permission has already been granted
            initReceiverAndObserver()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initReceiverAndObserver()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun initReceiverAndObserver() {
        try {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
            intentFilter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY - 1
            registerReceiver(mReceiver, intentFilter)
            log("registerReceiver suc.")
            contentResolver.registerContentObserver(Uri.parse(SMS_URI_ALL), true, mSmsObserver)
            log("registerReceiver registerContentObserver.")
        } catch (e: Exception) { //异常case: 小米6或mix android8.0手机 需要检查权限
            log("error e=$e")
            e.printStackTrace()
        }

    }


    private fun log(str: String) = Log.i(tag, str)

    private var mSmsObserver: ContentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            log("[mSmsObserver] onChange selfChange=$selfChange.")
            val smsUri = Uri.parse(SMS_URI_ALL)
            val selection = " body is NOT NULL ) GROUP BY ( address "
            try {
                val cursor: Cursor? = contentResolver.query(
                    smsUri,
                    sProjection,
                    selection,
                    null,
                    " _id DESC " + " LIMIT 1 "
                )
                cursor ?: return
                val indexBody = cursor.getColumnIndex("body")
                val indexAddress = cursor.getColumnIndex("address")
                val indexDate = cursor.getColumnIndex("date")
                if (cursor.moveToFirst()) {
                    val body = cursor.getString(indexBody)
                    val smsGateway = cursor.getString(indexAddress)
                    val longDate = cursor.getLong(indexDate)
                    mListener.onGetAndSetPinFromSms(body, longDate, smsGateway)
                }
                cursor.close()
            } catch (e: java.lang.Exception) {
                log("error e=$e")
            }
        }

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            onChange(selfChange)
        }
    }

    private var mReceiver: BroadcastReceiver? = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            log("[mReceiver] onReceive intent=$intent.")
            var smsGateway: String? = ""
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>
                if (pdus.isEmpty()) {
                    return
                }
                val smsContents = arrayOfNulls<SmsMessage>(pdus.size)
                val body = StringBuilder()
                try {
                    for (i in pdus.indices) {
                        val createFromPdu = SmsMessage
                            .createFromPdu(pdus[i] as ByteArray) ?: continue
                        smsContents[i] = createFromPdu
                        val part = createFromPdu.messageBody
                        smsGateway = createFromPdu.originatingAddress ?: ""
                        if (!TextUtils.isEmpty(part)) {
                            body.append(part)
                        }
                    }
                } catch (e: OutOfMemoryError) {
                    e.printStackTrace()
                    log("error e=$e")
                    return
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    log("error e=$e")
                    return
                }
                mListener.onGetAndSetPinFromSms(
                    body.toString(),
                    System.currentTimeMillis(),
                    smsGateway
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        log("unregisterReceiver suc.")
        contentResolver.unregisterContentObserver(mSmsObserver)
        log("unregisterContentObserver suc.")
    }

    override fun onGetAndSetPinFromSms(
        content: String?,
        longDate: Long,
        gateway: String?
    ): Boolean {
        Toast.makeText(this, "收到信息", Toast.LENGTH_SHORT).show()
        binding.tvSmsContent.text = content
        log("onGetAndSetPinFromSms suc, content=$content, gateway=$gateway, longDate=$longDate")
        return true
    }


}

interface OnGetAndSetPinListener {
    fun onGetAndSetPinFromSms(content: String?, longDate: Long, gateway: String?): Boolean
}
