
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Reader {

    public static DataSet readFile(String fileName) {

        ArrayList<String> file = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(file::add);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        DataSet data = process(file);
        normalize(data.data);
        return data;
    }

    private static DataSet process(ArrayList<String> lines) {

        DataSet data = new DataSet();
        Instance current = new Instance();
        String temp = lines.get(0);

        int attributes = 1;
        while (temp.contains(",")) {
            temp = temp.substring(temp.indexOf(",") + 1);
            attributes++;
        }

        int classificationIndex = findClassification(lines.get(0), attributes);
        for (String s : lines) {

            s = s + ", ";

            current.classification = classificationIndex;
            int counter = 0;

            while (s.contains(",")) {
                if (isNumeric(s.substring(0, s.indexOf(",")))) {
                    current.unbinnedFeatures.add(Double.parseDouble(s.substring(0, s.indexOf(","))));
                } else if (checkClassifications(data, s.substring(0, s.indexOf(","))) == -1) {
                    current.unbinnedFeatures.add((double) counter);
                    counter++;
                    data.map.classifications.add(s.substring(0, s.indexOf(",")));
                } else {
                    current.unbinnedFeatures.add((double) checkClassifications(data, s.substring(0, s.indexOf(","))));
                }
                s = s.substring(s.indexOf(",") + 1);
            }
            data.data.add(current);
            current = new Instance();
        }

        for (Instance i : data.data) {
            for (int j = 0; j < i.unbinnedFeatures.size(); j++) {
                System.out.print(i.unbinnedFeatures.get(j) + " ");
            }
            System.out.println("");
        }

        return data;
    }

    private static int checkClassifications(DataSet d, String s) {
        for (int i = 0; i < d.map.classifications.size(); i++) {
            if (d.map.classifications.get(i).equals(s)) {
                return i;
            }
        }
        return -1;
    }

    private static int findClassification(String instance, int max) {
        if (!isNumeric(instance.substring(0, instance.indexOf(",")))) {
            return 0;
        } else if (!isNumeric(instance.substring(instance.lastIndexOf(",") + 1, instance.length()))) {
            return max;
        } else {
            return 0;
        }
    }

    private static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    //normalize dataset by placing continuous data values into discrete bins
    private static void normalize(ArrayList<Instance> instances) {

        //extract table of feature values
        double[][] features = new double[instances.size()][instances.get(0).features.size()];
        for (int i = 0; i < features.length - 1; i++) {
            for (int j = 0; j < features[0].length - 1; j++) {
                features[i][j] = instances.get(i).unbinnedFeatures.get(j);
            }
        }

        //get statistics
        for (int i = 0; i < features.length - 1; ) {
            
        }
    }
}
