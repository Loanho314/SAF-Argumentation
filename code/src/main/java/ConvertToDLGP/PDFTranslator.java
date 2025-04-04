package ConvertToDLGP;

import java.io.*;
import java.util.regex.*;


public class PDFTranslator {
    public static void main(String[] args) {
        // Specify the input and output file paths
        String inputFilePath = "YAGO-SPARQL query.txt";
        String outputFilePath = "output.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String translatedLine = translateLine(line);
                if (translatedLine != null) {
                    writer.write(translatedLine);
                }
            }

            System.out.println("Translation completed. Output written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String translateLine(String line) {
    	 // Pattern 1: ?a0 rdf:type ?C2.
        if (line.matches("\\?\\w+ rdf:type \\?\\w+\\.")) {
            String[] parts = line.split(" ");
            String a0 = parts[0].substring(1); // Remove the '?' from ?a0
            String C2 = parts[2].substring(1, parts[2].length() - 1); // Remove '?' and '.' from ?C2
            return "String triple10 = " + C2 + " + \"(\" + " + a0 + " + \").\\n\";\n";
        }

        // Pattern 2: ?C1 owl:disjointWith ?C3.
        if (line.matches("\\?\\w+ owl:disjointWith \\?\\w+\\.")) {
            String[] parts = line.split(" ");
            String C1 = parts[0].substring(1); // Remove the '?' from ?C1
            String C3 = parts[2].substring(1, parts[2].length() - 1); // Remove '?' and '.' from ?C3
            return "String triple9 = \"! :- \" + " + C1 + " + \"(X0), \" + " + C3 + " + \"(X0).\\n\";\n";
        }

        // Pattern 3: ?C0 rdfs:subClassOf ?C1.
        if (line.matches("\\?\\w+ rdfs:subClassOf \\?\\w+\\.")) {
            String[] parts = line.split(" ");
            String C0 = parts[0].substring(1); // Remove the '?' from ?C0
            String C1 = parts[2].substring(1, parts[2].length() - 1); // Remove '?' and '.' from ?C1
            return "String triple1 = \" + " + C1 + " + \"(X0) :- \" + " + C0 + " + \"(X0).\\n\";\n";
        }

        // If the line doesn't match any pattern, return null
        return null;
    }
}

