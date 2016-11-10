
import java.util.ArrayList;

public class ID3 implements Classifier {

    private ArrayList<Instance> trainingData, testData;

    public ID3(ArrayList<Instance> trainingData, ArrayList<Instance> testData) {

        this.trainingData = trainingData;
        this.testData = testData;
    }

    @Override
    public void train(ArrayList<Instance> trainingSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {

        return -1;
    }
}
