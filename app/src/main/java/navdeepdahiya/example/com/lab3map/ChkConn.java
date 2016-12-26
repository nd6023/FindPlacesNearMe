package navdeepdahiya.example.com.lab3map;

/**
 * Created by navdeepdahiya on 7/23/2016.
 */

        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;

public class ChkConn {

    private Context _context;

    public ChkConn(Context context){
        this._context = context;
    }

    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
