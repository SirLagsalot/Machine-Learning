
import java.util.ArrayList;

public interface Classifier {

    default int classify(ArrayList<Integer> featureVector) {
        return -1;
    }
}
