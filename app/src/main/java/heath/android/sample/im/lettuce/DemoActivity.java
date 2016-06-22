package heath.android.sample.im.lettuce;

import android.os.Bundle;
import android.util.Log;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.RedisPubSubConnection;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;

import heath.android.sample.BaseActionBarActivity;
import heath.android.sample.Config;

/**
 * Created by heath on 2016/6/21.
 */
public class DemoActivity extends BaseActionBarActivity {
    private RedisClient client;
    private RedisPubSubConnection<String, String> connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* ping, pong */
//        RedisClient client = RedisClient.create("redis://password@127.0.0.1:6379");
//        RedisConnection<String, String> connection = client.connect();
//        String result = connection.ping();
//        Log.d(Config.TAG, "result:" + result);
//        connection.close();

        client = RedisClient.create("redis://password@127.0.0.1:6379");
        connection = client.connectPubSub();
        connection.addListener(new RedisPubSubListener<String, String>() {
            @Override
            public void message(String channel, String message) {
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "message:" + message);
            }

            @Override
            public void message(String pattern, String channel, String message) {
                Log.d(Config.TAG, "pattern:" + pattern);
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "message:" + message);
            }

            @Override
            public void subscribed(String channel, long count) {
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "count:" + count);
            }

            @Override
            public void psubscribed(String pattern, long count) {

            }

            @Override
            public void unsubscribed(String channel, long count) {
                Log.d(Config.TAG, "channel:" + channel);
                Log.d(Config.TAG, "count:" + count);
            }

            @Override
            public void punsubscribed(String pattern, long count) {

            }
        });
        connection.subscribe("heath", "huang");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        connection.close();
        client.shutdown();
    }
}
