package heath.android.sample.map.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by heath on 2016/1/5.
 */
public class Case implements ClusterItem {
    private LatLng position;
    public String building_name;
    public String address;
    public double latitude;
    public double longitude;

    public Case(String _building_name, String _address, double _lat, double _lng) {
        building_name = _building_name;
        address = _address;
        latitude = _lat;
        longitude = _lng;
        position = new LatLng(_lat, _lng);
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
