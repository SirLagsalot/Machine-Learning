
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
        boolean classAtStart = false, numeric = false;
        String[] header = lines.remove(0).split(",");

        //determine data type
        if ("numeric".equalsIgnoreCase(header[0])) {
            numeric = true;
        }
        if ("first".equalsIgnoreCase(header[1])) {
            classAtStart = true;
        }

        //parse each line as one instance
        for (String line : lines) {

            ArrayList<String> attributes = new ArrayList<>(Arrays.asList(line.split(",")));
            String classification = classAtStart ? attributes.remove(0) : attributes.remove(attributes.size() - 1);

            //this is really crude and expense, will changes to a set but this works for now
            if (!classifications.contains(classification)) {
                classifications.add(classification);
            }

            if (!numeric) {

                ArrayList<Integer> features = new ArrayList<>();
                for (String attribute : attributes) {
                    switch (attribute) {
                        case "y":
                            features.add(0);
                            break;
                        case "n":
                            features.add(1);
                            break;
                        case "?":
                            features.add(2);
                            break;
                        default:
                            System.out.println("Error in data parsing");
                            System.exit(-1);
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
        //again really crude, might find faster way to do this but is functional
        for (Instance instance : instances) {
            instance.setClassification(classifications.indexOf(instance.className));
        }
        return new DataSet(instances);
    }
}

//    private static DataSet process(ArrayList<String> lines) {
//
//        DataSet data = new DataSet();
//        Instance current = new Instance();
//        String temp = lines.get(0);
//
//        int attributes = 1;
//        while (temp.contains(",")) {
//            temp = temp.substring(temp.indexOf(",") + 1);
//            attributes++;
//        }
//
//        int classificationIndex = findClassification(lines.get(0), attributes);
//        for (String s : lines) {
//
//            s = s + ", ";
//
//            current.classification = classificationIndex;
//            int counter = 0;
//
//            while (s.contains(",")) {
//                if (isNumeric(s.substring(0, s.indexOf(",")))) {
//                   // System.out.println(s);
//                   // System.out.println("");
//                    current.unbinnedFeatures.add(Double.parseDouble(s.substring(0, s.indexOf(","))));
//                } else if (checkClassifications(data, s.substring(0, s.indexOf(","))) == -1) {
//                    current.unbinnedFeatures.add((double) counter);
//                    counter++;
//                    data.addClassification(s.substring(0, s.indexOf(",")));
//                } else {
//                    current.unbinnedFeatures.add((double) checkClassifications(data, s.substring(0, s.indexOf(","))));
//                }
//                s = s.substring(s.indexOf(",") + 1);
//            }
//            data.data.add(current);
//            current = new Instance();
//        }
//
////        for (Instance i : data.data) {
////            for (int j = 0; j < i.unbinnedFeatures.size(); j++) {
////                System.out.print(i.unbinnedFeatures.get(j) + " ");
////            }
////            System.out.println("");
////        }
//        return data;
//    }
//
//    private static int checkClassifications(DataSet d, String s) {
//
//        for (int i = 0; i < d.data.size(); i++) {
//            if (d.getClassification(i).equals(s)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private static int findClassification(String instance, int max) {
//
//        if (!isNumeric(instance.substring(0, instance.indexOf(",")))) {
//            return 0;
//        } else if (!isNumeric(instance.substring(instance.lastIndexOf(",") + 1, instance.length()))) {
//            return max;
//        } else {
//            return 0;
//        }
//    }
//
//    private static boolean isNumeric(String s) {
//        return s.matches("[-+]?\\d*\\.?\\d+");
//    }
//}
