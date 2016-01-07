package heath.android.sample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import heath.android.sample.model.ModelStringList;

public class StringListActivity extends BaseActionBarActivity {
    private StringListAdapter adapter;
    private ListView string_list_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        string_list_listview = new ListView(mContext);
        setContentView(string_list_listview);

        _setViews();
    }

    @Override
    protected void onDestroy() {
        recycleAllUsedBitmaps();

        super.onDestroy();
    }

    private void _setViews() {
        adapter = new StringListAdapter();
        string_list_listview.setAdapter(adapter);
    }

    protected void updateItems(int resource_id) {
        try {
            ArrayList<ModelStringList> items = readData(resource_id);
            adapter.setData(items);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setItemClickListener(AdapterView.OnItemClickListener listener) {
        string_list_listview.setOnItemClickListener(listener);
    }

    private ArrayList<ModelStringList> readData(int resource_id) throws JSONException {
        InputStream inputStream = getResources().openRawResource(resource_id);

        ArrayList<ModelStringList> items = new ArrayList<>();
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            ModelStringList model = new ModelStringList();
            model.title = object.optString("title");
            model.description = object.optString("description");
            model.activity = object.optString("activity");
            items.add(model);
        }

        return items;
    }

    class StringListAdapter extends BaseAdapter {
        private ArrayList<ModelStringList> data;

        public StringListAdapter() {
            data = new ArrayList<>();
        }

        public void setData(ArrayList<ModelStringList> _data) {
            data = _data;
        }

        public ArrayList<ModelStringList> getData() {
            return data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public ModelStringList getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder ;
            if (convertView == null) {
                convertView = View.inflate(mContext, android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();
                holder.title_view = (TextView) convertView.findViewById(android.R.id.text1);
                holder.description_view = (TextView) convertView.findViewById(android.R.id.text2);
                holder.title_view.setTypeface(null, Typeface.BOLD);
                holder.title_view.setTextSize(18);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title_view.setText(getItem(position).title);
            holder.description_view.setText(getItem(position).description);

            return convertView;
        }

        private class ViewHolder {
            public TextView title_view;
            public TextView description_view;
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_string_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
