
import java.util.ArrayList;


public interface Classifier {

    void train(ArrayList<Instance> trainingSet);
    
    default int classify(ArrayList<Integer> featureVector) {
        return -1;
    }
}
