package com.ururu2909.findfriends.room;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;
import com.ururu2909.findfriends.util.MyClusterManagerRenderer;
import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.models.ClusterMarker;
import com.ururu2909.findfriends.models.User;
import com.ururu2909.findfriends.services.LocationService;
import com.ururu2909.findfriends.util.SharedPreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.ururu2909.findfriends.util.Constants.ERROR_DIALOG_REQUEST;
import static com.ururu2909.findfriends.util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.ururu2909.findfriends.util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class RoomActivity extends AppCompatActivity implements RoomView, OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";
    ProgressBar progressBar;
    RoomPresenter presenter;
    SharedPreferences mSettings;
    private boolean mLocationPermissionGranted = false;
    MapView mMapView;
    private GoogleMap mGoogleMap;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mMapView = findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);

        progressBar = findViewById(R.id.progressBar);
        mSettings = SharedPreferencesManager.getInstance(this);
        presenter = new RoomPresenter(this, new RoomInteractor(), mSettings);

        checkMapServices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_leave_room) {
            presenter.leaveRoom();
        }
        return true;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setError() {
        Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();
    }

    @Override
    public void closeRoomActivity() {
        finish();
    }

    private void checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                if (mLocationPermissionGranted){
                    startLocationService();
                } else {
                    getLocationPermission();
                }
            }
        }
    }

    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(RoomActivity.this);

        if(available == ConnectionResult.SUCCESS){
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(RoomActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void startLocationService(){
        Intent serviceIntent = new Intent(this, LocationService.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            RoomActivity.this.startForegroundService(serviceIntent);
        }else{
            startService(serviceIntent);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
    }

    public void setCameraView(double latitude, double longitude) {
        double bottomBoundary = latitude - .1;
        double leftBoundary = longitude - .1;
        double topBoundary = latitude + .1;
        double rightBoundary = longitude + .1;

        LatLngBounds mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
    }

    public void addMapMarkers(ArrayList<User> users){
        if(mGoogleMap != null){

            if(mClusterManager == null){
                mClusterManager = new ClusterManager<>(this.getApplicationContext(), mGoogleMap);
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        this,
                        mGoogleMap,
                        mClusterManager);
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for(User user: users){
                try{
                    String snippet = user.getLogin();
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(user.getLatitude(), user.getLongitude()),
                            user.getLogin(),
                            snippet,
                            user.getAvatar());
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);
                }catch (NullPointerException e){
                    Log.e("xxx", "addMapMarkers: NullPointerException: " + e.getMessage() );
                }
            }
            mClusterManager.cluster();
        }
    }

    public void updateMapMarkers(ArrayList<User> users){
        boolean userClusterMarkerUpdated;
        for (User user : users) {
            userClusterMarkerUpdated = false;
            for (int i = 0; i < mClusterMarkers.size(); i++) {
                if (user.getLogin().equals(mClusterMarkers.get(i).getTitle())) {
                    LatLng updatedLatLng = new LatLng(
                            user.getLatitude(),
                            user.getLongitude()
                    );

                    mClusterMarkers.get(i).setPosition(updatedLatLng);
                    mClusterManagerRenderer.setUpdateMarker(mClusterMarkers.get(i));
                    userClusterMarkerUpdated = true;
                }
            }
            if (!userClusterMarkerUpdated){
                try{
                    String snippet = user.getLogin();

                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(user.getLatitude(), user.getLongitude()),
                            user.getLogin(),
                            snippet,
                            user.getAvatar());
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);
                    mClusterManagerRenderer.setUpdateMarker(newClusterMarker);
                }catch (NullPointerException e){
                    Log.e("xxx", "addMapMarkers: NullPointerException: " + e.getMessage() );
                }
                mClusterManager.cluster();
            }
        }

        boolean markerIsRelevant;
        for (int i = 0; i < mClusterMarkers.size(); i++) {
            markerIsRelevant = false;
            for (User user : users) {
                if (mClusterMarkers.get(i).getTitle().equals(user.getLogin())) {
                    markerIsRelevant = true;
                }
            }
            if (!markerIsRelevant){
                mClusterManager.removeItem(mClusterMarkers.get(i));
                mClusterMarkers.remove(i);
                mClusterManager.cluster();
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            startLocationService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                startLocationService();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (mLocationPermissionGranted) {
                startLocationService();
            } else {
                getLocationPermission();
            }
        }
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        stopService(new Intent(this, LocationService.class));
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
