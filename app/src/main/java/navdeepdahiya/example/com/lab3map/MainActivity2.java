package navdeepdahiya.example.com.lab3map;

/**
 * Created by navdeepdahiya on 7/19/2016.
 */
        import java.util.ArrayList;
        import java.util.HashMap;

        import android.annotation.TargetApi;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.Snackbar;
        import android.text.Html;
        import android.util.Log;
        import android.view.Menu;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.Button;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.Manifest;
        import android.widget.Toast;

public class MainActivity2 extends Activity {

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    public static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private TextView internet;
    // Connection detector class
    ChkConn cd ;

    // Alert Dialog Manager
    MngAlert alert = new MngAlert();

    // Google Places
    PositionAPI googlePlaces;

    // Places List
    PositionQueue nearPlaces;

    // GPS Location
    PositionSystem gps;

    // Button
    Button btnShowOnMap;

    // Progress dialog
    ProgressDialog pDialog;

    // Places Listview
    ListView lv;

    // ListItems data
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();

    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    String searchText;
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Position area name

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        snackbar = Snackbar
                .make(coordinatorLayout, "Nearby required places!!!", Snackbar.LENGTH_LONG);

        snackbar.show();
        searchText = getIntent().getStringExtra("searchText");
        //Toast.makeText(getBaseContext(), "Text searched NDND !!!  "+searchText, Toast.LENGTH_LONG).show();
        cd = new ChkConn(getApplicationContext());

        //Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            /*alert.showAlertDialog(MainActivity2.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false); */
            snackbar = Snackbar
                    .make(coordinatorLayout, "Internet Connection Error, Please connect to working Internet connection", Snackbar.LENGTH_LONG);

            snackbar.show();
            // stop executing code by return
            return;
        }


        if (!canAccessFineLocation() || !canAccessCoarseLocation() ) {
            requestPermissions(INITIAL_PERMS, 1337);
        }
        // creating GPS Class object
        gps = new PositionSystem(this);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location NDNDND899", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            //Toast.makeText(getBaseContext(), "Record successfully inserted!!!"+"latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            // Can't get user's current location
            /*alert.showAlertDialog(MainActivity2.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);*/
            snackbar = Snackbar
                    .make(coordinatorLayout, "GPS Status: Couldn't get location information. Please enable GPS", Snackbar.LENGTH_LONG);

            snackbar.show();
            // stop executing code by return
            return;
        }

        // Getting listview
        lv = (ListView) findViewById(R.id.list);

        // button show on map
        btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
        new LoadPlaces().execute();

        /** Button click event for shown on map */







        btnShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        MapsActivity.class);
                // Sending user current geo location
                i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
                i.putExtra("user_longitude", Double.toString(gps.getLongitude()));

                // passing near places to map activity
                i.putExtra("near_places", nearPlaces);
                setResult(RESULT_OK, i);
                finish();
            }
        });


        /**
         * ListItem click event
         * On selecting a listitem PositionOne is launched
         * */
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
               // Toast.makeText(getBaseContext(), reference, Toast.LENGTH_LONG).show();


                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        PositionOne.class);

                // Sending place refrence id to single place activity
                // place refrence id used to get "Position full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });


    }


    public boolean canAccessCoarseLocation() {
        return(hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    public boolean canAccessFineLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    public boolean canAccessInternet(){
        return(hasPermission(Manifest.permission.INTERNET));
    }

    public boolean hasPermission(String perm) {
        //return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
        return true;
    }

    /**
     * Background Async Task to Load Google places
     * */
    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity2.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new PositionAPI();

            try {
                // Separeate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                //
                String types = searchText; //

                // Radius in meters - increase this value if you don't find any places
                double radius = 1000; // 1000 meters

                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;

                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Position p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Position reference won't display in listview - it will be hidden
                                // Position reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.reference);

                                // Position name
                                map.put(KEY_NAME, p.name);


                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                            // list adapter
                            ListAdapter adapter = new SimpleAdapter(MainActivity2.this, placesListItems,
                                    R.layout.list_item,
                                    new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                                    R.id.reference, R.id.name });

                            // Adding data into listview
                            lv.setAdapter(adapter);
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                       /* alert.showAlertDialog(MainActivity2.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);*/
                        snackbar = Snackbar
                                .make(coordinatorLayout, "GPS Status: Couldn't get location information. Please enable GPS", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        /*alert.showAlertDialog(MainActivity2.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);*/
                        snackbar = Snackbar
                                .make(coordinatorLayout, "Places Error: Sorry unknown error occured.", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        /*alert.showAlertDialog(MainActivity2.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);*/
                        snackbar = Snackbar
                                .make(coordinatorLayout, "Places Error: Sorry query limit to google places is reached", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        /*alert.showAlertDialog(MainActivity2.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);*/
                        snackbar = Snackbar
                                .make(coordinatorLayout, "Places Error: Sorry error occured. Request is denied", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        /*salert.showAlertDialog(MainActivity2.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);*/
                        snackbar = Snackbar
                                .make(coordinatorLayout, "Places Error: Sorry error occured. Invalid Request", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else
                    {
                        /*alert.showAlertDialog(MainActivity2.this, "Places Error",
                                "Sorry error occured.",
                                false);*/
                        snackbar = Snackbar
                                .make(coordinatorLayout, "Places Error: Sorry error occured", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }



}