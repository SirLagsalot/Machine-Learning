
import java.util.ArrayList;

//the classifier interface which machine learning algorithms implements
//to provide consisent form for classification.  
//Parameters: An array list of features describing a test instance
//Outputs: a numeric classification of that vector
public interface Classifier {

    default int classify(ArrayList<Integer> featureVector) {
        return -1;
    }
}
