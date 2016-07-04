package heath.android.sample.im.lettuce;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.event.Event;
import com.lambdaworks.redis.event.EventBus;
import com.lambdaworks.redis.pubsub.RedisPubSubConnection;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;

import java.util.concurrent.TimeUnit;

import heath.android.sample.Config;
import heath.android.sample.utils.FileUtils;
import rx.functions.Action1;

/**
 * Created by heath on 2016/6/22.
 */
public class MessagingService extends Service {
    private RedisClient client;
    private RedisPubSubConnection<String, String> connection;
    private Handler handler;
    private CountDownTimer _timer;

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
        FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "MessagingService onCreate");
        handler = new Handler();
        new Thread(subscribe).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Config.TAG, "MessagingService onDestroy");
        FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "MessagingService onDestroy");

        if (connection != null && connection.isOpen()) {
            connection.close();
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "connection closed");
        }
        if (client != null) {
            client.shutdown();
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "client shutdown");
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(Config.TAG, "MessagingService onTaskRemoved");
        FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "MessagingService onTaskRemoved");

        if (connection != null && connection.isOpen()) {
            connection.close();
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "connection closed");
        }
        if (client != null) {
            client.shutdown();
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "client shutdown");
        }
    }

    private Runnable subscribe = new Runnable() {
        @Override
        public void run() {
            _connect();
        }
    };

    private void _disconnect() {
        if (_timer != null) {
            _timer.cancel();
            _timer = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "connection closed");
        }
        if (client != null) {
            client.shutdown();
            client = null;
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "client shutdown");
        }
    }

    private void _connect() {
        _disconnect();

        try {
            client = RedisClient.create("redis://ce28bb56-c1da-46df-9288-7327c4317aafc50b778a-551b-4cd0-bbc8-b7c3a8733e51f5632a50-3066-4e6e-8a2c-88dd3a264379@54.169.219.115:6379");
            _debugWatchConnectEvent(client);

            Log.d(Config.TAG, "client start subscribe");
            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "client start subscribe");
            connection = client.connectPubSub();
            connection.addListener(new RedisPubSubListener<String, String>() {
                @Override
                public void message(String channel, String message) {
                    Log.d(Config.TAG, "channel:" + channel);
                    Log.d(Config.TAG, "message:" + message);
                    FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "channel:" + channel + ", message:" + message);
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
                    FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "subscribed channel:" + channel);
                }

                @Override
                public void psubscribed(String pattern, long count) {

                }

                @Override
                public void unsubscribed(String channel, long count) {
                    Log.d(Config.TAG, "channel:" + channel);
                    Log.d(Config.TAG, "count:" + count);
                    FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "unsubscribed channel:" + channel);
                }

                @Override
                public void punsubscribed(String pattern, long count) {

                }
            });
            connection.subscribe("heath", "huang");

            handler.post(new Runnable() {
                @Override
                public void run() {
                    _timer = new CountDownTimer(30000, 10000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Log.d(Config.TAG, "timer onFinish");
                            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "timer onFinish");
                            RedisFuture<String> ping = connection.ping();
                            if (ping.await(3, TimeUnit.SECONDS)) {
                                String err_msg = ping.getError();
                                if (err_msg.startsWith("ERR only")) {
                                    Log.d(Config.TAG, "ping succeed");
                                    FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "ping succeed");
                                } else {
                                    Log.d(Config.TAG, "ping error: " + err_msg);
                                    FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "ping error: " + err_msg);
                                }
                                _timer.start();
                            } else {
                                Log.d(Config.TAG, "ping timeout");
                                FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "ping timeout");
                                _connect();
                            }
                        }
                    };
                    _timer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            _disconnect();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    _timer = new CountDownTimer(30000, 10000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Log.d(Config.TAG, "timer onFinish");
                            FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "timer onFinish");
                            _connect();
                        }
                    };
                    _timer.start();
                }
            });
        }
    }

    private void _debugWatchConnectEvent(RedisClient _client) {
        EventBus eventBus = _client.getResources().eventBus();
        eventBus.get().subscribe(new Action1<Event>() {
            @Override
            public void call(Event event) {
                Log.d(Config.TAG, "event:" + event);
                FileUtils.logToSDCard(getBaseContext(), "lettuce.txt", "event:" + event);
            }
        });
    }
}
