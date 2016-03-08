package finanzas.col.gob.mx.adeudo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import finanzas.col.gob.mx.adeudo.DB.DBContract;
import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;

public class MapaReceptoria extends FragmentActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private DBHelper mydb ;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private Marker singleMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_receptoria);

        mydb = new DBHelper(getBaseContext());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

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

         Toast.makeText(getApplicationContext(), "El vehículo no se encuentra activo. Acuda a la receptoría más cercana para aclarar la situación del vehículo", Toast.LENGTH_LONG).show(); // Update the UI

        mMap = googleMap;

        ArrayList<HashMap<String, String>> Puntos ;


        Puntos = mydb.GetReceptoria();

        LatLng punto;
        for(int i = 0; i < Puntos.size(); i++){
            // Add a marker in Sydney and move the camera
            punto = new LatLng(Double.parseDouble(Puntos.get(i).get(DBContract.Bancos.COLUMN_LATITUD)), Double.parseDouble(Puntos.get(i).get(DBContract.Bancos.COLUMN_LONGITUD)));
            mMap.addMarker(new MarkerOptions().position(punto).title(Puntos.get(i).get(DBContract.Bancos.COLUMN_NOMBRE)).snippet(Puntos.get(i).get(DBContract.Bancos.COLUMN_DIRECCION)));

        }

        if (mLastLocation != null) {
            punto = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            singleMarker = mMap.addMarker(new MarkerOptions().position(punto).title("Ubicación Atual").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 15));
        }
        else {
            punto = new LatLng(19.2432021, -103.7298157); // colima centro
            singleMarker = mMap.addMarker(new MarkerOptions().position(punto).title("Ubicación Atual").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 10));
        }


        //snippet multilinea
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v =  View.inflate(getBaseContext(), R.layout.marker, null);
                TextView info = (TextView) v.findViewById(R.id.info);
                if (marker.getSnippet() != null)
                    info.setText(marker.getTitle() + "\n" + marker.getSnippet());
                else
                    info.setText(marker.getTitle());
                return v;
            }

        });

        createLocationRequest();
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();


        LocationManager service = (LocationManager) getBaseContext().getSystemService(this.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Habilitar GPS para tener una localización mas precisa");

            alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }


    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }





    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {

        singleMarker.remove();
        LatLng  punto = new LatLng(location.getLatitude(), location.getLongitude());
        singleMarker = mMap.addMarker(new MarkerOptions().position(punto).title("Ubicación Atual").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 16));
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.

    }

    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

}
