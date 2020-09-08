package heath.android.sample

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import me.yokeyword.fragmentation.Fragmentation
import timber.log.Timber

/**
 * Created by heath on 2016/6/16.
 */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        Fragmentation.builder()
            // 设置 栈视图 模式为 BUBBLE: 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
            .stackViewMode(Fragmentation.NONE)
            .debug(BuildConfig.DEBUG)
            .install()
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
//            FakeCrashLibrary.log(priority, tag, message)
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t)
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t)
//                }
//            }
        }
    }
}