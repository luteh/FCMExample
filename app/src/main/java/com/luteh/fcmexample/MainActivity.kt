package com.luteh.fcmexample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
//            Crashlytics.log(Log.DEBUG, "tag", "Error crash testing 3")
            Crashlytics.logException(Throwable("Error crash testing 2"))
//            Crashlytics.getInstance().crash() // Force a crash
//            Crashlytics.getInstance()
        }

        addContentView(
            crashButton, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // To get FCM token
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d(TAG, "onCreate: $token")
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
    }
}
