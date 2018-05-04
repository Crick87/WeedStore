package com.python.cricket.weedstore;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback ,
        LocationListener,
        GoogleMap.OnMapClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    boolean bandGPS  = false;
    boolean bandRED = false;
    double latActual, lonActual;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        getGeoLocation();
        LatLng myUbication = new LatLng( latActual , lonActual);
        mMap.addMarker(new MarkerOptions().position(myUbication).title("Cliente").icon(BitmapDescriptorFactory.fromResource(R.drawable.marky)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myUbication));
    }

    private void getGeoLocation()
    {
        Location myUbication;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        bandGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        bandRED = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        try{
            if(bandGPS)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,10,this);
                myUbication = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latActual = myUbication.getLatitude();
                lonActual = myUbication.getLongitude();
                Toast.makeText(this,"Posición GPS exitosa "+latActual+":"+lonActual,Toast.LENGTH_SHORT).show();
            }else{
                if(bandRED)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,10,this);
                    myUbication = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    latActual = myUbication.getLatitude();
                    lonActual = myUbication.getLongitude();
                    Toast.makeText(this,"Posición Red exitosa "+latActual+":"+lonActual, Toast.LENGTH_SHORT).show();
                }
            }

        }catch (SecurityException se){

        }
    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
