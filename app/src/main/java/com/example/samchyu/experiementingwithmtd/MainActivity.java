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
import android.widget.Button;

import org.json.JSONObject;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    public static double currentLat = 80;
    public static double currentLong = 80;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        TextView d1 = findViewById(R.id.dynamic1);
        TextView d2 = findViewById(R.id.dynamic2);
        final Button btn1 = findViewById(R.id.updateStop);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "update button has been clicked");
                updateNearestStop();
            }
        });
    }
    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLat = location.getLatitude();
            currentLong =location.getLongitude();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {

        }
    };



    /**
     * make api call to MTD
     */
    void updateNearestStop() {
        String url = "https://developer.cumtd.com/api/v2.2/json/getstopsbylatlon?key=a13800cdc807401b9fcc4abbda713477";
        String lat = "&lat=" + Double.toString(currentLat);
        String lon = "&lon=" + Double.toString(currentLong);
        String count = "&count=1";
        url = url + lat + lon + count;

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            TextView d2 = findViewById(R.id.dynamic2);
                            d2.setText(response.toString());
                            Log.d(TAG, response.toString());
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

}
