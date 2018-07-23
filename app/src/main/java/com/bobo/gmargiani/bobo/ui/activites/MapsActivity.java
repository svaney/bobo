package com.bobo.gmargiani.bobo.ui.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.bobo.gmargiani.bobo.R;
import com.bobo.gmargiani.bobo.utils.AppConsts;
import com.bobo.gmargiani.bobo.utils.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;

    public static final int TYPE_ADD_LOCATION = 10;
    public static final int TYPE_SHOW_LOCATION = 20;

    private GoogleMap mMap;

    private View customMarker;
    private View fabButton;

    private LatLng tbilisi = new LatLng(41.69, 44.80);

    private LocationManager mLocationManager;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocationManager.removeUpdates(mLocationListener);
            } else {

            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public static void start(Activity activity, int operationType) {
        start(activity, operationType, -1000, -1000);
    }

    public static void start(Activity activity, int operationType, double lat, double lng) {
        Intent intent = new Intent(activity, MapsActivity.class);
        intent.putExtra(AppConsts.PARAM_MAP_OPERATION_TYPE, operationType);
        intent.putExtra(AppConsts.PARAM_MAP_LAT, lat);
        intent.putExtra(AppConsts.PARAM_MAP_LNG, lng);
        activity.startActivityForResult(intent, AppConsts.RC_MAP);
    }

    private int operationType;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        operationType = getIntent().getIntExtra(AppConsts.PARAM_MAP_OPERATION_TYPE, TYPE_ADD_LOCATION);
        lat = getIntent().getDoubleExtra(AppConsts.PARAM_MAP_LAT, -1000);
        lng = getIntent().getDoubleExtra(AppConsts.PARAM_MAP_LNG, -1000);

        customMarker = findViewById(R.id.ic_marker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                if (userLocation != null) {
                    resultIntent.putExtra(AppConsts.PARAM_MAP_LNG, userLocation.longitude);
                    resultIntent.putExtra(AppConsts.PARAM_MAP_LAT, userLocation.latitude);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        fabButton.setVisibility(operationType == TYPE_ADD_LOCATION ? View.VISIBLE : View.GONE);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        initMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConsts.PERMISSION_LOCATION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (lat != -1000 && lng != -1000) {
                        setUpMarker(new LatLng(lat, lng));
                    } else {
                        setUpMarker(tbilisi, 12f);
                    }
                    return;
                }
            }
            initMap();
        }
    }


    @SuppressLint({"NewApi", "MissingPermission"})
    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            finish();
        } else {
            if (mMap != null) {
                if (!AppUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        || !AppUtils.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, AppConsts.PERMISSION_LOCATION);
                } else {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    setUpMarker(null);
                }
            }
        }
    }

    private void setUpMarker(LatLng latLng) {
        setUpMarker(latLng, 15.0f);
    }

    private void setUpMarker(LatLng latLng, float zoom) {
        if (operationType == TYPE_SHOW_LOCATION) {
            LatLng position = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
            mMap.addMarker(new MarkerOptions().position(position));
        } else {
            customMarker.setVisibility(View.VISIBLE);
            if (latLng == null) {
                getCurrentLocation();
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
                listenToCamera();
            }

        }
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (isGPSEnabled || isNetworkEnabled) {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (lat != -1000 && lng != -1000) {
            setUpMarker(new LatLng(lat, lng));
            listenToCamera();
        } else if (location != null) {
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 14));
            listenToCamera();
        } else {
            setUpMarker(tbilisi, 12f);
        }
    }

    LatLng userLocation;

    private void listenToCamera() {
        if (mMap != null) {
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    userLocation = mMap.getCameraPosition().target;
                }
            });
        }

    }
}