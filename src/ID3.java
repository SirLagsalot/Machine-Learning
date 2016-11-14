
import java.util.ArrayList;

public class ID3 implements Classifier {

    private final ArrayList<Instance> trainingData;

    public ID3(ArrayList<Instance> trainingData) {

        this.trainingData = trainingData;
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {

        return -1;
    }
}
