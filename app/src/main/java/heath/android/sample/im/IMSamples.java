package heath.android.sample.im;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import heath.android.sample.R;
import heath.android.sample.StringListActivity;
import heath.android.sample.model.ModelStringList;

/**
 * Created by heath on 2016/6/21.
 */
public class IMSamples extends StringListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateItems(R.raw.im_sample);
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
