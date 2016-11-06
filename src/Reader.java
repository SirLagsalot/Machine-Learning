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
    
    public static DataSet process(ArrayList<String> lines){
        DataSet data = new DataSet();
        int i = 0;
        for(String s: lines){
            data.addLine(s, i);
            i++;
        }
        return data;
    }
}
