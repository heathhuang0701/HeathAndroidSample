package heath.android.sample.map;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;

import heath.android.sample.BaseActionBarActivity;
import heath.android.sample.R;

/**
 * Created by heath on 2016/1/14.
 */
public class TestGeocoding extends BaseActionBarActivity {
    private static final String TAG = "TestGeocoding";
    private static final String ADDRESS = "台北市市府路45號";
    private TextView tv_address, tv_location;
    private Button btn_convert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_convert = new Button(this);
        btn_convert.setTextSize(30);
        btn_convert.setText("轉換");
        RelativeLayout.LayoutParams btn_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btn_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btn_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        getRootView().addView(btn_convert, btn_params);

        tv_address = new TextView(this);
        tv_address.setTextSize(30);
        tv_address.setId(5000);
        tv_address.setText(ADDRESS);
        RelativeLayout.LayoutParams tv1_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv1_params.addRule(RelativeLayout.CENTER_IN_PARENT);
        getRootView().addView(tv_address, tv1_params);

        tv_location = new TextView(this);
        tv_location.setTextSize(30);
        RelativeLayout.LayoutParams tv2_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2_params.addRule(RelativeLayout.BELOW, 5000);
        tv2_params.setMargins(0, 30, 0, 0);
        getRootView().addView(tv_location, tv2_params);

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertAddress();
            }
        });
    }

    private void convertAddress() {
        GeoApiContext geo_context = new GeoApiContext().setApiKey(getString(R.string.google_geocoding_key)).setQueryRateLimit(2);
        GeocodingApiRequest req = GeocodingApi.newRequest(geo_context).address(ADDRESS);
        startLoading();
        req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
                Log.d(TAG, "formattedAddress:" + result[0].formattedAddress);
                Log.d(TAG, "lat:" + result[0].geometry.location.lat);
                Log.d(TAG, "lng:" + result[0].geometry.location.lng);
                final String text = result[0].geometry.location.lat + ", " + result[0].geometry.location.lng;

                tv_location.post(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        tv_location.setText(text);
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
                tv_location.post(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                    }
                });
            }
        });
    }
}
