package heath.android.sample.multimedia.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import heath.android.sample.R;
import heath.android.sample.StringListActivity;
import heath.android.sample.model.ModelStringList;

/**
 * Created by heath on 2016/1/7.
 */
public class QrcodeSamples extends StringListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateItems(R.raw.qrcode_sample);
        setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ModelStringList model = (ModelStringList) parent.getAdapter().getItem(position);
                    Class activity = Class.forName(getPackageName() + model.activity);
                    startActivity(new Intent(mContext, activity));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}