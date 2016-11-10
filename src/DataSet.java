
import java.util.ArrayList;
import java.util.HashMap;

public class DataSet {

    public ArrayList<Integer> data;
    Mapper map;

    public class Mapper{
        public String[] classifications;
        public String getClassification(int classification){
            return classifications[classification];
        }
    }

}
