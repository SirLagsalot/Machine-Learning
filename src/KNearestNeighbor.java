
import java.util.ArrayList;

public class KNearestNeighbor implements Classifier {

    private final int length;
    private final int k;
    private final ArrayList<Instance> trainingData;

    public KNearestNeighbor(ArrayList<Instance> trainingData, int k) {

        this.trainingData = trainingData;
        this.k = k;
        this.length = trainingData.get(0).features.size();
    }

    @Override
    public int classify(ArrayList<Integer> testFeatures) {

        int classification = -1;
        
        for (int i = 0; i < trainingData.size(); i++) {
            
            ArrayList<Integer> trainingFeatures = trainingData.get(i).features;
            assert trainingFeatures.size() == testFeatures.size();
            double distance = 0.0f;
            for (int j = 0; j < trainingFeatures.size(); j++) {
                int test = testFeatures.get(j);
                System.out.println(test);
                int train = trainingFeatures.get(j);
                System.out.println(train);
                distance += Math.pow(testFeatures.get(j) - trainingFeatures.get(j), 2);
            }
            trainingData.get(i).distance = Math.sqrt(distance);
        }
        
        
        
        
        
        
        
        
//
//        for (Integer in : trainingData.get(0).features) {
//            System.out.print(in + " ");
//        }
//        System.out.println("");
//        for (Integer in : featureVector) {
//            System.out.print(in + " ");
//        }
      //  System.out.println("");
//        //calculate distance to each instance in the training set
//        for (int i = 0; i < featureVector.size(); i++) {
//             int test = featureVector.get(i);
//             for (Instance trainInstance : trainingData) {
//                 int train = trainInstance.features
//             }
//       //     System.out.println("Instance: " + instance.features.size() + featureVector.size());
//            double distance = 0;
//            for (int j = 0; j < featureVector.size() - 1; j++) {
//                
//                    int testFeature = featureVector.get(j);
//                    System.out.print("testfeat: " + testFeature);
//                    int trainFeature = instance.features.get(0);
//                    System.out.println("    trainfeat: " + trainFeature);
//             //      System.out.println("instance.features.get(" + i + ") : " + instance.features.get(i));
//              //      System.out.println("i: " + i);
//                    distance += Math.pow(testFeature - trainFeature, 2);
////                } catch (Exception e) {
////                    System.out.println(e);
////                    System.exit(-1);
////                }
//
//            }
//            instance.distance = Math.sqrt(distance);
        

        //get the k smallest distances
        trainingData.sort((instance1, instance2) -> instance1.distance > instance2.distance ? 1 : -1);
        int[] kNearest = new int[k];
        for (int i = 0; i < k; i++) {
            kNearest[i] = trainingData.get(i).classification;
        }

        //get frequency of the k closest classifications
        int[] freq = new int[k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (kNearest[j] == kNearest[i]) {
                    freq[i]++;
                }
            }
        }

        //determine most frequent
        int mostFreq = -1;
        for (int i = 0; i < k; i++) {
            if (freq[i] > mostFreq) {
                classification = kNearest[i];
            }
        }
        return classification;
    }
}
