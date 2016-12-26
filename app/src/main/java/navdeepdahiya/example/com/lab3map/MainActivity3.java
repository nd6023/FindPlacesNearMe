package navdeepdahiya.example.com.lab3map;


/**
 * Created by navdeepdahiya on 7/29/16.
 */

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.FragmentTransaction;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ViewAnimator;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity3 extends SampleActivityBase {

    public static final String TAG = "MainActivity3";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;
    Button btnH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        btnH = (Button) findViewById(R.id.buttonHome);
        if (btnH != null) {
            btnH.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(getApplicationContext(),
                            MapsActivity.class);
                    // Sending user current geo location
                    i.putExtra("MSG", "HOME");

                    startActivity(i);

                }
            });
        }
    }





    /** Create a chain of targets that will receive log data */

}
