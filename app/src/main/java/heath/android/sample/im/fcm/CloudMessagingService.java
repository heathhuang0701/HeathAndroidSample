package heath.android.sample.im.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import heath.android.sample.MainActivity;
import heath.android.sample.R;

/**
 * Created by heath on 2016/6/16.
 */
public class CloudMessagingService extends FirebaseMessagingService {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("Heath", "From: " + remoteMessage.getFrom());

        /*
        {
            "notification": {
                "title": "我是標題啦",
                "body": "怎麼可能有標題"
            },
            "data": {
                "Nick": "Mario",
                "body": "great match!",
                "Room": "PortugalVSDenmark"
            },
            "to": "fegA5ADrQl4:APA91bF6Pi6n_93AFmt5_4qBOdFk2X4XuVFA-Bv8yXpn3LZ9aIwAq-LzE1PA4xDfbYSnWxkP3sIkrRyxHlk2ej4aiwDn25aOkoVROU7GCcTmprpO_FL1ZegNHytY0JP5Rx0PX2_zz-B-"
        }

        data的內容可以自訂，用途是點擊通知後會自動傳送給開啟的Activity。
        若是要在這取出來，方式如下：
        Log.d("Heath", "Notification Nickname: " + remoteMessage.getData().get("Nickname"));
        Log.d("Heath", "Notification Room: " + remoteMessage.getData().get("Room"));
        */

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Log.d("Heath", "Notification Title: " + title);
        Log.d("Heath", "Notification Message Body: " + body);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
            sendNotification(title, body);
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
