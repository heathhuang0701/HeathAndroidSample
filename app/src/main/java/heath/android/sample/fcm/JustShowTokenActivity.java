package heath.android.sample.fcm;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import heath.android.sample.BaseActionBarActivity;

/**
 * Created by heath on 2016/6/16.
 */
public class JustShowTokenActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Heath", "Refreshed token: " + refreshedToken);
        TextView tv = new TextView(mContext);
        tv.setText(refreshedToken);
        setContentView(tv);
    }
}
