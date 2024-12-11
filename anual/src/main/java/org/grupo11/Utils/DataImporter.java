package org.grupo11.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.javalin.http.UploadedFile;

public class DataImporter {

    public List<String> readCSV(UploadedFile file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.content()));
            String line;
            List<String> fields = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                fields.add(line);
            }
            return fields.subList(1, fields.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
