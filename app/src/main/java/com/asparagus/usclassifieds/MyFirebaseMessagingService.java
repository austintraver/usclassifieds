package com.asparagus.usclassifieds;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        String otherUser = "";
        String message = "";
        if (remoteMessage.getNotification() != null && GlobalHelper.getUser() != null) {
            otherUser = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            System.out.println("message: " + message);
            if(message.contains("sent")) {
                System.out.println("onMessageReceived: " + "adding incoming friend request");
                GlobalHelper.getUser().getIncomingFriendRequests().put(otherUser, "true");
                System.out.println(GlobalHelper.getUser().getIncomingFriendRequests());
            } else if(message.contains("cancelled")) {
                System.out.println("onMessageReceived: " + "removing incoming friend request");
                GlobalHelper.getUser().getIncomingFriendRequests().remove(otherUser);
            } else if(message.contains("accepted")) {
                System.out.println("onMessageReceived: " + "adding friend");
                GlobalHelper.getUser().getFriends().put(otherUser, "true");
                GlobalHelper.getUser().getOutgoingFriendRequests().remove(otherUser);
            } else if(message.contains("rejected")) {
                System.out.println("onMessageReceived: " + "removing outgoing friend request");
                GlobalHelper.getUser().getOutgoingFriendRequests().remove(otherUser);
            } else if(message.contains("removed")) {
                System.out.println("onMessageReceived: " + "removing friend");
                GlobalHelper.getUser().getFriends().remove(otherUser);
            }

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if(!GlobalHelper.getUserID().equals(otherUser)) {
                sendNotification(remoteMessage.getNotification().getBody());
            }
        }

        /* Also if you intend on generating your own notifications as a result
        of a received FCM message, here is where that should be initiated.
        See sendNotification method below. */
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        /* If you want to send messages to this application instance or manage
        this apps subscriptions on the server side, send the Instance ID token
        to your app server. */
        sendRegistrationToServer(token);
    }

    /* Handle time allotted to BroadcastReceivers */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * Modify this method to associate the person's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {}

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo).setContentTitle(getString(R.string.fcm__message))
                .setContentText(messageBody).setAutoCancel(true).setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title",
                                                                  NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}