package eco.org.greenapp.eco.org.greenapp.maps;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danan on 4/28/2018.
 */

public class JSONMaps {
public HashMap<Integer,List<List<HashMap<String,String>>>> parse(JSONArray jObject) {

    HashMap<Integer,List<List<HashMap<String, String>>>> listaaa = new HashMap<>();
    List<List<HashMap<String, String>>> routes = new ArrayList<>();
    JSONObject mapsDistance = null;
    JSONObject mapsDuration = null;
    JSONArray mapsRoutes = null;
    JSONArray mapsLegs = null;
    JSONArray mapsSteps = null;
    JSONObject obj = null;
    for(int q=0;q<jObject.length();q++){
    try {
        obj = jObject.getJSONObject(q);
          routes = new ArrayList<List<HashMap<String, String>>>();
        mapsRoutes = obj.getJSONArray("routes");
        for (int i = 0; i < mapsRoutes.length(); i++) {
            mapsLegs = ((JSONObject) mapsRoutes.get(i)).getJSONArray("legs");

            List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

            for (int j = 0; j < mapsLegs.length(); j++) {

                mapsDuration = ((JSONObject) mapsLegs.get(j)).getJSONObject("duration");
                HashMap<String, String> hmDuration = new HashMap<String, String>();
                hmDuration.put("duration", mapsDuration.getString("text"));
                path.add(hmDuration);

                 mapsDistance = ((JSONObject) mapsLegs.get(j)).getJSONObject("distance");
                Log.i("111", "disss "+mapsDistance);
                HashMap<String, String> hmDistance = new HashMap<String, String>();
                hmDistance.put("distance", mapsDistance.getString("text"));
                path.add(hmDistance);
                mapsSteps = ((JSONObject) mapsLegs.get(j)).getJSONArray("steps");

                 for (int k = 0; k < mapsSteps.length(); k++) {
                    String polyline = "";
                    polyline = (String) ((JSONObject) ((JSONObject) mapsSteps.get(k)).get("polyline")).get("points");
                    List<LatLng> list = decodePoly(polyline);

                }
            }

            routes.add(path);
        }
        listaaa.put(q,routes);
    } catch (JSONException e) {
        e.printStackTrace();
    } catch (Exception e) {
    }
}
    return listaaa;
}

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
