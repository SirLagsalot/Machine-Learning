
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Reader {

    //do you have a big data file that needs reading?
    //well do i have a solution for you!
    public static DataSet readFile(String fileName) {

        ArrayList<String> file = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(file::add);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        DataSet data = process(file);

        return data;
    }

    public static DataSet process(ArrayList<String> lines) {
        DataSet data = new DataSet();
        Instance current = new Instance();
        String temp = lines.get(0);

        System.out.println(temp);

        int attributes = 1;
        while (temp.contains(",")) {
            temp = temp.substring(temp.indexOf(",") + 1);
            attributes++;
        }

        System.out.println(attributes);

        int classificationIndex = findClassification(lines.get(0), attributes);
        System.out.println(classificationIndex);
        for (String s : lines) {
            
            s = s + ", ";
            
            current.classification = classificationIndex;
            int counter = 0;

            while (s.contains(",")) {
                if(isNumeric(s.substring(0, s.indexOf(",")))){
                    current.unbinnedFeatures.add(Double.parseDouble(s.substring(0, s.indexOf(","))));
                }
                else if(checkClassifications(data, s.substring(0, s.indexOf(","))) == -1){
                    current.unbinnedFeatures.add((double)counter);
                    counter++;
                    data.map.classifications.add(s.substring(0, s.indexOf(",")));
                }
                else{
                    current.unbinnedFeatures.add((double)checkClassifications(data, s.substring(0, s.indexOf(","))));
                }
                s = s.substring(s.indexOf(",") + 1);
            }
            data.data.add(current);
            current = new Instance();
        }

        for (Double d : data.data.get(0).unbinnedFeatures) {
            System.out.print(d + " ");
        }

        return data;
    }
    
    public static int checkClassifications(DataSet d, String s){
        for(int i = 0; i < d.map.classifications.size(); i++){
            if(d.map.classifications.get(i).equals(s)){
                return i;
            }
        }
        return -1;
    }

    public static int findClassification(String instance, int max) {
        if (!isNumeric(instance.substring(0, instance.indexOf(",")))) {
            return 0;
        } else if (!isNumeric(instance.substring(instance.lastIndexOf(",") + 1, instance.length()))) {
            return max;
        } else {
            return 0;
        }
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
}
