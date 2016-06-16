package heath.android.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by heath on 2016/6/16.
 */
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
