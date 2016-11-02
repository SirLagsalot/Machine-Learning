import java.util.ArrayList;

public class NaiveBayes implements Classifier {

    private  ArrayList<Instance> trainingData, testData;

    public NaiveBayes(ArrayList<Instance> trainingData, ArrayList<Instance> testData) {

        this.trainingData = trainingData;
        this.testData = testData;
    }



    @Override
    public String classify() {

        String classification = "";

        return classification;
    }
}
