
import java.util.ArrayList;

public interface Classifier {

    //Classify vector
    default int classify(ArrayList<Integer> featureVector) {
        return -1;
    }
}
