
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {   
        DataSet data = Reader.readFile("./data/glass.data.txt");
        //ArrayList<Instance> dataInstances = Sanitizer.sanitize(data);
        //Instance test = new Instance("3", "1", "1", "3");

        //split into training and test sets
        ArrayList<Instance> trainingData = new ArrayList<>();
        ArrayList<Instance> testData = new ArrayList<>();

        //run classifiers
//        Classifier classifier = new KNearestNeighbor(trainingData, testData, 3);
//        String KNNClassification = classifier.classify();
//
//        classifier = new NaiveBayes(trainingData, testData);
//        String NaiveBayesClassification = classifier.classify();
//
//        classifier = new TAN(trainingData, testData);
//        String TANClassification = classifier.classify();
//
//        classifier = new ID3(trainingData, testData);
//        String ID3Classification = classifier.classify();
//
//        System.out.println("k-Nearest Neighbor: " + KNNClassification);
//        System.out.println("Naive Bayes: " + NaiveBayesClassification);
//        System.out.println("Tree Augmented Naive Bayes: " + TANClassification);
//        System.out.println("Iterative Dichotomiser 3: " + ID3Classification);
    }
}
