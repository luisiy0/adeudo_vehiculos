package finanzas.col.gob.mx.adeudo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import finanzas.col.gob.mx.adeudo.DB.DBContract;
import finanzas.col.gob.mx.adeudo.DB.DBHelper;
import finanzas.col.gob.mx.adeudo.Utilities.Utils;

/**
 * Created by lnunez on 30/12/2015.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
                                                    LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    private MapView m;
    private DBHelper mydb ;
    private Marker singleMarker;
    private View rootView;
    private LocationRequest mLocationRequest;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;


    public MapFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mydb = new DBHelper(getContext());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if(getArguments().getString("Receptoria") == null)
            getActivity().setTitle(getArguments().getString(Utils.Banco));
        m = (MapView) rootView.findViewById(R.id.map);
        m.onCreate(savedInstanceState);
        m.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<HashMap<String, String>> Puntos ;


       Puntos = mydb.GetBanco(getArguments().getString(Utils.Banco));

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
                View v = rootView.inflate(getContext(), R.layout.marker, null);
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
        m.onResume();


        LocationManager service = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
        m.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        m.onLowMemory();
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
