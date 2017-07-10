import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvCombiner {

    //Private constructor so the class cannot be instantiated
    private CsvCombiner(){}

    /**
     * Combines a set of CSV files with identical schemas into a single CSV file
     *
     * @param  directory  the path where the CSV files are located
     */
    public static void combineFiles(String directory) throws IOException, CsvSchemaMismatchException{

        //Check to make sure directory exists
        Path directoryPath = Paths.get(directory);

        if (!Files.exists(directoryPath)) {
            throw new IOException("Directory does not exist.");
        }

        List<String> csvFileNames = getCsvFilesFromDirectory(directoryPath);
        String sourceFilePath = String.format("%s/%s", directory, csvFileNames.get(0));
        List<String> masterSchema = getCsvSchema(sourceFilePath);

        //Create new empty CSV to hold combined data
        String destinationFilePath = createNewCsvFile(directory);

        //Append header to master file
        writeHeaderToDestinationFile(destinationFilePath, masterSchema);

        //Append contents of all files to the combined file
        for(String filename : csvFileNames) {
            sourceFilePath = String.format("%s/%s", directory, filename);

            //Validate that the schema matches the master schema
            List<String> schema = getCsvSchema(sourceFilePath);
            if (!schema.equals(masterSchema))
                throw new CsvSchemaMismatchException("CSV schema did not match master schema");

            //Read contents of CSV and append to master file
            appendContentsToCombinedFile(sourceFilePath, destinationFilePath);
        }
    }

    /**
     * Writes the CSV header to the destination file
     *
     * @param  destinationFilePath  the path of the file that will be written to
     * @param  schema an ordered list of the CSV file header fields
     */
    public static void writeHeaderToDestinationFile(String destinationFilePath, List<String> schema) {
        String header = String.join(",", schema);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFilePath));
            bw.write(header + "\n");
            bw.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the schema of the provided CSV file. The schema is defined as the
     * name and ordering of the file header
     *
     * @param  directoryPath  the path where the CSV files are located
     * @return a list of CSV file names that reside in the input directory
     */
    public static List<String> getCsvFilesFromDirectory(Path directoryPath) {
        List<String> csvFiles = new ArrayList<String>();
        String[] files = new java.io.File(directoryPath.toString()).list();

        for (String fileName : files) {
            File file = new File(fileName);
            //Assuming that the file exists - should we check just in case?
            if(file.getName().endsWith(".csv")) {
                csvFiles.add(fileName);
            }
        }

        return csvFiles;
    }

    /**
     * Gets the schema of the provided CSV file. The schema is defined as the
     * name and ordering of the file header
     *
     * @param  filename  the path where the CSV file will be created
     * @return an ordered list of the CSV file header fields
     */
    public static List<String> getCsvSchema(String filename) {
        List<String> fields = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String header = reader.readLine();
            fields = Arrays.asList(header.split(","));
            reader.close();
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return fields;
    }

    /**
     * Reads the non-header row contents of the input CSV file
     * and appends to the combined file.
     *
     * @param  sourceFilePath  the path of the CSV file whose contents will be appended to the combined file
     * @param  destinationFilePath the path of the CSV that will be appended to
     */
    public static void appendContentsToCombinedFile(String sourceFilePath, String destinationFilePath) {
        try {
            RandomAccessFile rf = new RandomAccessFile(sourceFilePath, "rw");

            //Read the header row to set the pointer to the start of the next row
            rf.readLine();

            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(destinationFilePath, true));

            int i;
            byte[] b = new byte[16384];

            while((i = rf.read(b)) != -1) {
                os.write(b, 0, i);
            }

            os.close();
            rf.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new CSV file in the provided directory
     *
     * @param  directory  the path where the CSV file will be created
     * @return the full path of the newly created CSV file
     */
    public static String createNewCsvFile(String directory) {
        String combinedFilePath = String.format("%s/%s", directory, "combined.csv");
        try {
            new File(combinedFilePath).createNewFile();
        }
        catch (IOException ex) {
            System.out.println("Unable to create CSV file.");
        }
        return combinedFilePath;
    }
}