
import java.util.ArrayList;

public interface Classifier {

    String classify(ArrayList<Instance> data, Instance test);
}
