

/*
    Something about DataLines?
    I'm not quite sure how we want to implement this, my initial thought
    wasn't congruent with what your section in the design doc said, so...
    Maybe we can use this class to connect the two methods
 */


import java.util.HashMap;

public class Feature {

    protected String value;
    protected HashMap data;

    public Feature(String value) {

        this.value = value;
    }
}
