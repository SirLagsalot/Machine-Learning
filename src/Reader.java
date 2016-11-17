
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Reader {

    public static DataSet readFile(String fileName) {

        ArrayList<String> file = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(file::add);
        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
            System.exit(-1);
        }
        return parseData(file);
    }

    private static DataSet parseData(ArrayList<String> lines) {

        ArrayList<String> classifications = new ArrayList<>();
        ArrayList<Instance> instances = new ArrayList<>();

        //parse each line as one instance
        for (String line : lines) {

            ArrayList<String> attributes = new ArrayList<>(Arrays.asList(line.split(",")));
            boolean classAtStart = !attributes.get(0).matches("[-+]?\\d*\\.?\\d+");

            String classification = classAtStart ? attributes.remove(0) : attributes.remove(attributes.size() - 1);
            if (!classifications.contains(classification)) {
                classifications.add(classification);
            }

            if (classAtStart) {
                ArrayList<Integer> features = new ArrayList<>();
                for (String attribute : attributes) {
                    assert attribute != null;
                    if ("y".equals(attribute)) {
                        features.add(0);
                    } else if ("n".equals(attribute)) {
                        features.add(1);
                    } else {
                        features.add(2);
                    }
                }
                instances.add(new Instance(features, classification, true));
            } else {
                ArrayList<Double> features = new ArrayList<>();
                for (String attribute : attributes) {
                    features.add(Double.parseDouble(attribute));
                }
                instances.add(new Instance(features, classification));
            }
        }
        for (Instance instance : instances) {
            instance.setClassification(classifications.indexOf(instance.className));
        }

        return new DataSet(instances, classifications.size());
    }
}
