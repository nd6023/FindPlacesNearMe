package navdeepdahiya.example.com.lab3map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

        import android.content.Intent;
        import android.location.Location;
        import android.location.LocationListener;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;

        import com.google.android.gms.location.places.Place;
        import com.google.android.gms.location.places.ui.PlacePicker;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.location.Address;
        import android.widget.TextView;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Context;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.widget.TextView;
        import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
        import java.util.List;


/**
 * Created by navdeepdahiya on 7/26/16.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    EditText location;
    Button btnSearch;
    String loc;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    List<Address> addressList;
    Address address;
    EditText location_tf;

    // ND ++

    PositionQueue nearPlaces;

    double latitudeD;
    double longitudeD;
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView mName;
    private TextView mAddress;
    private TextView mAttributions;
    SupportMapFragment mapFragment;

    // ND --

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapnd);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        Button btnN = (Button) findViewById(R.id.buttonN);

        //List<Address> addressList = null;
        if (btnN != null) {
            btnN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    location_tf = (EditText) findViewById(R.id.editTextSearchnd);
                    loc = location_tf.getText().toString();
                    if (loc != null || !loc.equals("")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                        intent.putExtra("searchText", loc);

                        //Toast.makeText(getBaseContext(), "MMMM Text searched Map NDND !!!  " + loc, Toast.LENGTH_LONG).show();
                        startActivityForResult(intent, 100);
                    }
                }
            });
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            //TextView textViewActFir = (TextView) findViewById(R.id.textViewActFir2);
           // TextView textViewOP = (TextView) findViewById(R.id.textViewOP);
            double lati  = data.getIntExtra("user_latitude", 0);
            double longi  = data.getIntExtra("user_longitude", 0);
            nearPlaces = (PositionQueue) data.getSerializableExtra("near_places");

            //textViewActFir.setText(grade + " grade is equal to "+score+"%.");
            //Toast.makeText(getBaseContext(), "God is great!!!",Toast.LENGTH_LONG).show();


            //Toast.makeText(getBaseContext(), "Got the data NDNDND!!!", Toast.LENGTH_LONG).show();

            LatLng latLng=null;


            latLng = new LatLng(lati, longi);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(latLng , 14.0f) );

            if (nearPlaces.results != null) {
                // loop through all the places
                for (Position position : nearPlaces.results) {
                    latitudeD = (position.geometry.location.lat); // latitude
                    longitudeD = (position.geometry.location.lng); // longitude

                    latLng = new LatLng(latitudeD, longitudeD);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(position.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                }
            }

            //textViewOP.setText(name + " grade is equal to "+pass+"%.");
            //finish();

        }
        if ((requestCode == 200) && (resultCode == RESULT_OK)) {
            Toast.makeText(getBaseContext(), "Home Page", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        //txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

        /*Location myLocation = mMap.getMyLocation();
        LatLng l1 = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(l1).title("Current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(l1));*/
    }

    public void onSearch(View view) {

        location_tf = (EditText) findViewById(R.id.editTextSearchnd);
        //String location = location_tf.getText().toString();
        loc = location_tf.getText().toString();
        //List<Address> addressList = null;
        if (loc != null || !loc.equals("")) {

            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(loc, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressList.get(0);
            System.out.println(address.getFeatureName());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());


            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));



/*
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent intent;

            try {
                intent = builder.build(getApplicationContext());
                startActivityForResult(intent,100);

            }
            catch (Exception e){
                e.printStackTrace();
            }*/
        }
    }
    /*
        protected void onActivityResult(int requestCode, int resultcode, Intent data ){
            if(requestCode==100){
                if(resultcode==RESULT_OK){
                    Place place = PlacePicker.getPlace(data,context);
                    String address = String.format("Place:  %s", place.getAddress());
                    location_tf.setText(address);
                }
            }
        }*/
    @Override
    public void onLocationChanged(Location location) {
        // txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        LatLng l1 = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(l1).title("Current location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(l1));
        mMap.addMarker(new MarkerOptions().position(l1).title("Current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(l1 , 14.0f) );

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }


    public void tabV(View view) {

        Intent intent = new Intent(getApplicationContext(), MapTab.class);
        startActivityForResult(intent, 200);
    }


}
