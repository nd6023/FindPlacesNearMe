package navdeepdahiya.example.com.lab3map;

import com.google.api.client.util.Key;

import java.io.Serializable;


/**
 * Created by navdeepdahiya on 7/18/2016.
 */


        import java.io.Serializable;

        import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class PositionData implements Serializable {

    @Key
    public String status;

    @Key
    public Position result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}