package com.example.samchyu.experiementingwithmtd;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements OnMapReadyCallback{


    public BlankFragment() {
        // Required empty public constructor
    }

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View map = inflater.inflate(R.layout.fragment_blank, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            transaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng busStop = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(busStop).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(busStop));

    }

        public void setMapType(int type) {
        mMap.setMapType(type);
        }
}
