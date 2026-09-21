package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
   @SuppressWarnings("unchecked")
public static String csvToJson(String csvString) {
    
    String result = "{}";
    
    try {
        
        CSVReader reader = new CSVReader(new java.io.StringReader(csvString));
        java.util.List<String[]> rows = reader.readAll();
        
        String[] headings = rows.get(0);
        
        JsonObject json = new JsonObject();
        JsonArray prodNums = new JsonArray();
        JsonArray colHeadings = new JsonArray();
        JsonArray data = new JsonArray();
        
        // Add column headings
        for (String heading : headings) {
            colHeadings.add(heading);
        }
        
        // Add data rows
        for (int i = 1; i < rows.size(); i++) {
            
            String[] row = rows.get(i);
            JsonArray dataRow = new JsonArray();
            
            prodNums.add(row[0]);
            
            // Skip ProdNum because it is stored separately
            for (int j = 1; j < row.length; j++) {
                
                if (j == 2 || j == 3) {
                    dataRow.add(Long.parseLong(row[j]));
                }
                else {
                    dataRow.add(row[j]);
                }
            }
            
            data.add(dataRow);
        }
        
        json.put("ProdNums", prodNums);
        json.put("ColHeadings", colHeadings);
        json.put("Data", data);
        
        result = Jsoner.serialize(json);
        
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    
    return result.trim();
}
    @SuppressWarnings("unchecked")
public static String jsonToCsv(String jsonString) {
    
    String result = "";
    
    try {
        
        JsonObject json = (JsonObject) Jsoner.deserialize(jsonString);
        
        JsonArray prodNums = (JsonArray) json.get("ProdNums");
        JsonArray colHeadings = (JsonArray) json.get("ColHeadings");
        JsonArray data = (JsonArray) json.get("Data");
        
        java.io.StringWriter stringWriter = new java.io.StringWriter();
        CSVWriter writer = new CSVWriter(stringWriter);
        
        // Write column headings
      String[] headings = new String[colHeadings.size()];
        
        for (int i = 0; i < colHeadings.size(); i++) {
            headings[i] = (String) colHeadings.get(i);
        }
        
        writer.writeNext(headings);
        
        // Write data rows
        for (int i = 0; i < data.size(); i++) {
            
            JsonArray dataRow = (JsonArray) data.get(i);
            String[] csvRow = new String[colHeadings.size()];
            
            csvRow[0] = (String) prodNums.get(i);
            
            for (int j = 0; j < dataRow.size(); j++) {
    
    if (j == 2) {
        csvRow[j + 1] = String.format("%02d", ((Number) dataRow.get(j)).intValue());
    }
    else {
        csvRow[j + 1] = String.valueOf(dataRow.get(j));
    }
}
            
            writer.writeNext(csvRow);
        }
        
        writer.close();
        result = stringWriter.toString();
        
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    
    return result.trim();
}
}