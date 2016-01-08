package heath.android.sample.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import heath.android.sample.BaseActionBarActivity;

/**
 * Created by heath on 2016/1/6.
 */
public class PlaceActivity extends BaseActionBarActivity {
    int PLACE_PICKER_REQUEST = 1;
    private TextView place_name, place_address, place_attr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root_view = new LinearLayout(this);
        root_view.setOrientation(LinearLayout.VERTICAL);
        setContentView(root_view);

        Button btn = new Button(this);
        btn.setText("取得地點");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(PlaceActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        place_name = new TextView(this);
        place_address = new TextView(this);
        place_attr = new TextView(this);
        place_name.setBackgroundColor(Color.parseColor("#BBBBBB"));
        place_address.setBackgroundColor(Color.parseColor("#DDDDDD"));
        place_attr.setBackgroundColor(Color.parseColor("#BBBBBB"));
        place_name.setTextSize(20);
        place_address.setTextSize(16);
//        place_name.setTextSize(20);

        root_view.addView(btn);
        root_view.addView(place_name);
        root_view.addView(place_address);
        root_view.addView(place_attr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();

            Log.d("CAC", "getId:" + place.getId());
            Log.d("CAC", "getName:" + place.getName());
            Log.d("CAC", "getAddress:" + place.getAddress());
            Log.d("CAC", "lat:" + place.getLatLng().latitude + ", lng:" + place.getLatLng().longitude);
            Log.d("CAC", "getPhoneNumber:" + place.getPhoneNumber());
            Log.d("CAC", "getPriceLevel:" + place.getPriceLevel());
            Log.d("CAC", "getRating:" + place.getRating());
            Log.d("CAC", "getWebsiteUri:" + place.getWebsiteUri());

            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            place_name.setText(name);
            place_address.setText(address);
            place_attr.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
