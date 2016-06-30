package heath.android.sample.im.lettuce;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.RedisPubSubConnection;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;

import heath.android.sample.Config;
import heath.android.sample.utils.FileUtils;

/**
 * Created by heath on 2016/6/22.
 */
public class MessagingService extends Service {
    private RedisClient client;
    private RedisPubSubConnection<String, String> connection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Config.TAG, "MessagingService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Config.TAG, "MessagingService onCreate");
        FileUtils.logToSDCard(getBaseContext(), "lettuce.log", "MessagingService onCreate");

        client = RedisClient.create("redis://ce28bb56-c1da-46df-9288-7327c4317aafc50b778a-551b-4cd0-bbc8-b7c3a8733e51f5632a50-3066-4e6e-8a2c-88dd3a264379@54.169.219.115:6379");
        connection = client.connectPubSub();
        connection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "message:" + message);
                FileUtils.logToSDCard(getBaseContext(), "lettuce.log", "channel:" + channel + ", message:" + message);
            }

            @Override
            public void message(String pattern, String channel, String message) {
                Log.d(Config.TAG, "pattern:" + pattern);
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "message:" + message);
            }

            @Override
            public void subscribed(String channel, long count) {
                Log.d(Config.TAG, "subscribed:" + System.currentTimeMillis());
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "count:" + count);
                FileUtils.logToSDCard(getBaseContext(), "lettuce.log", "subscribed channel:" + channel);
            }

            @Override
            public void psubscribed(String pattern, long count) {

            }

            @Override
            public void unsubscribed(String channel, long count) {
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "count:" + count);
                FileUtils.logToSDCard(getBaseContext(), "lettuce.log", "unsubscribed channel:" + channel);
            }

            @Override
            public void punsubscribed(String pattern, long count) {

            }
        });
        connection.subscribe("heath", "huang");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Config.TAG, "MessagingService onDestroy");
        FileUtils.logToSDCard(getBaseContext(), "lettuce.log", "MessagingService onDestroy");

        if (connection != null && connection.isOpen()) {
            connection.close();
        }
        if (client != null) {
            client.shutdown();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(Config.TAG, "MessagingService onTaskRemoved");
        FileUtils.logToSDCard(getBaseContext(), "lettuce.log", "MessagingService onTaskRemoved");

        if (connection != null && connection.isOpen()) {
            connection.close();
        }
        if (client != null) {
            client.shutdown();
        }
    }
}
