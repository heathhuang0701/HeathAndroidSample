package heath.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import heath.android.sample.im.lettuce.MessagingService;
import heath.android.sample.model.ModelStringList;

/**
 * Created by heath on 2016/1/6.
 */
public class MainActivity extends StringListActivity {
    private static final int REQUEST_CODE_STORAGE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, MessagingService.class));

        /*
        點擊通知後，若是有data就可以直接在被通知呼叫的Activity取出
         */
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("Heath", "Key: " + key + " Value: " + value);
            }
        }

        setNavigationTitle("Samples");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        updateItems(R.raw.sample_data);
        setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ModelStringList model = (ModelStringList) parent.getAdapter().getItem(position);
                    Class activity = Class.forName(getPackageName() + model.activity);
                    startActivity(new Intent(mContext, activity).putExtra("title", model.title));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
