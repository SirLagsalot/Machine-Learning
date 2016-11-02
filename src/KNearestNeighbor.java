import java.util.ArrayList;

public class KNearestNeighbor implements Classifier {

    public static int k = 3;
    private ArrayList<Instance> trainingData, testData;

    public KNearestNeighbor(ArrayList<Instance> trainingData, ArrayList<Instance> testData, int k) {

        this.trainingData = trainingData;
        this.testData = testData;
        this.k = k;
    }

    @Override
    public String classify() {

        String classification = "";

        return classification;
    }
}
