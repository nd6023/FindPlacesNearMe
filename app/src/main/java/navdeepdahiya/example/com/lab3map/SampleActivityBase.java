package navdeepdahiya.example.com.lab3map;


/**
 * Created by navdeepdahiya on 7/29/16.
 */


        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;

/**
 * Base launcher activity, to handle most of the common plumbing for samples.
 */
public class SampleActivityBase extends FragmentActivity {

    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        //initializeLogging();
    }

    /** Set up targets to receive log data */

}
