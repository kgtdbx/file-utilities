import java.io.IOException;

public class App {

    public static void main(String[] args) {
        if (args[0].equals("combine-csv")) {
            String directory = args[1];
            try {
                CsvCombiner.combineFiles(directory);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            catch (CsvSchemaMismatchException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}