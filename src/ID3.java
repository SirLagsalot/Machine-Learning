import java.util.ArrayList;

public class ID3 implements Classifier {

    private  ArrayList<Instance> trainingData, testData;

    public ID3(ArrayList<Instance> trainingData, ArrayList<Instance> testData) {

        this.trainingData = trainingData;
        this.testData = testData;
    }

    @Override
    public String classify() {

        String classification = "";

        return classification;
    }
}
