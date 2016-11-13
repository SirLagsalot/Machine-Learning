
import java.util.ArrayList;

public class DataSet {

    public ArrayList<Integer> data;
    public Mapper map;

    public class Mapper {

        public String[] classifications;

        public String getClassification(int classification) {
            return classifications[classification];
        }
    }

}
