
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ryan Brand
 */
public class DataSet {

    public ArrayList<DataLine> set;
    private HashMap<Integer, Integer> missing;

    public DataSet() {
        set = new ArrayList();
        missing = new HashMap();
    }

    public void printData() {
        for (DataLine d : set) {
            for (int i = 0; i < d.size; i++) {
                d.printVal(i);
            }
            System.out.println("");
        }
    }

    public void addLine(String line, int index) {
        line += "  ";
        int i = 0;
        line = line.replaceAll(",", " ");
        DataLine temp = new DataLine();
        while (!line.equals(" ")) {
            //System.out.println(next.substring(0, next.indexOf(" ")));
            if (!line.substring(0, line.indexOf(" ")).equals("?")) {
                temp.addData(line.substring(0, line.indexOf(" ")), i);
            }
            else{
                missing.put(i, index);
            }
            line = line.substring(line.indexOf(" ") + 1);
            i++;
        }
        set.add(temp);
    }

    public class DataLine {

        private HashMap<Integer, String> strings;
        private HashMap<Integer, Double> nums;
        private int size;

        public DataLine() {
            strings = new HashMap();
            nums = new HashMap();
        }

        public void addData(String data, int key) {
            try {
                double val = Double.parseDouble(data);
                nums.put(key, val);
                size++;
            } catch (NumberFormatException e) {
                strings.put(key, data);
                size++;
            }
        }

        public void printVal(int key) {
            if (strings.containsKey(key)) {
                System.out.print(strings.get(key) + " ");
            } else {
                System.out.print(nums.get(key) + " ");
            }
        }

        public int getSize() {
            return size;
        }
    }
}
