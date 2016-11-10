
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wilson
 */
public class Utilities {
    public static int getClassificationCount(ArrayList<Instance> dataSet){
        Set set = new HashSet<Integer>();
        for(Instance instance : dataSet){
            set.add(instance.classification);
        }
        return set.size();
    }
}
