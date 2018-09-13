package com.ssgmail.shubhammsoni.iesbuddy;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Directions extends FragmentActivity implements OnMapReadyCallback,CustomAdapterClickListener {

    private GoogleMap mMap;
RecyclerView recyclerView;
Marker now;
CustomAdapterClickListener clickListener = this;
Location_recycler_adapter adapter;
    ArrayList<Location_item_model> items= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        provideData();

        recyclerView=findViewById(R.id.rv_items);
        adapter=new Location_recycler_adapter(items,this,clickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    void provideData()
    {  LatLng enggBlock = new LatLng(22.656124,75.826578);
       LatLng IPS_MESS= new LatLng(22.654859,75.826094);
        LatLng STADIUM= new LatLng(22.654334,75.827107);
        LatLng Central_building= new LatLng(22.654912, 75.826058);
        LatLng parking= new LatLng(22.655538, 75.825701);
        LatLng canteen= new LatLng(22.655395, 75.826651);
        LatLng Digital_ion_zone= new LatLng(22.655626, 75.826610);
        LatLng placement_cell = new LatLng(22.655582, 75.826610);
        LatLng mechanical_dep= new LatLng(22.656209, 75.826120);



        items.add(new Location_item_model("IPS Mess","Food facility",IPS_MESS));
        items.add(new Location_item_model("Engineering Block","Institute of Engineering & Science",enggBlock));
        items.add(new Location_item_model("Central Building","Centeral Office",Central_building));
        items.add(new Location_item_model("Stadium","Play Ground",STADIUM));
        items.add(new Location_item_model("Digital ION Zone","Examination centre",Digital_ion_zone));
        items.add(new Location_item_model("Placement Cell","Job opportuinities",placement_cell));
        items.add(new Location_item_model("Canteen","Food facility",canteen));
        items.add(new Location_item_model("Parking","Parking LOT",parking));
        items.add(new Location_item_model("Mechanical Block","Mechanical Engineering",mechanical_dep));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng IPS = new LatLng(22.655292, 75.825108);
        now=mMap.addMarker(new MarkerOptions().position(IPS).title("IPS Academy"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(IPS,16));
    }

    @Override
    public void onItemClick(View view, int position) {

        animateMarker(now,items.get(position).getLatLng(),false);

    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
}
