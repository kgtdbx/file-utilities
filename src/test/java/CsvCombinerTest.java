import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvCombinerTest {

    @Test
    public void testGetCsvSchemaWithCorrectOrder() {
        //Arrange
        List<String> expectedSchema = new ArrayList<>();
        expectedSchema.add("field1");
        expectedSchema.add("field2");
        expectedSchema.add("field3");
        expectedSchema.add("field4");

        //Act
        List<String> actualSchema = CsvCombiner.getCsvSchema("/Users/westonsankey/Documents/Git/file-utilities/src/test/resources/testcsv/test1.csv");

        //Assert
        assertThat(actualSchema, is(expectedSchema));
    }

    @Test
    public void testGetCsvSchemaWithIncorrectOrder() {
        //Arrange
        List<String> expectedSchema = new ArrayList<>();
        expectedSchema.add("field2");
        expectedSchema.add("field1");
        expectedSchema.add("field3");
        expectedSchema.add("field4");

        //Act
        List<String> actualSchema = CsvCombiner.getCsvSchema("/Users/westonsankey/Documents/Git/file-utilities/src/test/resources/testcsv/test1.csv");

        //Assert
        assertThat(actualSchema, is(not(expectedSchema)));
    }

    @Test
    public void testGetCsvSchemaWithIncorrectValues() {
        //Arrange
        List<String> expectedSchema = new ArrayList<>();
        expectedSchema.add("field");
        expectedSchema.add("field2");
        expectedSchema.add("field3");
        expectedSchema.add("field4");

        //Act
        List<String> actualSchema = CsvCombiner.getCsvSchema("/Users/westonsankey/Documents/Git/file-utilities/src/test/resources/testcsv/test1.csv");

        //Assert
        assertThat(actualSchema, is(not(expectedSchema)));
    }

    @Test
    public void testGetCsvFilesFromDirectory() {
        //Arrange
        List<String> expectedFiles = new ArrayList<>();
        expectedFiles.add("test1.csv");
        expectedFiles.add("test2.csv");
        expectedFiles.add("test3.csv");

        //Act
        Path directory = Paths.get("/Users/westonsankey/Documents/Git/file-utilities/src/test/resources/testcsv");
        List<String> actualFiles = CsvCombiner.getCsvFilesFromDirectory(directory);

        //Assert
        assertThat(actualFiles, is(expectedFiles));
    }
}
