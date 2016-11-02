import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Reader {

    //do you have a big data file that needs reading?
    //well do i have a solution for you!

    public static ArrayList<String> readFile(String fileName) {

        ArrayList<String> file = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(file::add);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return file;
    }
}
