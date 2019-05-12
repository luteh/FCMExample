package com.luteh.fcmexample.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessaging

class MyFirebaseInstanceService : FirebaseInstanceIdService() {

    private val TAG = "MyFirebaseInstanceServi"

    override fun onTokenRefresh() {
        // Get updated InstanceID token
        val refreshedToken = FirebaseInstanceId.getInstance().token

        FirebaseMessaging.getInstance().subscribeToTopic("all")

        Log.d(TAG, "onTokenRefresh: Refreshed token = $refreshedToken")

        /* If you want to send messages to this application instance or manage this apps subscriptions on the server side,
        send the Instance ID token to your app server.*/

        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {
        Log.d(TAG, "sendRegistrationToServer: TOKEN = $refreshedToken")
    }
}
