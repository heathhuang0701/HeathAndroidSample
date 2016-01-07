package heath.android.sample.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import heath.android.sample.BaseActionBarActivity;
import heath.android.sample.R;
import heath.android.sample.map.model.Case;

public class ClusterMapActivity extends BaseActionBarActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<Case>, ClusterManager.OnClusterItemInfoWindowClickListener<Case> {
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";
    private Context mContext;
    private GoogleMap mMap;
    private ArrayList<Marker> markers = new ArrayList<>();
    private ClusterManager<Case> mClusterManager;
    private List<Case> cases = new ArrayList<>();
    private InfoWindowContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new CaseRenderer());
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

//        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForClusters());
//        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setOnCameraChangeListener(mClusterManager);

        try {
            cases = readMapData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("CAC", "cases count:" + cases.size());
        mClusterManager.addItems(cases);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Case _case : cases) {
            builder.include(_case.getPosition());
        }
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 480, 800, 50));
    }

    @Override
    public void onClusterItemInfoWindowClick(Case aCase) {
        Toast.makeText(this, aCase.building_name, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onClusterClick(Cluster<Case> cluster) {
        float zoom_level = mMap.getCameraPosition().zoom;
        Log.d("CAC", "zoom level:" + zoom_level);
        if (zoom_level > 15.5) {
            if (adapter == null) {
                adapter = new InfoWindowContentAdapter();
            }
            adapter.setData(cluster);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(cluster.getPosition()), 200, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("CAC", "which:" + which);
                                    Case _case = adapter.getItem(which);
                                }
                            })
                            .create();

                    dialog.show();
                }

                @Override
                public void onCancel() {

                }
            });

            return true;
        }

        return false;
    }

    private class CaseRenderer extends DefaultClusterRenderer<Case> {

        public CaseRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Case item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).title(item.building_name).snippet(item.address);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

//    public class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {
//
//        MyCustomAdapterForClusters() {}
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            if (clickedCluster != null) {
//                ListView listview = new ListView(mContext);
//                listview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
//                InfoWindowContentAdapter adapter = new InfoWindowContentAdapter(clickedCluster);
//                listview.setAdapter(adapter);
//                return listview;
//            }
//
//            return null;
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return null;
//        }
//    }

    private class InfoWindowContentAdapter extends BaseAdapter {
        private Cluster<Case> _cluster;

        public void setData(Cluster<Case> cluster) {
            _cluster = cluster;
        }

        @Override
        public int getCount() {
            return _cluster.getSize();
        }

        @Override
        public Case getItem(int position) {
            return (Case) _cluster.getItems().toArray()[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(mContext, android.R.layout.simple_list_item_2, null);
                holder = new ViewHolder();
                holder.title_view = (TextView) convertView.findViewById(android.R.id.text1);
                holder.description_view = (TextView) convertView.findViewById(android.R.id.text2);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title_view.setText(getItem(position).building_name);
            holder.description_view.setText(getItem(position).address);

            return convertView;
        }

        private class ViewHolder {
            public TextView title_view;
            public TextView description_view;
        }
    }

    private List<Case> readMapData() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.map_data);

        List<Case> items = new ArrayList<Case>();
        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String build_name = object.optString("BuildingName");
            String address = object.optString("Address");
            double lat = object.getDouble("Latitude");
            double lng = object.getDouble("Longitude");
            items.add(new Case(build_name, address,lat, lng));
        }
        return items;
    }
}
