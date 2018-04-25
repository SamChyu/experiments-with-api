package com.example.samchyu.experiementingwithmtd;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    public static double currentLat = 80;
    public static double currentLong = 80;

    public static String json;
    public static String json1;

    public static String[][] stopData;
    public static String[][] poiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);
        final Button btn1 = findViewById(R.id.updateStop);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "update button has been clicked");
                getStops();
                getPOI();
            }
        });
    }


    /**
     * make api call to MTD
     */
    void getStops() {
        String url = "https://developer.cumtd.com/api/v2.2/json/getstops?key=a13800cdc807401b9fcc4abbda713477";
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            json = response.toString();
                            stopData = parseAllStops();
                            Log.d(TAG, "stopdata populated");

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String[][] parseAllStops() {
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        JsonArray stops = result.get("stops").getAsJsonArray();
        int stopCounter = 0;
        for (int i = 0; i < stops.size(); i++) {
            JsonObject individualStop = stops.get(i).getAsJsonObject();
            JsonArray stop_points = individualStop.get("stop_points").getAsJsonArray();
            for (int j = 0; j < stop_points.size(); j++) {
                stopCounter++;
            }
        }
        String[][] output = new String[stopCounter][4];
        for (int i = 0; i < stops.size(); i++) {
            JsonObject individualStop = stops.get(i).getAsJsonObject();
            JsonArray stop_points = individualStop.get("stop_points").getAsJsonArray();
            for (int j = 0; j < stop_points.size(); j++) {
                JsonObject individualStopPoint = stop_points.get(j).getAsJsonObject();
                String individualStopPointStopName = individualStopPoint.get("stop_name").getAsString();
                double lat = individualStopPoint.get("stop_lat").getAsDouble();
                double lon = individualStopPoint.get("stop_lon").getAsDouble();
                String code = individualStopPoint.get("code").getAsString();
                output[i][0] = individualStopPointStopName;
                output[i][1] = String.valueOf(lat);
                output[i][2] = String.valueOf(lon);
                output[i][3] = code;
            }
        }
        return output;
    }

    void getPOI() {
        String url = "https://data.urbanaillinois.us/resource/9p4k-dr6i.json";
        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            json1 = response.toString();
                            poiData = parseAllPOI();
                            Log.d(TAG, "poi populated");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String[][] parseAllPOI() {
        JsonParser parser = new JsonParser();
        JsonArray result = parser.parse(json1).getAsJsonArray();
        String[][] poi = new String[result.size()][4];
        for (int i =0; i < result.size(); i++) {
            JsonObject individualPOI = result.get(i).getAsJsonObject();
            String resourceName = individualPOI.get("resource_name").getAsString();
            String resourceType = individualPOI.get("resource_type").getAsString();
            JsonObject location = individualPOI.get("location_1").getAsJsonObject();
            JsonArray coordinates = location.get("coordinates").getAsJsonArray();
            double lat = coordinates.get(0).getAsDouble();
            double lon = coordinates.get(1).getAsDouble();
            poi[i][0] = resourceName;
            poi[i][1] = String.valueOf(lat);
            poi[i][2] = String.valueOf(lon);
            poi[i][3] = resourceType;
        }
        return poi;
    }
}
