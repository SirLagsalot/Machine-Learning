
import java.util.ArrayList;

public class TAN extends NaiveBayes {


    public TAN(ArrayList<Instance> trainingData) {
        super(trainingData);
    }


    @Override
    public int classify(ArrayList<Integer> featureVector) {
        return -1;
    }

    @Override
    public void train(ArrayList<Instance> trainingSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
