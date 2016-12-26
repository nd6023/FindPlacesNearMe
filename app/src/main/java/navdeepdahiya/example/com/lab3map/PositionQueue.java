package navdeepdahiya.example.com.lab3map;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;


/**
 * Created by navdeepdahiya on 7/25/2016.
 */
        import java.io.Serializable;
        import java.util.List;

        import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class PositionQueue implements Serializable {

    @Key
    public String status;

    @Key
    public List<Position> results;

}