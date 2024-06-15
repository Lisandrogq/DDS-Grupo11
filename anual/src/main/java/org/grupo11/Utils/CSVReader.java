package org.grupo11.Utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class CSVReader {
    /**
     * 
     * @param csvFileName - name of the csv file to look for in the resources folder
     */
    public static void read(String csvFileName, Consumer<List<String>> onRead) {
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(csvFileName);
            com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new InputStreamReader(inputStream));
            String[] line;

             // Skip the header line
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                List<String> fields = Arrays.asList(line);
                onRead.accept(fields);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
