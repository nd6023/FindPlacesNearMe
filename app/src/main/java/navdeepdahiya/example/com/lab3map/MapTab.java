package navdeepdahiya.example.com.lab3map;


        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.Snackbar;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
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


        import java.io.IOException;
        import java.util.List;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
public class MapTab extends AppCompatActivity {
    //public class MapsActivity extends Activity {
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
    private CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button btnHome;
    Button btnRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(), "MapView");
        viewPagerAdapter.addFragments(new RecFragment(), "Details");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        btnHome = (Button) findViewById(R.id.buttonH);

        btnHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        MapsActivity.class);
                // Sending user current geo location
                i.putExtra("MSG", "HOME");

                setResult(RESULT_OK, i);
                finish();
            }
        });

        btnRV = (Button) findViewById(R.id.buttonRV);

        if (btnRV != null) {
            btnRV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                        intent.putExtra("searchText", loc);

                        //Toast.makeText(getBaseContext(), "MMMM Text searched Map NDND !!!  " + loc, Toast.LENGTH_LONG).show();
                    startActivity(intent);

                }
            });
        }

        //Intent intent = new Intent(getApplicationContext(), MapSecond.class);
        //startActivityForResult(intent, 50);
        //Snackbar.make(coordinatorLayout, "Had a snack at Snackbar", Snackbar.LENGTH_LONG);
        /*Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Started the application", Snackbar.LENGTH_LONG);
        snackbar.show();*/

    }
}
